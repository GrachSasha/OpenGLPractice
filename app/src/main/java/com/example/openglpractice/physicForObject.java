package com.example.openglpractice;

import java.io.Console;

import static com.example.openglpractice.MainActivity.render;
import static com.example.openglpractice.gameObject.gameObjectPool;

class physicForObject implements Runnable {

    //init?
    static final int MAXOBJECTS = 10;
    private Thread physicThread;

    //fields
    private volatile boolean move = false;
    private volatile boolean jump = false;
    private volatile boolean falling = false;
    private float[] objVertices;
    private gameObject gObj;

    public physicForObject(float[] vert, gameObject gameObject) {
        objVertices = vert;
        gObj = gameObject;

        physicThread = new Thread(this);
        physicThread.start();
    }

    public float[] getObjVertices(){return objVertices;}

    public void doStep(){move = true;}

    public void doJump(){jump = true;}

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
        for(int i = 0; i < 3; i++){
            if(gameObjectPool[i].physic != this){
                if (isOnElement(i)) {
                    if ((gameObjectPool[i].physic.getObjVertices()[10] < this.getObjVertices()[1])) {
                        falling = true;
                        break;
                    } else {
                        falling = false;
                        break;
                    }
                } else {
                    falling = true;
                    break;
                }
            }
        }
    }

    private boolean isOnElement(int i){
        if((gameObjectPool[i].physic.getObjVertices()[6] <= this.getObjVertices()[6]) &&
        (gameObjectPool[i].physic.getObjVertices()[9] >= this.getObjVertices()[6])){return true;}
        else{return false;}
    }

}
