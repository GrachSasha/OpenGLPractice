package com.example.openglpractice;

import static com.example.openglpractice.MainActivity.render;
import static com.example.openglpractice.dynamicObject.dynamicObjectPool;

class physicForObject implements Runnable {


    //init?
    static final int MAXOBJECTS = 10;
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
    private dynamicObject linkDynamicObject;


    public physicForObject(float[] vert, dynamicObject dynamicObject) {
        objVertices = vert;
        linkDynamicObject = dynamicObject;

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

    private void walkRight(){
        dynamicObjectPool[0].physic.getObjVertices()[0] += 0.10f;
        dynamicObjectPool[0].physic.getObjVertices()[3] += 0.10f;
        dynamicObjectPool[0].physic.getObjVertices()[6] += 0.10f;
        render.prepareDynamicModels(linkDynamicObject);

    }
    private void walkLeft(){
        dynamicObjectPool[0].physic.getObjVertices()[0] += (-0.10f);
        dynamicObjectPool[0].physic.getObjVertices()[3] += (-0.10f);
        dynamicObjectPool[0].physic.getObjVertices()[6] += (-0.10f);
        render.prepareDynamicModels(linkDynamicObject);

    }

    private void jump(float coord){
        if(dynamicObjectPool[0].physic.getObjVertices()[1] <= MAX_GROUND) {
            dynamicObjectPool[0].physic.getObjVertices()[1] += coord;
            dynamicObjectPool[0].physic.getObjVertices()[4] += coord;
            dynamicObjectPool[0].physic.getObjVertices()[7] += coord;
            render.prepareDynamicModels(linkDynamicObject);
        }
    }


    private void fall(){
        if(dynamicObjectPool[0].physic.getObjVertices()[1] >= MIN_GROUND) {
            dynamicObjectPool[0].physic.getObjVertices()[1] += -0.05f;
            dynamicObjectPool[0].physic.getObjVertices()[4] += -0.05f;
            dynamicObjectPool[0].physic.getObjVertices()[7] += -0.05f;
            render.prepareDynamicModels(linkDynamicObject);
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

            if ((dynamicObjectPool[0].physic.getObjVertices()[4]  >=
                    staticObject.getStaticObjectPool()[i].getVertices()[1]) &&
                    (dynamicObjectPool[0].physic.getObjVertices()[4] <=
                            staticObject.getStaticObjectPool()[i].getVertices()[7])) {

                if (Math.abs(dynamicObjectPool[0].physic.getObjVertices()[3] -
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

            if ((dynamicObjectPool[0].physic.getObjVertices()[1]  >=
                    staticObject.getStaticObjectPool()[i].getVertices()[4]) &&
                    (dynamicObjectPool[0].physic.getObjVertices()[1] <=
                            staticObject.getStaticObjectPool()[i].getVertices()[7])) {

                if (Math.abs(dynamicObjectPool[0].physic.getObjVertices()[0] -
                        staticObject.getStaticObjectPool()[i].getVertices()[3]) < 0.05f)
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
            if ((dynamicObjectPool[0].physic.getObjVertices()[6] >= staticObject.getStaticObjectPool()[i].getVertices()[0]) &&
                    (dynamicObjectPool[0].physic.getObjVertices()[6] <= staticObject.getStaticObjectPool()[i].getVertices()[3])) {

                //проверка по у
                if (Math.abs(dynamicObjectPool[0].physic.getObjVertices()[7] -
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
            if ((dynamicObjectPool[0].physic.getObjVertices()[0] >= staticObject.getStaticObjectPool()[i].getVertices()[6]) &&
                    (dynamicObjectPool[0].physic.getObjVertices()[0] <= staticObject.getStaticObjectPool()[i].getVertices()[9]) ||
                        (dynamicObjectPool[0].physic.getObjVertices()[3] >= staticObject.getStaticObjectPool()[i].getVertices()[6]) &&
                            (dynamicObjectPool[0].physic.getObjVertices()[3] <= staticObject.getStaticObjectPool()[i].getVertices()[9])) {

                //если выше
                if (Math.abs(dynamicObjectPool[0].physic.getObjVertices()[1] -
                        staticObject.getStaticObjectPool()[i].getVertices()[7]) < 0.025f){
                    falling = false;
                    return false;
                }
            }
        }
        falling = true;
        return true;
    }


    //    private void upCheck(){
////        UP_VECTOR[X1] = dynamicObjectPool[0].physic.getObjVertices()[6];
////        UP_VECTOR[Y1] = dynamicObjectPool[0].physic.getObjVertices()[7] + 0.10f;
//
//            for(int i = 0; i < staticObject.getStaticObjectPool().length-1; i++){
//
//                //проверка на пустые объекты
//                if (staticObject.getStaticObjectPool()[i] == null) {
//                    continue;
//                }
//
//                //проверка по х
//                if((dynamicObjectPool[0].physic.getObjVertices()[6] >= staticObject.getStaticObjectPool()[i].getVertices()[0]) &&
//                        (dynamicObjectPool[0].physic.getObjVertices()[6] <= staticObject.getStaticObjectPool()[i].getVertices()[3])){
//
//                    //проверка по у
//                    if((dynamicObjectPool[0].physic.getObjVertices()[7] + 0.10f > staticObject.getStaticObjectPool()[i].getVertices()[1]) &&
//                            (dynamicObjectPool[0].physic.getObjVertices()[7] < staticObject.getStaticObjectPool()[i].getVertices()[1])){
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
//        float centerDotY = (dynamicObjectPool[0].physic.getObjVertices()[1] + dynamicObjectPool[0].physic.getObjVertices()[7])/2;
//        float centerDotX = dynamicObjectPool[0].physic.getObjVertices()[6];
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
//                if ((dynamicObjectPool[0].physic.getObjVertices()[6] >= staticObject.getStaticObjectPool()[i].getVertices()[6]) &&
//                        (dynamicObjectPool[0].physic.getObjVertices()[6] <= staticObject.getStaticObjectPool()[i].getVertices()[9])) {
//
//                    //если выше
//                    if (dynamicObjectPool[0].physic.getObjVertices()[1] > staticObject.getStaticObjectPool()[i].getVertices()[7]){
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

