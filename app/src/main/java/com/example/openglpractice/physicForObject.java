package com.example.openglpractice;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;
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

    //fields
    private Thread physicThread;
    private float[] objVertices;
    private dynamicObject linkDynamicObject;
    private Context context;

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
        gravityCheck();
    }

    private void jump(float coord){
        dynamicObjectPool[0].physic.getObjVertices()[1] += coord;
        dynamicObjectPool[0].physic.getObjVertices()[4] += coord;
        dynamicObjectPool[0].physic.getObjVertices()[7] += coord;
        render.prepareDynamicModels(linkDynamicObject);
        gravityCheck();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void fall(){
        if(dynamicObjectPool[0].physic.getObjVertices()[1]>= -3f) {
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
                walk();
                move = false;
            }

            if (jump){
                if(!falling) {
                    for (int i = 0; i < 3; i++) {
                        jump(0.3f);
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
//        for(int i = 0; i < 3; i++){
//            if(dynamicObjectPool[i].physic != this){
//                if (isOnElement(i)) {
//                    if ((dynamicObjectPool[i].physic.getObjVertices()[10] < this.getObjVertices()[1])) {
//                        falling = true;
//                        break;
//                    } else {
//                        falling = false;
//                        break;
//                    }
//                } else {
//                    falling = true;
//                    break;
//                }
//            }
//        }
        for(int i = 0; i < staticObject.staticObjectPool.length-1; i++){
            if(staticObject.staticObjectPool[i] == null){
                //
                break;
            }
            if((dynamicObjectPool[0].physic.getObjVertices()[6] >= staticObject.staticObjectPool[i].getVertices()[6]) &&
                (dynamicObjectPool[0].physic.getObjVertices()[6] <= staticObject.staticObjectPool[i].getVertices()[9]))
            {
                //если над объектом, проверяем высоту и падаем
                if(dynamicObjectPool[0].physic.getObjVertices()[1] >= staticObject.staticObjectPool[i].getVertices()[7]){
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

//    private boolean isOnElement(int i){
//        if((dynamicObjectPool[i].physic.getObjVertices()[6] <= this.getObjVertices()[6]) &&
//        (dynamicObjectPool[i].physic.getObjVertices()[9] >= this.getObjVertices()[6])){return true;}
//        else{return false;}
//    }

}
