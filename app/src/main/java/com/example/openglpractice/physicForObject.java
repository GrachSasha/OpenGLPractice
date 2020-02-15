package com.example.openglpractice;

import static com.example.openglpractice.MainActivity.render;

class physicForObject implements Runnable {

    //init?
    static final int MAXOBJECTS = 10;
    public static physicForObject objectPhysicPool[] = new physicForObject[MAXOBJECTS];
    static int objectCounter = 0;
    private Thread physicThread;
    private Thread gravitation;


    //fields
    volatile boolean move = false;
    volatile boolean jump = false;
    volatile boolean falling = false;
    float[] objVertices;
    gameObject gObj;

    public physicForObject(float[] vert, gameObject gameObject) {
        objVertices = vert;
        gObj = gameObject;
        addToPool(this);

        physicThread = new Thread(this);
        physicThread.start();

    }


    private void addToPool(physicForObject objPhysic) {
        if(objectCounter < MAXOBJECTS ){
            objectPhysicPool[objectCounter] = objPhysic;
            objectCounter++;
        }

    }

    public float[] getObjVertices(){return objVertices;}

    public void doStep(){
        move = true;
    }

    public void doJump(){
        jump = true;
    }

    public void anotherJump(){

    }

    //=====================================================================

    private void walk(){
        objVertices[0] += 0.1f;
        objVertices[3] += 0.1f;
        objVertices[6] += 0.1f;
        render.prepareDynamicModels(gObj);
        //checkGround();
    }

    private void jump(float coord){
        objVertices[1] += coord;
        objVertices[4] += coord;
        objVertices[7] += coord;
        render.prepareDynamicModels(gObj);
        //checkGround();
    }


    private void gravitation(){
        objVertices[1] += -0.1f;
        objVertices[4] += -0.1f;
        objVertices[7] += -0.1f;
        render.prepareDynamicModels(gObj);
        //checkGround();
    }

    @Override
    public void run() {
        do {
            if (move) {
                walk();
                move = false;
                checkGround();
            }

            if (jump){
                for(int i = 0; i < 100; i++){
                    jump(0.01f);
                }
                jump = false;
                falling = true;
            }
            if (falling){
                checkGround();
                gravitation();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (true);
    }

    //WARNING! HARD CODE!
    //todo НЕТ ИТЕРАТОРА НЕТ ПРОВЕРКИ НА СЕБЯ
    private void checkGround() {
        //for (int i=0; i<objectPhysicPool.length; i++){
        //WARNING! HARD CODE!
            if(!(objectPhysicPool[1].getObjVertices()[0] < objectPhysicPool[0].getObjVertices()[6]) &&
                    (objectPhysicPool[1].getObjVertices()[6] > objectPhysicPool[0].getObjVertices()[6]) &&
                    (objectPhysicPool[1].getObjVertices()[6] > objectPhysicPool[0].getObjVertices()[1])){
                falling = true;
            }
            else {falling = false;}
        //}
    }



//    @Override
//    public void run() {
//
//    }
    //in thread

}
