package com.cave.r.dalton.flashcubes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

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
    private int touching;
    private int randomNumber;
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
        if (xCo >= (x - (width/2)) && xCo <= (x + (width /2))){
            if(yCo >= (y - (height/2)) && yCo <= (y + (height/2))){
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
            if(collision(this, boxes[i])) touching++;
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

}
