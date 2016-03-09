package com.cave.r.dalton.flashcubes;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Created by dalton on 3/8/16.
 */
public class MoveableRectangle {
    private int x, y, width, height;
    private boolean pickedUp;
    private int color;
    private String display;

    public MoveableRectangle(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        pickedUp = false;
        color = Color.WHITE;
        display = "?";
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
    }

    public boolean contains(int xCo, int yCo){
        if (xCo > (x - (width/2)) && xCo < (x + (width /2))){
            if(yCo > (y - (height/2)) && yCo < (y + (height/2))){
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
        paint.setTextSize(50);
        canvas.drawRect(getLeft(),getTop(),getRight(),getBottom(), paint);
        paint.setColor(Color.WHITE);
        if(color == Color.WHITE) paint.setColor(Color.BLACK);
        canvas.drawText(display,x-10,y+25,paint);
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
