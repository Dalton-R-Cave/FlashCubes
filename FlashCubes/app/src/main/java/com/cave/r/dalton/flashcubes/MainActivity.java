package com.cave.r.dalton.flashcubes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MainPanel(this, 5));
        Log.d(TAG, "View Added");
    }

    @Override
    protected void onDestroy(){

        Log.d(TAG, "Destroying View...");
        super.onDestroy();
    }

    @Override
    protected void onStop(){
        Log.d(TAG, "Stopping View...");
        super.onStop();
    }
}
