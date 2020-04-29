package com.example.openglpractice;

import static com.example.openglpractice.MainActivity.render;
import static com.example.openglpractice.dynamicObject.dynamicObjectPool;

class physicForObject implements Runnable {

    //init?
    static final int MAXOBJECTS = 10;
    private static final int MIN_GROUND = -3;


    //flags
    private volatile boolean move = false;
    private volatile boolean jump = false;
    private volatile boolean falling = false;
    private volatile boolean isPossibleWalk = true;

    //fields
    private Thread physicThread;
    private float[] objVertices;
    private dynamicObject linkDynamicObject;
    float currentX;
    float currentY;

    public physicForObject(float[] vert, dynamicObject dynamicObject) {
        objVertices = vert;
        linkDynamicObject = dynamicObject;

        physicThread = new Thread(this);
        physicThread.start();
    }

    public float[] getObjVertices(){return objVertices;}

    public void doStep(){move = true;}

    public void doJump(){jump = true;}

    //=====================================================================

    private void walk(){
        dynamicObjectPool[0].physic.getObjVertices()[0] += 0.25f;
        dynamicObjectPool[0].physic.getObjVertices()[3] += 0.25f;
        dynamicObjectPool[0].physic.getObjVertices()[6] += 0.25f;
        render.prepareDynamicModels(linkDynamicObject);
//        wallCheck();
//        gravityCheck();
    }

    private void jump(float coord){
        dynamicObjectPool[0].physic.getObjVertices()[1] += coord;
        dynamicObjectPool[0].physic.getObjVertices()[4] += coord;
        dynamicObjectPool[0].physic.getObjVertices()[7] += coord;
        render.prepareDynamicModels(linkDynamicObject);
//        gravityCheck();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void fall(){
        if(dynamicObjectPool[0].physic.getObjVertices()[1] >= MIN_GROUND) {
            dynamicObjectPool[0].physic.getObjVertices()[1] += -0.05f;
            dynamicObjectPool[0].physic.getObjVertices()[4] += -0.05f;
            dynamicObjectPool[0].physic.getObjVertices()[7] += -0.05f;
            render.prepareDynamicModels(linkDynamicObject);
        } else {falling = false;}
        //render.prepareDynamicModels(linkDynamicObject);
        //gravityCheck();
    }

    @Override
    public void run() {
        do {
            //
            if (move) {
                wallCheck();
                if(isPossibleWalk) {
                    walk();
                    move = false;
                    gravityCheck();
                } else {
                    move = false;
                }
            }

            //
            if (jump){
//                if(!falling) {
                    for (int i = 0; i < 3; i++) {
                        jump(0.3f);
                    }
//                }
                jump = false;
                gravityCheck();
            }

            //
            if (falling){
                fall();
                gravityCheck();
            }

            //
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (true);
    }

    private void gravityCheck() {


        for (int i = 0; i < staticObject.staticObjectPool.length - 1; i++) {
            //проверка на пустые объекты
            if (staticObject.staticObjectPool[i] == null) {
                continue;
            }

            //если над объектом
            if ((dynamicObjectPool[0].physic.getObjVertices()[6] >= staticObject.staticObjectPool[i].getVertices()[6]) &&
                    (dynamicObjectPool[0].physic.getObjVertices()[6] <= staticObject.staticObjectPool[i].getVertices()[9])) {

                //если над объектом, проверяем высоту и падаем
                if (dynamicObjectPool[0].physic.getObjVertices()[1] > staticObject.staticObjectPool[i].getVertices()[7]) {
                    falling = true;
                    break;

                } else {
                    //если над объетом, но не выше не падаем
                    falling = false;
                    break;
                }

            } else {
                //если ни над одним объектом падаем
                falling = true;
                //break;
            }
        }
    }

    //todo NEED CREATE DIRECTION
    private void wallCheck() {
        for (int i = 0; i < staticObject.staticObjectPool.length - 1; i++) {
            //проверка на пустые объекты
            if (staticObject.staticObjectPool[i] == null) {
                continue;
            }

            //проверка на высоту
            if (dynamicObjectPool[0].physic.getObjVertices()[4] > staticObject.staticObjectPool[i].getVertices()[7]) {
                isPossibleWalk = true;
                break;
            } else {

                //если игрок стоит перед объектом, то он не двигается дальше в него
                if (Math.abs(dynamicObjectPool[0].physic.getObjVertices()[3] - staticObject.staticObjectPool[i].getVertices()[0]) <= 0.1) {
                    isPossibleWalk = false;
                    break;
                } else {
                    isPossibleWalk = true;
                    //break;
                }
            }
        }
    }

}
