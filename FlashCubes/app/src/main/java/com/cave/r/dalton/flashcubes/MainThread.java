package com.cave.r.dalton.flashcubes;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by dalton on 3/7/16.
 */
public class MainThread extends Thread {

    private static final String TAG = MainThread.class.getSimpleName();

    //FRAMERATE VARIABLES:
    private final static int MAX_FPS = 30; //desired frames per second
    private final static int MAX_FRAME_SKIPS = 5; //maximum number of frames to be skipped
    private final static int FRAME_PERIOD = 1000/MAX_FPS; // the frame period

    //DISPLAY VARIABLES:
    private SurfaceHolder surfaceHolder; //Surface holder that can access the physical surface
    private Universe mainPanel; //The actual view that handles inputs and draws to the surface

    //FLAGS:
    private boolean running; //flag to hold the game state

    public MainThread(SurfaceHolder surfaceHolder, Universe mainPanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.mainPanel = mainPanel;
    }

    @Override
    public void run(){
        Canvas canvas;
        Log.d(TAG, "Starting logic loop");

        //for fps management
        long beginTime; //the time when the cycle began
        long timeDiff; //the time it took for the cycle to execute
        int sleepTime = 0; //ms to sleep(< 0 if we're behind)
        int framesSkipped; //number of frames being skipped

        while (running){
            canvas = null;
            //try locking the canvas for exclusive pixel editing in the surface
            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    beginTime = System.currentTimeMillis();
                    framesSkipped = 0; //reset the frames skipped
                    this.mainPanel.update(); //update game state
                    this.mainPanel.render(canvas); //render state to the screen
                    timeDiff = System.currentTimeMillis() - beginTime;
                    sleepTime = (int)(FRAME_PERIOD - timeDiff); //calculate sleep time

                    if (sleepTime > 0){ //We are ok
                        try{
                            //send the thread to sleep to save battery
                            Thread.sleep(sleepTime);
                        }
                        catch(InterruptedException e){
                            //do nothing for now
                        }
                    }

                    while(sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS){ //We need to catch up
                        this.mainPanel.update(); //update without rendering
                        sleepTime += FRAME_PERIOD;
                        framesSkipped++;
                    }


                }
            }
            finally{
                //in case of n exception the surface is not left in an inconsistent state
                if (canvas != null){
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    public boolean getRunning(){
        return running;
    }

    public void setRunning(boolean running){
        this.running = running;
    }
}
