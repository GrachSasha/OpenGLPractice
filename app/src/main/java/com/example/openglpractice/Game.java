package com.example.openglpractice;

import android.util.Log;
import android.view.MotionEvent;


import static com.example.openglpractice.MainActivity.render;
import static com.example.openglpractice.MainActivity.screenWidth;

class Game {
    // треугольник
    // x    y   z

    private float[] playerVertices = {

            -3f, -1f, 0f,       0, 0,
            -2.5f, -1f, 0f,     0, 1,

            -3f, -0.5f, 0f,     1, 0,
            -2.5f, -0.5f, 0f,   1, 1,
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

    private float[]gamePadVertices = {
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


    //======================================== fields ==========================================//
    String GAME_LOG = " Game ";
    playerController playerController;
    //==========================================================================================//


    public Game() {
    }

    public void prepareModelsForLevel(){
        Thread prepareModelsThread;
        prepareModelsThread = new Thread(new Runnable() {
            @Override
            public void run() {

                //init Игрока с физикой и контроллер
                dynamicObject player = new dynamicObject(playerVertices, true);
                Log.i(GAME_LOG, "Players load");

                playerController = new playerController(player);
                Log.i(GAME_LOG, "Player controller load");

                //enemies
//                AI enemy  = new AI(enemyVertices);
//                Log.i(GAME_LOG, "AI load");

                //init объектов
                staticObject platform1 = new staticObject(platform1Vertices);
                staticObject platform2 = new staticObject(platform2Vertices);
                staticObject platform3 = new staticObject(platform3Vertices);
                staticObject platform4 = new staticObject(platform4Vertices);
                staticObject platform5 = new staticObject(platform5Vertices);
                staticObject platform6 = new staticObject(platform6Vertices);
                staticObject platform7 = new staticObject(platform7Vertices);
                Log.i(GAME_LOG, "Platforms load");

                //Грузим корды динамических объектов
                render.prepareDynamicModels(player.physic.getObjVertices());
//                enemy.createModel();

                //Грузим корды стастических объектов
//                render.prepareGamePad(gamePadVertices);
                render.preparePlatform(platform1);
                render.preparePlatform(platform2);
                render.preparePlatform(platform3);
                render.preparePlatform(platform4);
                render.preparePlatform(platform5);
                render.preparePlatform(platform6);
                render.preparePlatform(platform7);
            }
        });
        prepareModelsThread.start();
    }

    public void getTouchEvent(MotionEvent event) {
        float sector = screenWidth/3;
        float cord = event.getX();

        if(cord < sector){playerController.walkLeft();}
        if((cord > sector) && (cord < sector*2)){playerController.jump();}
        if((cord > sector*2) && (cord < sector*3)){playerController.walkRight();}

    }
}
