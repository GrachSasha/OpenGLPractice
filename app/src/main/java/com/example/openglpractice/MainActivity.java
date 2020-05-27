package com.example.openglpractice;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity {
//static Logger logger = Logger.getLogger();
    //=================================================
    private GLSurfaceView glSurfaceView;
             // треугольник
            // x    y   z
    float[] playerVertices = {

            -3f, -2f, 0f,
            -2.5f, -2f, 0f,
            -2.75f, -1.75f, 0f,

    };

//    float[] enemyVertices = {
//
//            -3f, -2f, 0f,
//            -2.5f, -2f, 0f,
//            -2.75f, -1.75f, 0f,
//
//    };

    float[] platform1Vertices = {

            -3f, -3f, 0,
            1f, -3f, 0,

            -3f, -2.75f, 0,
            1f, -2.75f, 0
    };

    float[] platform2Vertices = {

            -1f, -2.75f, 0,
            0.5f, -2.75f, 0,
            -1f, -2.5f,0,
            0.5f, -2.5f, 0
    };

    float[] platform3Vertices = {

            2f, -3f, 0,
            3f, -3f, 0,
            2f, -0.75f,0,
            3f, -0.75f, 0
    };

    float[] platform4Vertices = {
            0.5f, -1.5f, 0,
            1f, -1.5f, 0,

            0.5f, -1.25f,0,
            1f, -1.25f, 0
    };

    float[] platform5Vertices = {
            3f, -3f, 0,
            6f, -3f, 0,

            3f, -2.75f,0,
            6f, -2.75f, 0
    };

    float[] platform6Vertices = {
            -1.25f, -0.25f, 0,
            2.25f, -0.25f, 0,

            -1.25f, 0,0,
            2.25f, 0, 0
    };

    float[] platform7Vertices = {
            -2f, -2f, 0,
            -0.5f, -2f, 0,

            -2f, -1.5f, 0,
            -0.5f, -1.5f, 0
    };

    float[]gamePadVertices = {
            2.0f, -1.5f, 0.1f,
            3.0f, -1.5f, 0.1f,
            2.5f, -1.0f, 0.1f,

            2.0f, -2.5f, 0.1f,
            3.0f, -2.5f, 0.1f,
            2.5f, -3.0f, 0.1f,

            3.0f, -1.5f, 0.1f,
            3.0f, -2.5f, 0.1f,
            3.5f, -2.0f, 0.1f,

    };

    //init Game Control
    Game game;

    //init render
    static OpenGLRenderer render;


    //init Игрока с физикой и контроллер
    dynamicObject player = new dynamicObject(playerVertices);
    gameController playerController = new gameController(player);

    //dynamicObject enemy = new dynamicObject(enemyVertices);
    //gameController enemyController = new gameController(enemy);

    //init объектов
    staticObject platform1 = new staticObject(platform1Vertices);
    staticObject platform2 = new staticObject(platform2Vertices);
    staticObject platform3 = new staticObject(platform3Vertices);
    staticObject platform4 = new staticObject(platform4Vertices);
    staticObject platform5 = new staticObject(platform5Vertices);
    staticObject platform6 = new staticObject(platform6Vertices);
    staticObject platform7 = new staticObject(platform7Vertices);



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
        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;
        Toast.makeText(this, "WIDTH = " + screenWidth + "HEIGHT = " + screenHeight, Toast.LENGTH_LONG).show();

        //Инициализация рендера
        glSurfaceView = new GLSurfaceView(this);
        game = new Game(glSurfaceView);
        glSurfaceView.setEGLContextClientVersion(2);
        render = new OpenGLRenderer(this);

        //Грузим корды динамических объектов
        render.prepareDynamicModels(player);
        //render.prepareDynamicModelsForEnemy(enemy);

        render.prepareGamePad(gamePadVertices);

        render.preparePlatform(platform1);
        render.preparePlatform(platform2);
        render.preparePlatform(platform3);
        render.preparePlatform(platform4);
        render.preparePlatform(platform5);
        render.preparePlatform(platform6);
        render.preparePlatform(platform7);

        //Рендер на весь экран
        glSurfaceView.setRenderer(render);
        setContentView(glSurfaceView);

        //Game game = new Game(enemyController);

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

        if(event.getX()<200) {
            playerController.jump();
        } else {
            playerController.walk();}
        return true;
    }

    private boolean supportES2() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }

}