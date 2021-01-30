package com.example.openglpractice;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static com.example.openglpractice.Game.dynamicObjectPool;
import static com.example.openglpractice.Game.gameObjectCounter;
import static com.example.openglpractice.MainActivity.render;

public class dynamicObject implements RenderCommandsForDynamicObjects {


    private static final String DYNAMIC_OBJECT_LOG = "DynamicObject";
    //===fields===//
        public boolean isRealPlayer = false;
        private boolean isInventoryOpen = false;

        physicForObject physic;
        public String TEXTURE_NAME;
        FloatBuffer objectBuffer;
        int objectTexture;
        private float eyeX;
        private float eyeY;
        private int objectId;
    //===fields===//

    //todo убрать передачу Context`а и возможно загрузку модельки
    public dynamicObject(float[] vertices, boolean pl, String textureName){
        isRealPlayer = pl;
        objectId = this.hashCode();
        TEXTURE_NAME = textureName;
        physic = new physicForObject(vertices, this);
        addToPool(this);
    }

    @Override
    public void drawDynamicObject(int texture) {
        render.bindData(objectBuffer);
        render.setTexture(objectBuffer, texture, false);
        //работает и без этого render.setMatrixForDynamicObject();
        render.setMatrixForDynamicObject();
        render.bindMatrix();
        render.drawArraysForDynamicObject(GL_TRIANGLE_STRIP, 0, 4);
    }

    @Override
    public void prepareCoordinatesAndConvert(float[] gObject) {
        objectBuffer =  ByteBuffer
                .allocateDirect(gObject.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        objectBuffer.put(gObject).position(0);

        eyeX = gObject[0];
        eyeY = gObject[11];
    }

    private void addToPool(dynamicObject dynamicObject) {
        //todo use MAX constant
        if(gameObjectCounter < 10){
            dynamicObjectPool[gameObjectCounter] = dynamicObject;
            gameObjectCounter++;
        }else {
            Log.i(DYNAMIC_OBJECT_LOG,"Max objects count!");
        }
    }

    public String getName() {
        return TEXTURE_NAME;
    }

    public void openInventory() { isInventoryOpen = true;}


    public float getEyeX() {
        return eyeX;
    }

    public float getEyeY() {
        return eyeY;
    }
}
