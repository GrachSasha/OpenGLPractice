package com.example.openglpractice;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.Toast;

public class MainActivity extends Activity {
    //=================================================
    private GLSurfaceView glSurfaceView;

    //init Game Control
    Handler controlHandler;
    Game game;
    static int screenWidth;
    int screenHeight;

    //init render
    static OpenGLRenderer render;

//===================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!supportES2()) {
            Toast.makeText(this, "OpenGL ES 2.0 is not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenWidth = displaymetrics.widthPixels;
        screenHeight = displaymetrics.heightPixels;
        Toast.makeText(this, "WIDTH = " + screenWidth + "HEIGHT = " + screenHeight, Toast.LENGTH_LONG).show();

        //Create Handler for Controller
        controlHandler = new Handler();

        //Инициализация рендера
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        render = new OpenGLRenderer(this);

        //Рендер на весь экран
        glSurfaceView.setRenderer(render);
        setContentView(glSurfaceView);

        game = new Game();
        game.prepareModelsForLevel();

    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
        Runtime.getRuntime().freeMemory();

    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
        Runtime.getRuntime().freeMemory();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        game.getTouchEvent(event);
        return true;
    }

    private boolean supportES2() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }

}