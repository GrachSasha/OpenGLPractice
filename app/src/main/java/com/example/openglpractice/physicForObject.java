package com.example.openglpractice;

import static com.example.openglpractice.MainActivity.render;
import static com.example.openglpractice.dynamicObject.dynamicObjectPool;

class physicForObject implements Runnable {

    //constants
    public static final byte X1 = 0;
    public static final byte Y1 = 1;
    public static final byte X2 = 2;
    public static final byte Y2 = 3;
    //init?
    static final int MAXOBJECTS = 10;
    private static final int MIN_GROUND = -3;

    //vectors
    private volatile float[] UP_VECTOR = new float[3];
    private volatile float[] DOWN_VECTOR= new float[3];
    private volatile float[] RIGHT_VECTOR= new float[3];
    private volatile float[] LEFT_VECTOR= new float[3];


    //flags
    private volatile boolean move = false;
    private volatile boolean jump = false;
    private volatile boolean falling = false;
    private volatile boolean isPossibleWalk = true;

    //fields

    private Thread physicThread;
    private float[] objVertices;
    private dynamicObject linkDynamicObject;
    private float[] layerMap = {3f, 2.75f, 2.50f, 2.25f, 2f, 1.75f, 1.5f, 1.25f, 1f, 0.75f, 0.5f, 0.25f, 0f,
                                -0.25f, -0.5f, -0.75f, -1f, -1.25f, -1.5f, -1.75f, -2f, -2.25f, -2.5f, -2.75f, -3f};
    float centerDotX;
    float centerDotY;


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

    private void walkRight(){
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
            if (move) {
                wallCheck();
                if(isPossibleWalk) {
                    walkRight();
                    move = false;
                    gravityCheck();
                } else {
                    move = false;
                }
            }
            if (jump){
//                if(!falling) {
                    for (int i = 0; i < 3; i++) {
                        jump(0.3f);
                    }
//                }
                jump = false;
                gravityCheck();
            }
            if (falling){
                fall();
                gravityCheck();
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (true);
    }

    private void gravityCheck() {
        centerDotY = (dynamicObjectPool[0].physic.getObjVertices()[1] + dynamicObjectPool[0].physic.getObjVertices()[7])/2;
        centerDotX = dynamicObjectPool[0].physic.getObjVertices()[6];
        DOWN_VECTOR[X1] = centerDotX;
        DOWN_VECTOR[Y1] = centerDotY + (-0.15f);

        for (int i = 0; i < staticObject.getStaticObjectPool().length - 1; i++) {

            //проверка на пустые объекты
            if (staticObject.getStaticObjectPool()[i] == null) {
                continue;
            }

                //если над объектом
                if ((dynamicObjectPool[0].physic.getObjVertices()[6] >= staticObject.getStaticObjectPool()[i].getVertices()[6]) &&
                        (dynamicObjectPool[0].physic.getObjVertices()[6] <= staticObject.getStaticObjectPool()[i].getVertices()[9])) {

                    //если выше
                    if (dynamicObjectPool[0].physic.getObjVertices()[1] > staticObject.getStaticObjectPool()[i].getVertices()[7]){
                            if(DOWN_VECTOR[Y1] < staticObject.getStaticObjectPool()[i].getVertices()[7]) {
                                falling = false;
                                break;
                            } else {
                            falling = true;
                            continue;}
                    } else {
                        //если над объетом, но не выше не падаем
                        //falling = false;
                        //break;
                        continue;
                    }

                } else {
                    //если ни над одним объектом падаем
                    falling = true;
                    //break;
                    continue;
                 }

        }
    }

    //todo NEED CREATE DIRECTION
    private void wallCheck() {
        for (int i = 0; i < staticObject.getStaticObjectPool().length - 1; i++) {

            //проверка на пустые объекты
            if (staticObject.getStaticObjectPool()[i] == null) { continue; }

            //проверка на высоту
                if (dynamicObjectPool[0].physic.getObjVertices()[4] > staticObject.getStaticObjectPool()[i].getVertices()[7]) {
                    isPossibleWalk = true;
                    break;
                } else {

                    //если игрок стоит перед объектом, то он не двигается дальше в него
                    if (Math.abs(dynamicObjectPool[0].physic.getObjVertices()[3] - staticObject.getStaticObjectPool()[i].getVertices()[0]) <= 0.1) {
                        isPossibleWalk = false;
                        break;
                    } else {
                        isPossibleWalk = true;
                        //break;
                    }
                }
        }
    }

    private boolean isNearToObject(staticObject staticObj, float pogreshnost){
        float centerDotStaticObjectX = (staticObj.getVertices()[0] + staticObj.getVertices()[3]) / 2;
        float centerDotStaticObjectY = (staticObj.getVertices()[1] + staticObj.getVertices()[7]) / 2;
        centerDotY = (dynamicObjectPool[0].physic.getObjVertices()[1] + dynamicObjectPool[0].physic.getObjVertices()[7])/2;
        centerDotX = dynamicObjectPool[0].physic.getObjVertices()[6];

        //maybe can broke!
        if((Math.abs(centerDotStaticObjectX - centerDotX) < pogreshnost)&&(Math.abs(centerDotStaticObjectY - centerDotY) < pogreshnost)){
            return true;
        }
        return false;
    }

    private void downCheck(){

        centerDotY = (dynamicObjectPool[0].physic.getObjVertices()[1] + dynamicObjectPool[0].physic.getObjVertices()[7])/2;
        centerDotX = dynamicObjectPool[0].physic.getObjVertices()[6];
        DOWN_VECTOR[X1] = centerDotX;
        DOWN_VECTOR[Y1] = centerDotY + (-0.25f);

        for(int i = 0; i < staticObject.getStaticObjectPool().length - 1; i++){
            //проверка на пустые объекты
            if (staticObject.getStaticObjectPool()[i] == null) { continue; }

            //если над объектом
            if ((DOWN_VECTOR[X1] >= staticObject.getStaticObjectPool()[i].getTop()[X1]) &&
                    ((DOWN_VECTOR[X1] <= staticObject.getStaticObjectPool()[i].getTop()[X2]))) {

                //если над объектом, проверяем высоту и падаем
                if (DOWN_VECTOR[Y1] > staticObject.getStaticObjectPool()[i].getTop()[Y1]) {
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

    private void quickSortBy(){
        staticObject staticObjectBuffer;

    }

}
