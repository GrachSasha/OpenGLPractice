package com.example.openglpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static com.example.openglpractice.MainActivity.render;

public class Menu extends AppCompatActivity implements RenderCommandsForStaticObjects {

    GLSurfaceView glSurfaceView;
    float[] backgroundVertices = { -5f, -5f, 0f, 0, 0,
                                 5f, -5f, 0f, 0, 1,
                                  -5f, 5f, 0f, 1, 0,
                                    5f, 5f,  0f, 1, 1 };

    float[] newGameButtonVertices = {-3f, -1f, 0f, 0, 0,
                                         -2.5f, -1f, 0f, 0, 1,
                                            -3f, -0.5f, 0f, 1, 0,
                                                -2.5f, -0.5f, 0f, 1, 1};

//    private Map<String, float[]>  menuObjects = new HashMap();

    private FloatBuffer menuBuffer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        menuObjects.put("background",backgroundVertices);
//        menuObjects.put("newGameButton", newGameButtonVertices);
        render.drawSelector = 2;
        super.onCreate(savedInstanceState);
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        setContentView(glSurfaceView);
        glSurfaceView.setRenderer(render);

    }

    @Override
    public void drawStaticObject(int texture) {
//        for(int i =0; i < menuObjects.size(); i++) {
            prepareCoordinatesAndConvert(backgroundVertices);
            render.bindData(menuBuffer);
            render.setTexture(menuBuffer, texture, false);
            render.setMatrixForMenu();
            render.bindMatrixForMenu();
            render.drawArraysForStaticObject(GL_TRIANGLE_STRIP, 0, 4);


//        }
    }

    @Override
    public void prepareCoordinatesAndConvert(float[] gObject) {
        menuBuffer = ByteBuffer
                .allocateDirect(backgroundVertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        menuBuffer.put(backgroundVertices).position(0);

    }
}

