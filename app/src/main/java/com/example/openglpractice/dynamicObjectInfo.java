package com.example.openglpractice;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static com.example.openglpractice.Menu.render;

public class dynamicObjectInfo implements RenderCommandsForDynamicObjects {


    private static final String DYNAMIC_OBJECT_LOG = "DynamicObject";
    //===fields===//
        public boolean isRealPlayer = false;
        private boolean isInventoryOpen = false;

        dynamicObject physic;
        public String TEXTURE_NAME;
        FloatBuffer objectBuffer;
        int objectTexture;
        private float eyeX;
        private float eyeY;
        private int objectId;
    //===fields===//

    //todo убрать передачу Context`а и возможно загрузку модельки
    public dynamicObjectInfo(boolean pl, String textureName){
        isRealPlayer = pl;
        objectId = this.hashCode();
        TEXTURE_NAME = textureName;
    }

    @Override
    public void drawDynamicObject(int texture) {
        render.bindData(objectBuffer);
        render.setTexture(objectBuffer, texture, false);
        //работает и без этого render.setMatrixForDynamicObject();
        render.setMatrixForDynamicObject();
        render.bindMatrixForLevel();
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
