package com.example.openglpractice;

import android.util.Log;

import static com.example.openglpractice.Game.dynamicObjectPool;
import static com.example.openglpractice.MainActivity.render;

class dynamicObject extends dynamicObjectInfo implements Runnable {


    //init?
    private static final int MIN_GROUND = -3;
    private static final int MAX_GROUND = 3;

    //flags
    private volatile boolean moveLeft = false;
    private volatile boolean moveRight = false;
    private volatile boolean jump = false;
    private volatile boolean falling = false;

    //fields
    private Thread physicThread;
    private float[] objVertices;
    private int gameObjectCounter = 0;
    private final String DYNAMIC_OBJECT_LOG = "Dynamic_Object";


    public dynamicObject(float[] vertices, boolean pl, String textureName) {
        super(pl, textureName);
        addToPool(this);
        objVertices = vertices;

        physicThread = new Thread(this);
        physicThread.start();
    }

    public float[] getObjVertices(){return objVertices;}

    public void doStepLeft(){
        moveLeft = true;}

    public void doStepRight(){
        moveRight = true;}

    public void doJump(){jump = true;}

    //=====================================================================
    private void addToPool(dynamicObject dynamicObject) {
        //todo use MAX constant
        if(gameObjectCounter < 10){
            dynamicObjectPool[gameObjectCounter] = dynamicObject;
            gameObjectCounter++;
        }else {
            Log.i(DYNAMIC_OBJECT_LOG,"Max objects count!");
        }
    }

    private void walkRight(){

        //движение хитбокса
        objVertices[0] += 0.10f;
        objVertices[5] += 0.10f;
        objVertices[10] += 0.10f;
        objVertices[15] += 0.10f;

        //движение текстуры
        objVertices[3] += 0.125f;
        objVertices[8] += 0.125f;
        objVertices[13] += 0.125f;
        objVertices[18] += 0.125f;

        if(this.isRealPlayer){this.prepareCoordinatesAndConvert(objVertices);} else
            {render.changeDynamicModelsForEnemy(this);}

    }
    private void walkLeft(){

        objVertices[0] += (-0.10f);
        objVertices[5] += (-0.10f);
        objVertices[10] += (-0.10f);
        objVertices[15] += (-0.10f);

        if(this.isRealPlayer){this.prepareCoordinatesAndConvert(objVertices);} else
        {render.changeDynamicModelsForEnemy(this);}
    }

    private void jump(float coord) {
        if (objVertices[1] <= MAX_GROUND) {
            objVertices[1] += coord;
            objVertices[6] += coord;
            objVertices[11] += coord;
            objVertices[16] += coord;

            if (this.isRealPlayer) {
                this.prepareCoordinatesAndConvert(objVertices);
            } else {
                render.changeDynamicModelsForEnemy(this);
            }

        }
    }

    private void fall(){
        if(objVertices[1] >= MIN_GROUND) {
            objVertices[1] += -0.05f;
            objVertices[6] += -0.05f;
            objVertices[11] += -0.05f;
            objVertices[16] += -0.05f;

            if(this.isRealPlayer){this.prepareCoordinatesAndConvert(objVertices);} else
            {render.changeDynamicModelsForEnemy(this);}

        } else {falling = false;}
    }

    @Override
    public void run() {
        do {
            if (moveLeft) {
                if(wallCheckLeft()) {
                    walkLeft();
                    moveLeft = false;
                    downCheck();
                } else {
                    moveLeft = false;
                }
            }

            if (moveRight) {
                if(wallCheckRight()) {
                    walkRight();
                    moveRight = false;
                    downCheck();
                } else {
                    moveRight = false;
                }
            }

            if (jump){
                if(!falling) {
                    for (int i = 0; i < 10; i++) {
                        if (upCheck()) {
                            jump(0.1f);
                        } else {
                            break;
                        }
                    }
                }
                jump = false;
                downCheck();
            }

            if (falling){
                if(downCheck()) {
                    fall();
                }
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (true);
    }

    private boolean wallCheckRight() {
        //todo NEED CREATE DIRECTION

        for (int i = 0; i < staticObject.getStaticObjectPool().length - 1; i++) {

            //проверка на пустые объекты
            if (staticObject.getStaticObjectPool()[i] == null) {
                continue;
            }

            if ((objVertices[6]  >=
                    staticObject.getStaticObjectPool()[i].getVertices()[1]) &&
                    (objVertices[6] <=
                            staticObject.getStaticObjectPool()[i].getVertices()[11])) {

                if (Math.abs(objVertices[5] -
                        staticObject.getStaticObjectPool()[i].getVertices()[0]) < 0.05f)
                        {
                            return false;
                        }
            }
        }
        return true;

    }

    private boolean wallCheckLeft() {
        //todo NEED CREATE DIRECTION

        for (int i = 0; i < staticObject.getStaticObjectPool().length - 1; i++) {

            //проверка на пустые объекты
            if (staticObject.getStaticObjectPool()[i] == null) {
                continue;
            }

            if ((objVertices[1]  >=
                    staticObject.getStaticObjectPool()[i].getVertices()[6]) &&
                    (objVertices[1] <=
                            staticObject.getStaticObjectPool()[i].getVertices()[11])) {

                if (Math.abs(objVertices[0] -
                        staticObject.getStaticObjectPool()[i].getVertices()[5]) < 0.05f)
                {
                    return false;
                }
            }
        }
        return true;

    }

    private boolean upCheck() {

        for (int i = 0; i < staticObject.getStaticObjectPool().length - 1; i++) {

            //проверка на пустые объекты
            if (staticObject.getStaticObjectPool()[i] == null) {
                continue;
            }

            //проверка по х
            if ((objVertices[10] >= staticObject.getStaticObjectPool()[i].getVertices()[0]) &&
                    (objVertices[10] <= staticObject.getStaticObjectPool()[i].getVertices()[5])) {

                //проверка по у
                if (Math.abs(objVertices[11] -
                        staticObject.getStaticObjectPool()[i].getVertices()[1]) < 0.05f) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean downCheck(){
        for (int i = 0; i < staticObject.getStaticObjectPool().length - 1; i++) {

            //проверка на пустые объекты
            if (staticObject.getStaticObjectPool()[i] == null) {
                continue;
            }

            //если над объектом
            if ((objVertices[0] >= staticObject.getStaticObjectPool()[i].getVertices()[10]) &&
                    (objVertices[0] <= staticObject.getStaticObjectPool()[i].getVertices()[15]) ||
                        (objVertices[5] >= staticObject.getStaticObjectPool()[i].getVertices()[10]) &&
                            (objVertices[5] <= staticObject.getStaticObjectPool()[i].getVertices()[15])) {

                //если выше
                if (Math.abs(objVertices[1] -
                        staticObject.getStaticObjectPool()[i].getVertices()[11]) < 0.025f){
                    falling = false;
                    return false;
                }
            }
        }
        falling = true;
        return true;
    }


    //    private void upCheck(){
////        UP_VECTOR[X1] = objVertices[6];
////        UP_VECTOR[Y1] = objVertices[7] + 0.10f;
//
//            for(int i = 0; i < staticObject.getStaticObjectPool().length-1; i++){
//
//                //проверка на пустые объекты
//                if (staticObject.getStaticObjectPool()[i] == null) {
//                    continue;
//                }
//
//                //проверка по х
//                if((objVertices[6] >= staticObject.getStaticObjectPool()[i].getVertices()[0]) &&
//                        (objVertices[6] <= staticObject.getStaticObjectPool()[i].getVertices()[3])){
//
//                    //проверка по у
//                    if((objVertices[7] + 0.10f > staticObject.getStaticObjectPool()[i].getVertices()[1]) &&
//                            (objVertices[7] < staticObject.getStaticObjectPool()[i].getVertices()[1])){
//                            isPossibleJump = false;
//                            break;
//                    } else {
//                        isPossibleJump = true;
//                        continue; }
//                } else if (i == staticObject.getStaticObjectPool().length-1){
//                isPossibleJump = true;
//                break;
//                } else {continue;}
//            }
//    }

    //    private void downCheck(){
//        float centerDotY = (objVertices[1] + objVertices[7])/2;
//        float centerDotX = objVertices[6];
//        DOWN_VECTOR[Y1] = centerDotY + (-0.15f);
//
//        for (int i = 0; i < staticObject.getStaticObjectPool().length - 1; i++) {
//
//            //проверка на пустые объекты
//            if (staticObject.getStaticObjectPool()[i] == null) {
//                continue;
//            }
//
//                //если над объектом
//                if ((objVertices[6] >= staticObject.getStaticObjectPool()[i].getVertices()[6]) &&
//                        (objVertices[6] <= staticObject.getStaticObjectPool()[i].getVertices()[9])) {
//
//                    //если выше
//                    if (objVertices[1] > staticObject.getStaticObjectPool()[i].getVertices()[7]){
//                            if(DOWN_VECTOR[Y1] < staticObject.getStaticObjectPool()[i].getVertices()[7]) {
//                                falling = false;
//                                break;
//                            } else {
//                            falling = true;
//                            continue;}
//                    } else {
//                        //если над объетом, но не выше не падаем
//                        //falling = false;
//                        //break;
//                        continue;
//                    }
//
//                } else {
//                    //если ни над одним объектом падаем
//                    falling = true;
//                    //break;
//                    continue;
//                 }
//
//        }
//    }
}

