package com.example.openglpractice;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static com.example.openglpractice.MainActivity.render;

public class gameMenu implements RenderCommandsForStaticObjects{

    float[] menuVertices = { -3f, -3f, 0f, 0f,
                                    3f, -3f, 0f, 0f,
                                        -3f, 3f, 0f, 1f,
                                            3f, 3f, 0f, 1f };
    private FloatBuffer menuBuffer;

    gameMenu(){ }

    @Override
    public void drawStaticObject(int texture) {
        prepareCoordinatesAndConvert(menuVertices);
        render.bindData(menuBuffer);
        render.setTexture(menuBuffer, texture, false);
        render.setMatrixForMenu();
        render.createViewMatrixForMenu();
        render.drawArraysForStaticObject(GL_TRIANGLE_STRIP, 0, 4);
    }

    @Override
    public void prepareCoordinatesAndConvert(float[] gObject) {
        menuBuffer = ByteBuffer
                .allocateDirect(menuVertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        menuBuffer.put(menuVertices).position(0);
    }
}
