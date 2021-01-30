package com.example.openglpractice;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import static com.example.openglpractice.MainActivity.render;
import static com.example.openglpractice.MainActivity.screenWidth;

//Thread для теста
class Game implements Runnable {
    // треугольник
    // x    y   z

    private float[] playerVertices = {
                                //x     y
            -3f, -1f, 1f,       0.125f, 1,
            -2.5f, -1f, 1f,     0, 1,

            -3f, -0.5f, 1f,     0.125f, 0.5f,
            -2.5f, -0.5f, 1f,   0, 0.5f,

//            -3f, -1f, 0f,       0f, 0.5f,
//            -2.5f, -1f, 0f,     0, 1f,
//
//            -3f, -0.5f, 0f,     0.125f, 1,
//            -2.5f, -0.5f, 0f,   0.125f, 0.5f,




//            -3f, -1f, 0f,       0, 0.5f,
//            -2.5f, -1f, 0f,     0.125f, 0.5f,
//
//            -3f, -0.5f, 0f,     0, 1,
//            -2.5f, -0.5f, 0f,   0.125f, 1,
    };

    private float[] enemyVertices = {

            -3f, -2f, 0f,       0, 0,
            -2.5f, -2f, 0f,     0, 1,

            -3f, -1.5f, 0f,     1, 0,
            -2.5f, -1.5f, 0f,   1, 1,

    };


    private float[] platform1Vertices = {

            -3f, -3f, 0,    0, 0,
            1f, -3f, 0,     0, 1,

            -3f, -2.75f, 0, 1, 0,
            1f, -2.75f, 0,  1, 1,
    };

    private float[] platform2Vertices = {

            -1f, -2.75f, 0,     0, 0,
            0.5f, -2.75f, 0,    0, 1,
            -1f, -2.5f,0,       1, 0,
            0.5f, -2.5f, 0,     1, 1,
    };

    private float[] platform3Vertices = {

            2f, -3f, 0,     0, 0,
            3f, -3f, 0,     0, 1,
            2f, -0.75f,0,   1, 0,
            3f, -0.75f, 0,  1, 1,
    };

    private float[] platform4Vertices = {
            0.5f, -1.5f, 0, 0, 0,
            1f, -1.5f, 0,   0, 1,

            0.5f, -1.25f,0, 1, 0,
            1f, -1.25f, 0,  1, 1,
    };

    private float[] platform5Vertices = {
            3f, -3f, 0,     0, 0,
            6f, -3f, 0,     0, 1,

            3f, -2.75f,0,   1, 0,
            6f, -2.75f, 0,  1, 1,
    };

    private float[] platform6Vertices = {
            -1.25f, -0.25f, 0,  0, 0,
            2.25f, -0.25f, 0,   0, 1,

            -1.25f, 0,0,        1, 0,
            2.25f, 0, 0,        1, 1,
    };

    private float[] platform7Vertices = {
            -2f, -2f, 0,        0, 0,
            -0.5f, -2f, 0,      0, 1,

            -2f, -1.5f, 0,      1, 0,
            -0.5f, -1.5f, 0,    1, 1,
    };

    private float[] platform8Vertices = {
            -2.5f, -2.5f, 0.5f,        0, 0,
            -1f, -2.5f, 0.5f,      0, 1,

            -2.5f, -2f, 0.5f,      1, 0,
            -1f, -2f, 0.5f,    1, 1,
    };

    private float[]gamePadVertices = {
            2.0f, -1.5f, 0.1f, 0,0,
            3.0f, -1.5f, 0.1f, 0,1,
            2.5f, -1.0f, 0.1f, 1,1,

            2.0f, 3.0f, 0.1f, 0,0,
            3.0f, 3.0f, 0.1f, 0,1,
            2.0f, 3.5f, 0.1f, 1,0,
            3.0f, 3.5f, 0.1f, 1,1,
//            2.0f, -2.5f, 0.1f,  0,0,
//            3.0f, -2.5f, 0.1f,  0,1,
//            2.5f, -3.0f, 0.1f,  1,1,
//
//            3.0f, -1.5f, 0.1f,  0,0,
//            3.0f, -2.5f, 0.1f,  0,1,
//            3.5f, -2.0f, 0.1f,  1,1

    };


    //===pools===//
    //todo хз нужен ли volatile
    public static int gameObjectCounter = 0;
    public static int staticObjectCounter = 0;

    public static volatile dynamicObject dynamicObjectPool[] = new dynamicObject[10];
    public static volatile  staticObject staticObjectPool[]= new staticObject[10];
    public static volatile TextWriter textWriter;
    //===pools===//

    //===fields===//
    private Context gameContext;
    static String GAME_LOG = " Game ";
    playerController playerController;
    Thread textWriterThread;
    //===fields===//

    public Game() {
        //todo убрать загрузку уровня из конструктора
        createMenu();
        prepareModelsForLevel1();
        textWriterThread = new Thread(this);
        textWriterThread.start();
    }

    public void prepareModelsForLevel1(){
        textWriter = new TextWriter();
        render.drawSelector = 1;
        dynamicObject player = new dynamicObject(playerVertices, true, "child_go");
        Log.i(GAME_LOG, "Players load");

        playerController = new playerController(player);
        Log.i(GAME_LOG, "Player controller load");
        //init объектов
        staticObject platform1 = new staticObject(platform1Vertices,"box");
        staticObject platform2 = new staticObject(platform2Vertices,"box");
        staticObject platform3 = new staticObject(platform3Vertices,"box");
        staticObject platform4 = new staticObject(platform4Vertices,"box");
        staticObject platform5 = new staticObject(platform5Vertices,"box");
        staticObject platform6 = new staticObject(platform6Vertices,"box");
        staticObject platform7 = new staticObject(platform7Vertices,"box");
        staticObject platform8 = new staticObject(platform8Vertices,"box");
        Log.i(GAME_LOG, "Platforms load");
        render.prepareGamePad(gamePadVertices);
    }

    public void getTouchEvent(MotionEvent event) {
        //todo привести в порядок вызов меню
        render.drawSelector = 1;
//        prepareModelsForLevel1();
        float sector = screenWidth/3;
        float cord = event.getX();
        if(cord < sector){playerController.walkLeft();}
//        if((cord > sector) && (cord < sector*2)){playerController.jump();}

        if((cord > sector*2) && (cord < sector*3)){playerController.walkRight();}

    }

    public void createMenu() {
        render.drawSelector = 0;
  }

  //TEST=====================================//

    @Override
    public void run() {
        do {
            textWriter.setText("mudlo",0.5f, new float[]{dynamicObjectPool[0].getEyeX(), dynamicObjectPool[0].getEyeY()});
            Log.i("MAINACTIVITY", "AFTER textWriter.setText(9)");

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        while(true);

    }

    //TEST=====================================//
}