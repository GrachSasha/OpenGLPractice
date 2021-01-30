package com.example.openglpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static com.example.openglpractice.MainActivity.render;

public class Menu extends AppCompatActivity implements RenderCommandsForStaticObjects {

    GLSurfaceView glSurfaceView;
    float[] menuVertices = { -5f, -5f, 0f, 0, 0,
                                 5f, -5f, 0f, 0, 1,
                                  -5f, 5f, 0f, 1, 0,
                                    5f, 5f,  0f, 1, 1 };

   private FloatBuffer menuBuffer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        render.drawSelector = 2;
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        setContentView(glSurfaceView);
        glSurfaceView.setRenderer(render);

    }

    @Override
    public void drawStaticObject(int texture) {
        prepareCoordinatesAndConvert(menuVertices);
        render.bindData(menuBuffer);
        render.setTexture(menuBuffer, texture, false);
        render.setMatrixForStaticObject();
        render.bindMatrix();
        render.drawArraysForStaticObject(GL_TRIANGLE_STRIP,0,4);
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

