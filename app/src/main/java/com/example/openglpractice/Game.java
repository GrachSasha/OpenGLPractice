package com.example.openglpractice;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;

import static com.example.openglpractice.MainActivity.render;
import static com.example.openglpractice.MainActivity.screenWidth;

class Game {
    // треугольник
    // x    y   z

    private float[] playerVertices = {

            -3f, -1f, 0f,       0, 0,
            -2.5f, -1f, 0f,     0, 0.17f,

            -3f, -0.5f, 0f,     1, 0,
            -2.5f, -0.5f, 0f,   1, 0.17f,
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
            2.0f, -1.5f, 0.1f, 0,0,
            3.0f, -1.5f, 0.1f, 0,1,
            2.5f, -1.0f, 0.1f, 1,1,

            2.0f, -2.5f, 0.1f,  0,0,
            3.0f, -2.5f, 0.1f,  0,1,
            2.5f, -3.0f, 0.1f,  1,1,

            3.0f, -1.5f, 0.1f,  0,0,
            3.0f, -2.5f, 0.1f,  0,1,
            3.5f, -2.0f, 0.1f,  1,1

    };


    //======================================== fields ==========================================//
    static String GAME_LOG = " Game ";
    Activity activity;
    playerController playerController;
    ResourceLoader loader;
    //==========================================================================================//


    public Game(MainActivity mainActivity) {
        //todo убрать загрузку уровня из конструктора
        activity = mainActivity;
        prepareModelsForLevel1();
    }

    public void prepareModelsForLevel1(){

                //TEST
//                loader.openLibrary();
                //TEST

                //init Игрока с физикой и контроллер

                render.drawSelector = 1;
                dynamicObject player = new dynamicObject(playerVertices, true, "child_go");

                Log.i(GAME_LOG, "Players load");

                playerController = new playerController(player);
                Log.i(GAME_LOG, "Player controller load");

                //enemies
//                AI enemy  = new AI(enemyVertices);
//                Log.i(GAME_LOG, "AI load");

                //init объектов
                staticObject platform1 = new staticObject(platform1Vertices,"box");
                staticObject platform2 = new staticObject(platform2Vertices,"box");
                staticObject platform3 = new staticObject(platform3Vertices,"box");
                staticObject platform4 = new staticObject(platform4Vertices,"box");
                staticObject platform5 = new staticObject(platform5Vertices,"box");
                staticObject platform6 = new staticObject(platform6Vertices,"box");
                staticObject platform7 = new staticObject(platform7Vertices,"box");
                Log.i(GAME_LOG, "Platforms load");

                //Грузим корды динамических объектов
                render.prepareDynamicModels(player.physic.getObjVertices());
//                enemy.createModel();

                //Грузим корды стастических объектов
                render.preparePlatform(platform1);
                render.preparePlatform(platform2);
                render.preparePlatform(platform3);
                render.preparePlatform(platform4);
                render.preparePlatform(platform5);
                render.preparePlatform(platform6);
                render.preparePlatform(platform7);
                render.prepareGamePad(gamePadVertices);
    }

    public void getTouchEvent(MotionEvent event) {
        //todo привести в порядок вызов меню
        render.drawSelector = 1;
//        prepareModelsForLevel1();
        float sector = screenWidth/3;
        float cord = event.getX();
        if(cord < sector){playerController.walkLeft();}
        if((cord > sector) && (cord < sector*2)){playerController.jump();}
        if((cord > sector*2) && (cord < sector*3)){playerController.walkRight();}

    }

    public void createMenu() {
        render.drawSelector = 0;
    }
}
