package com.example.openglpractice;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import java.util.List;

import static com.example.openglpractice.LevelActivity.screenHeight;
import static com.example.openglpractice.MenuActivity.render;
import static com.example.openglpractice.LevelActivity.screenWidth;

//Thread для теста
class Level implements Runnable {
    // треугольник
    // x    y   z

//    private float[] playerVertices = {
//                                //x     y
//            -3f, -1f, 1f,       0.125f, 1,
//            -2.5f, -1f, 1f,     0, 1,
//
//            -3f, -0.5f, 1f,     0.125f, 0.5f,
//            -2.5f, -0.5f, 1f,   0, 0.5f,
//
//    };
    private float[] playerVertices = {
                                //x     y
            -3f, -1f, 1f,       0, 0,
            -2.5f, -1f, 1f,     0, 1,

            -3f, -0.5f, 1f,     1, 0,
            -2.5f, -0.5f, 1f,   1, 1,

    };

    private float[] enemyVertices = {

            -3f, -2f, 1f,       0.125f, 1,
            -2.5f, -2f, 1f,     0, 1,

            -3f, -1.5f, 1f,     0.125f, 0.5f,
            -2.5f, -1.5f, 1f,   0, 0.5f,

    };

    private float[]gamePadVertices = {
            2.0f, -1.5f, 0.1f, 0,0,
            3.0f, -1.5f, 0.1f, 0,1,
            2.5f, -1.0f, 0.1f, 1,1,

//            2.0f, 3.0f, 0.1f, 0,0,
//            3.0f, 3.0f, 0.1f, 0,1,
//            2.0f, 3.5f, 0.1f, 1,0,
//            3.0f, 3.5f, 0.1f, 1,1,

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
    private Button buttonPool[] = new Button[10];
    //===pools===//

    //===fields===//
    private Context gameContext;
    static String GAME_LOG = "GAME_LOG";
    playerController playerController;
    playerController enemyController;
    private Thread textWriterThread;
    private Thread enemyContrllerThread;
    private float platforms[][];
    //===fields===//

    public Level() {
        //todo убрать загрузку уровня из конструктора
//        createMenu();
        prepareDynamicModelsForLevel();

//        enemyContrllerThread = new Thread(this);
//        enemyContrllerThread.start();

//        textWriterThread = new Thread(this);
//        textWriterThread.start();
    }

    public Level(List<String> resources) {
        createStaticObjectsForLevel(prepareResourcesForStaticObjects(resources));
        prepareDynamicModelsForLevel();
        prepareButtons();
    }

    private void prepareButtons() {

    }

    private void createStaticObjectsForLevel(float[][] verticesResources) {
        for(int i = 0; i < verticesResources.length; i ++){
            new staticObject(verticesResources[i],"box");
        }
        Log.i(GAME_LOG, "Platforms load");
    }

    public void prepareDynamicModelsForLevel(){
//        textWriter = new TextWriter();

        dynamicObject player = new dynamicObject(playerVertices, true, "child_go");
        player.prepareCoordinatesAndConvert(player.getObjVertices());
        playerController = new playerController(player);
        Log.i(GAME_LOG, "1. " + dynamicObjectPool[0].TEXTURE_NAME);

        //init объектов
        dynamicObject enemy = new dynamicObject(enemyVertices, false, "button");
        enemy.prepareCoordinatesAndConvert(enemy.getObjVertices());
        enemyController = new playerController(enemy);
        Log.i(GAME_LOG, "2. " + dynamicObjectPool[1].TEXTURE_NAME);

//        textWriter.setText("mod",0.5f, new float[]{dynamicObjectPool[0].getEyeX(), dynamicObjectPool[0].getEyeY()});
        //todo render do not call this method. Get out from render add some class for it
        render.prepareGamePad(gamePadVertices);
    }

    public void getTouchEvent(MotionEvent event) {
        //todo привести в порядок вызов меню

        Log.i(GAME_LOG,"X: " + event.getX() + "; " + "Y: " + event.getY());
        //Log.i(GAME_LOG,"Raw X: " + event.getRawX() + "; " + "Raw Y: " + event.getRawY());
        float sectorX = screenWidth/2;
        float sectorY = screenHeight/2;

        float cordX = event.getX();
        float cordY = event.getY();
//WORKS
        float sceneX = ((cordX/screenWidth)*2.0f - 1.0f)*5f;
        float sceneY = ((cordY/screenHeight)*-2.0f + 1.0f)*5f;
        Log.i(GAME_LOG,"sceneX: " + sceneX + "; " + "sceneY: " + sceneY);

        if((sceneX > gamePadVertices[0])
                && (sceneX < gamePadVertices[5])
                    &&(sceneY < gamePadVertices[11])
                        &&(sceneY > gamePadVertices[1])){
            Log.i(GAME_LOG, "Pressed");
        }

//        if((cordX < sectorX) && (cordY < sectorY)){playerController.walkLeft();
//        playerController.walkDown();}
//        if(cordX > sectorX){playerController.walkRight();}
//Не спрашивай это дерьмо с экраном андроида, пляшем от координат телефона, приходится выкручиваться так
        if((cordX > sectorX) && (cordY < sectorY)){playerController.walkRight();
        playerController.walkUp();}

        if((cordX > sectorX) && (cordY > sectorY)){playerController.walkRight();
            playerController.walkDown();}

        if((cordX < sectorX) && (cordY < sectorY)){playerController.walkLeft();
            playerController.walkUp();}

        if((cordX < sectorX) && (cordY > sectorY)){playerController.walkLeft();
            playerController.walkDown();}
    }

    public void createMenu() {
        render.drawSelector = 0;
  }


    private float[][] prepareResourcesForStaticObjects(List<String> rawResources) {
        int resSize = rawResources.size();
        platforms = new float[resSize][20];
        StringBuilder rawSing = new StringBuilder();
        Log.i(GAME_LOG, "resSize: " + resSize);

        for(int i =0; i < resSize; i++){
            int floatCounts = 0;
            String rawLine = rawResources.get(i);
            Log.i(GAME_LOG, "rawLine: " + rawLine);

            for(int j =0; j < rawLine.length(); j++){

                if((rawLine.charAt(j) != ',')){
                    rawSing.append(rawLine.charAt(j));
                    Log.i(GAME_LOG, "string build: " + rawSing);

                } else {
                    Log.i(GAME_LOG, "to array: " + rawSing);
                    platforms[i][floatCounts] = Float.valueOf(rawSing.toString());
                    rawSing.delete(0,16);
                    Log.i(GAME_LOG, "platforms[" + i + "][" + floatCounts + "] = " + platforms[i][floatCounts]);
                    floatCounts++;
                }

            }
        }
        rawSing = null;
        return platforms;
    }
    //TEST=====================================//

    @Override
    public void run() {
//        do {
//            enemyController.walkLeft();
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            Log.i(GAME_LOG, "ENEMY STEP");
//        }while (true);
    }

    //TEST=====================================//
}