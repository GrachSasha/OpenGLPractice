package com.example.openglpractice;

public class staticObject{


    static staticObject[] staticObjectPool = new staticObject[10];
    static int staticObjectCounter = -1;

    private float[] objVertices;

    staticObject(float[] vertices){
        objVertices = vertices;
        push(this);
    }

    public float[] getVertices(){
        return objVertices;
    }

    //=========================================================================

    public void push(staticObject staticObject){
        if (staticObjectCounter == staticObjectPool.length - 1) {

            throw new ArrayIndexOutOfBoundsException("staticObjectPool full");
        }
        staticObjectPool[++staticObjectCounter] = staticObject;
    }

    public void delete(staticObject staticObject){
        if(staticObjectCounter == -1){
            throw new ArrayIndexOutOfBoundsException("staticObjectPool empty");
        }
        staticObjectPool[staticObjectCounter] = null;
        staticObjectCounter--;
    }

}
