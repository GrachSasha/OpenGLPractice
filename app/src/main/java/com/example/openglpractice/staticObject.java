package com.example.openglpractice;

public class staticObject{


    static staticObject[] staticObjectPool = new staticObject[10];
    static int staticObjectCounter = -1;

    private float[] objVertices;

    staticObject(float[] vertices){
        objVertices = vertices;
        push(this);
    }

    public static staticObject[] getStaticObjectPool() {
        return staticObjectPool;
    }

    synchronized public static void setStaticObjectPool(staticObject[] staticObjectPool) {
        staticObject.staticObjectPool = staticObjectPool;
    }

    public float[] getVertices(){
        return objVertices;
    }

    //=========================================================================

    public void push(staticObject staticObject){
        if (staticObjectCounter == getStaticObjectPool().length - 1) {

            throw new ArrayIndexOutOfBoundsException("staticObjectPool full");
        }
        getStaticObjectPool()[++staticObjectCounter] = staticObject;
    }

    public void delete(staticObject staticObject){
        if(staticObjectCounter == -1){
            throw new ArrayIndexOutOfBoundsException("staticObjectPool empty");
        }
        getStaticObjectPool()[staticObjectCounter] = null;
        staticObjectCounter--;
    }
                                //x          //y            //x          //y
     public float[] getTop(){
         return new float[]{objVertices[6], objVertices[7], objVertices[9], objVertices[10]};
     }
     public float[] getDown(){
         return new float[]{objVertices[0], objVertices[1], objVertices[3], objVertices[4]};
     }
     public float[] getRight(){
         return new float[]{objVertices[3], objVertices[4], objVertices[9], objVertices[10]};
     }
     public float[] getLeft(){
         return new float[]{objVertices[0], objVertices[1], objVertices[6], objVertices[7]};
     }
}
