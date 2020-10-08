package com.example.openglpractice;


import static com.example.openglpractice.Game.staticObjectPool;

public class alternativePhysicForObject implements Runnable {

    //fields
    //todo create one-projection array
    static volatile byte[][] level;
    private dynamicObject linkDynamicObject;
    private float DOWN_MAX= -3f;
    private float UP_MAX = 3f;
    private float LEFT_MAX = -3f;
    private float RIGHT_MAX = 3f;


    //flags
    boolean haveChanges = false;

    alternativePhysicForObject(float[] vertices, dynamicObject dynamicObject){
       //todo load level from src

        level = new byte[100][1000];
        linkDynamicObject = dynamicObject;
        createLevel();
        setStartingPosition(vertices);
    }

    //todo remove from here this method
    private void createLevel() {
        float [] floatBufferForStaticObject;
        for(int i = 0; i < staticObjectPool.length-1; i++) {
            floatBufferForStaticObject = staticObjectPool[i].getVertices();

        }
    }


    @Override
    public void run() {
        if(haveChanges){sendToRender();}
    }

    private void sendToRender() {

    }

    private void setStartingPosition(float[] vertices) {

    }

}
