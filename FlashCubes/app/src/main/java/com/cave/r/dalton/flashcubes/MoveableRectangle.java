package com.cave.r.dalton.flashcubes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by dalton on 3/8/16.
 */
public class MoveableRectangle {

    private static final String TAG = MoveableRectangle.class.getSimpleName();

    private static final int TEXT_SIZE = 50;
    private static final int OFFSET_PER_PIXEL = 10;

    private int x, y, width, height, textXOffset, textYOffset;
    private boolean pickedUp;
    private int color;
    private String display;
    private Status state;
    private int touching; //How many rectangles it is in contact with
    private int lastTouch;
    public ArrayList<MoveableRectangle> touchCallBack; //An array of all the of currently touching rectangles
    private int randomNumber;
    private int total;
    private int index;
    private Random generator;


    public MoveableRectangle(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        textYOffset = TEXT_SIZE / 2;
        pickedUp = false;
        color = Color.WHITE;
        setDisplay("?");
        state = Status.START;
        randomNumber = -1;
        index = -1;
        total = 0;
        lastTouch = 0;
        touchCallBack = new ArrayList<>();
    }

    public int getIndex(){
        return index;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public int getX(){
        return x;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getY(){
        return y;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getWidth(){
        return width;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public int getTextXOffset(){
        return textXOffset;
    }

    public void setTextXOffset(int textXOffset){
        this.textXOffset = textXOffset;
    }

    public int getTextYOffset(){
        return textYOffset;
    }

    public void setTextYOffset(int textYOffset){
        this.textYOffset = textYOffset;
    }

    public boolean getPickedUp(){
        return pickedUp;
    }

    public void setPickedUp(boolean pickedUp){
        this.pickedUp = pickedUp;
    }

    public int getColor(){
        return color;
    }

    public void setColor(int color){
        this.color = color;
    }

    public String getDisplay(){
        return display;
    }

    public void setDisplay(String display){
        this.display = display;
        if(display.length() == 1)
            textXOffset = 1;
        else
            textXOffset = display.length()/2;
    }

    public Status getState(){
        return state;
    }

    public void setState(Status state){
        this.state = state;
    }

    public int getTouching(){
        return touching;
    }

    public void setTouching(int touching){
        this.touching = touching;
    }

    public int getRandomNumber(){
        return randomNumber;
    }

    public void setRandomNumber(int randomNumber){
        this.randomNumber = randomNumber;
    }

    public boolean contains(int xCo, int yCo){
        if (xCo >= (x - (width/2)) && xCo <= (x + (width /2))) {
            if (yCo >= (y - (height / 2)) && yCo <= (y + (height / 2))) {
                if (xCo < x) {
                } else if (xCo > x) {
                }
                return true;
            }
        }
        return false;
    }

    public void setLocation(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getLeft(){
        return (x-width/2);
    }

    public int getRight(){
        return (x+width/2);
    }

    public int getTop(){
        return (y-height/2);
    }

    public int getBottom(){
        return (y+height/2);
    }

    public void checkForCollisions(MoveableRectangle[] boxes, int thisBox){
        touching = 0;
        for (int i = 0; i < boxes.length; i++){
            if (i == thisBox) continue;
            else if(collision(this, boxes[i])){
                touching++;
                if (!touchCallBack.contains(boxes[i])) {
                    touchCallBack.add(boxes[i]);
                }
            }
            else{
                touchCallBack.remove(boxes[i]);
            }
        }
    }

    public void checkGenerate(){
        if(state == Status.GENERATING) return;

        if (touching == 0){
            state = Status.GENERATING;
            generator = new Random();
            randomNumber = generator.nextInt(6) + 1;
            display = randomNumber + "";
            Log.d(TAG, "Cube changing to generating state");
        }
    }

    public void checkScoring(){

        if (touching > 0){
            if(touching == lastTouch) return;
            lastTouch = touching;
            total = 0;
            for (int i = 0; i < touchCallBack.size(); i++){
                Log.d(TAG,touchCallBack.get(i).getRandomNumber() + "");
                total += touchCallBack.get(i).getRandomNumber();
            }
            total += this.getRandomNumber();
            state = Status.SCORING;

            display = total + "";
            Log.d(TAG, "Cubes starting to add");
        }
        else{
            lastTouch = 0;
            state = Status.GENERATING;
            total = 0;
            display = randomNumber + "";
        }
    }

    public static boolean collision(MoveableRectangle moving, MoveableRectangle other){
        if (moving.contains(other.getLeft(), other.getTop())) return true;

        if(moving.contains(other.getLeft(), other.getBottom()))return true;

        if(moving.contains(other.getRight(), other.getTop())) return true;

        if(moving.contains(other.getRight(), other.getBottom())) return true;

        return false;
    }

    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(TEXT_SIZE);
        canvas.drawRect(getLeft(),getTop(),getRight(),getBottom(), paint);
        paint.setColor(Color.WHITE);
        if(color == Color.WHITE) paint.setColor(Color.BLACK);
        canvas.drawText(display,x-(textXOffset * OFFSET_PER_PIXEL),y+textYOffset,paint);
    }

    public void handleTouch(MotionEvent event){
        if (state == Status.DONE) return;
        int eX = (int)event.getX();
        int eY = (int)event.getY();

        if(pickedUp){
            setLocation(eX, eY);
        }

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if(contains(eX, eY)){
                pickedUp = true;
                setColor(Color.BLUE);
            }
        }

        if(event.getAction() == MotionEvent.ACTION_UP){
            if(pickedUp) setColor(Color.WHITE);
            pickedUp = false;
        }
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("State: " + state + ", Random Number: " + randomNumber);
        return builder.toString();
    }

    //Return 0 if same
    //Return -1 if this is to the left
    //Return 1 if this is to the right
    public int compareX(MoveableRectangle other){
        if (other.getX() > this.getX()) return -1;
        else if (other.getX() < this.getX()) return 1;
        else return 0;
    }

}
