package com.cave.r.dalton.flashcubes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

/**
 * Created by dalton on 3/7/16.
 */
public class MainPanel extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = MainPanel.class.getSimpleName();
    private int screenWidth, screenHeight;
    private Universe universe;
    MoveableRectangle box;
    MoveableRectangle box2;
    MoveableRectangle box3;

    private static final int BOX_WIDTH = 100;
    private static final int BOX_HEIGHT = 100;

    //Index of box currently pressed with finger

    public MainPanel(Context context, int numCubes){
        super(context);
        box = new MoveableRectangle(100, 100, BOX_WIDTH, BOX_HEIGHT);
        box2 = new MoveableRectangle(500, 500, BOX_WIDTH, BOX_HEIGHT);
        box3 = new MoveableRectangle(800, 800, BOX_WIDTH, BOX_HEIGHT);
        WindowManager wm = (WindowManager) context.getSystemService((Context.WINDOW_SERVICE));
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        getHolder().addCallback(this);
        universe = new Universe(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        universe.setRunning(true);
        universe.start();

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
                universe.join();
                retry = false;
            }
            catch(InterruptedException e){
                //retry shutting down thread
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        box.handleTouch(event);
        box2.handleTouch(event);
        box3.handleTouch(event);
        if(MoveableRectangle.collision(box, box2)){
            if(box.getPickedUp())
                box2.setColor(Color.RED);
            else box.setColor(Color.RED);
        }
        if(MoveableRectangle.collision(box, box3)){
            if(box.getPickedUp())
                box3.setColor(Color.RED);
            else box.setColor(Color.RED);
        }
        if(MoveableRectangle.collision(box3, box2)){
            if(box3.getPickedUp())
                box2.setColor(Color.RED);
            else box3.setColor(Color.RED);
        }

        return true;
    }

    public void render(Canvas canvas){

        //Draw the background
        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY);
        canvas.drawPaint(paint);

        //Draw the Boxes
        box.draw(canvas);
        box2.draw(canvas);
        box3.draw(canvas);
    }

    public void update(){
        //do nothing
    }
}
