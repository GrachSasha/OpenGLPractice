package com.example.openglpractice;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static com.example.openglpractice.Game.staticObjectCounter;
import static com.example.openglpractice.Game.staticObjectPool;
import static com.example.openglpractice.Menu.render;

public class staticObject implements RenderCommandsForStaticObjects{

    //===fields===//
        private float[] objVertices;
        public String TEXTURE_NAME;
        FloatBuffer objectBuffer;
    //===fields===//

    staticObject(float[] vertices,String textureName){
        objVertices = vertices;
        TEXTURE_NAME = textureName;
        push(this);
        prepareCoordinatesAndConvert(objVertices);
    }

    public static staticObject[] getStaticObjectPool() {
        return staticObjectPool;
    }

    public float[] getVertices(){
        return objVertices;
    }

    public String getName() {
        return TEXTURE_NAME;
    }

    //=========================================================================

    @Override
    public void drawStaticObject(int texture) {
        render.bindData(objectBuffer);
        render.setTexture(objectBuffer, texture, false);
        render.setMatrixForStaticObject();
        render.bindMatrixForLevel();
        render.drawArraysForStaticObject(GL_TRIANGLE_STRIP,0,4);
    }
    @Override
    public void prepareCoordinatesAndConvert(float[] gObject) {
        objectBuffer=  ByteBuffer
                .allocateDirect(gObject.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        objectBuffer.put(gObject).position(0);
    }

    public void push(staticObject staticObject){
        if (staticObjectCounter == getStaticObjectPool().length - 1) {

            throw new ArrayIndexOutOfBoundsException("staticObjectPool full");
        }
        getStaticObjectPool()[staticObjectCounter++] = staticObject;
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
