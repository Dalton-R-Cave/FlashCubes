package com.cave.r.dalton.flashcubes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by dalton on 3/7/16.
 */
public class Universe extends SurfaceView implements SurfaceHolder.Callback {

    private static final int NUM_OF_BOXES = 5;

    private static final String TAG = Universe.class.getSimpleName();
    private MainThread mainThread;
    private MoveableRectangle boxes[];

    private Status universeState;

    private static final int BOX_WIDTH = 200;
    private static final int BOX_HEIGHT = 200;

    public Universe(Context context){
        super(context);
        universeState = Status.START;
        boxes = new MoveableRectangle[NUM_OF_BOXES];
        initializeBoxes();
        setCubeDisplay("START");
        getHolder().addCallback(this);
        mainThread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    public void initializeBoxes(){
        for (int i = 0; i < NUM_OF_BOXES; i++){
            boxes[i] = new MoveableRectangle(500 + (i*BOX_WIDTH), 600, BOX_WIDTH, BOX_HEIGHT);
        }
    }

    public void setCubeDisplay(String toDisplay){
        int length = toDisplay.length();
        int currentIndex = 0;
        int lettersPerBox = (int)(Math.floor(length / NUM_OF_BOXES));
        for (int i = 0; i < NUM_OF_BOXES; i++){
            if(i==NUM_OF_BOXES - 1){
                boxes[i].setDisplay(toDisplay.substring(currentIndex, length - 1));
            }
            boxes[i].setDisplay(toDisplay.substring(currentIndex, currentIndex += lettersPerBox));
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mainThread.setRunning(true);
        mainThread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //do nothing
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Surface is being destroyed");

        boolean retry = true;
        while(retry){
            try{
                mainThread.join();
                retry = false;
            }
            catch(InterruptedException e){
                //retry shutting down thread
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        for(int i = 0; i < NUM_OF_BOXES; i++){
            boxes[i].handleTouch(event);
        }

        return true;
    }

    public void render(Canvas canvas){

        //Draw the background
        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY);
        if (paint != null) {
            canvas.drawPaint(paint);
        }

        //Draw the Boxes
        for(int i = 0; i < NUM_OF_BOXES; i++){
            boxes[i].draw(canvas);
        }
    }

    public void update(){
        //do nothing
    }
}
