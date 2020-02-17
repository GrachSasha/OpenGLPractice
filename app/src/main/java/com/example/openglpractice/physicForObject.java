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
        objVertices[0] += 0.35f;
        objVertices[3] += 0.35f;
        objVertices[6] += 0.35f;
        render.prepareDynamicModels(gObj);
        gravityCheck();
    }

    private void jump(float coord){
        objVertices[1] += coord;
        objVertices[4] += coord;
        objVertices[7] += coord;
        render.prepareDynamicModels(gObj);
        gravityCheck();
    }


    private void fall(){
        if(objVertices[1]>= -3f) {
            objVertices[1] += -0.07f;
            objVertices[4] += -0.07f;
            objVertices[7] += -0.07f;
            render.prepareDynamicModels(gObj);
        } else {falling = false;}
        //render.prepareDynamicModels(gObj);
        //gravityCheck();
    }

    @Override
    public void run() {
        do {
            if (move) {
                walk();
                move = false;
            }

            if (jump){
                if(!falling) {
                    for (int i = 0; i < 10; i++) {
                        jump(0.07f);
                    }
                }
                jump = false;
                gravityCheck();
            }
            if (falling){
                gravityCheck();
                fall();
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
    private void gravityCheck() {
        //for (int i=0; i<objectPhysicPool.length; i++) {
        for(int i = 1; i < 3; i++) {
            if (isOnElement(i)) {
                if ((objectPhysicPool[i].getObjVertices()[10] < objectPhysicPool[0].getObjVertices()[1])) {
                    falling = true;
                } else {
                    falling = false;
                }
            } else {
                falling = true;
            }
        }
        //}
    }

    private boolean isOnElement(int i){
        if((objectPhysicPool[i].getObjVertices()[6] <= objectPhysicPool[0].getObjVertices()[6]) &&
        (objectPhysicPool[i].getObjVertices()[9] >= objectPhysicPool[0].getObjVertices()[6])){return true;}
        else{return false;}
    }

}
