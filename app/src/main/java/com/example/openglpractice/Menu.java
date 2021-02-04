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
    float[] backgroundVertices = { -5f, -5f, 0f, 0, 0,
                                 5f, -5f, 0f, 0, 1,
                                  -5f, 5f, 0f, 1, 0,
                                    5f, 5f,  0f, 1, 1 };

    float[] newGameButtonVertices = {-3f, -1f, 1f, 0, 0,
                                         -2.5f, -1f, 1f, 0, 1,
                                            -3f, -0.5f, 1f, 1, 0,
                                                -2.5f, -0.5f, 1f, 1, 1};

    float menuObjectVertices[][] = new float[50][];
    private FloatBuffer menuBuffer;
    //todo сделать буффер для объектов, рисуется одинаково так один и тот же буффер

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        menuObjectVertices[1] = backgroundVertices;
        menuObjectVertices[2] = newGameButtonVertices;
        render.drawSelector = 2;
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        setContentView(glSurfaceView);
        glSurfaceView.setRenderer(render);

    }

    @Override
    public void drawStaticObject(int texture) {
            prepareCoordinatesAndConvert(backgroundVertices);
            render.bindData(menuBuffer);
            render.setTexture(menuBuffer, texture, false);
            render.setMatrixForMenu();
            render.bindMatrixForMenu();
            render.drawArraysForStaticObject(GL_TRIANGLE_STRIP, 0, 4);
    }

    public void drawStaticObject(int texture, float [] vertices) {
            prepareCoordinatesAndConvert(vertices);
            render.bindData(menuBuffer);
            render.setTexture(menuBuffer, texture, false);
            render.setMatrixForMenu();
            render.bindMatrixForMenu();
            render.drawArraysForStaticObject(GL_TRIANGLE_STRIP, 0, 4);
    }

    @Override
    public void prepareCoordinatesAndConvert(float[] gObject) {
        menuBuffer = ByteBuffer
                .allocateDirect(backgroundVertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        menuBuffer.put(backgroundVertices).position(0);

    }

    public void drawMenu(int... textures){
        for(int i =0; i < textures.length; i++){
            drawStaticObject(textures[i], menuObjectVertices[i]);
        }
    }
}

