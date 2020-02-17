package com.example.openglpractice;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

public class MainActivity extends Activity {
//=================================================
    private GLSurfaceView glSurfaceView;

    float[] vertices = {
            // треугольник
          // x    y   z
            -3f, -2f, 0.25f,//2
            -2.5f, -2f, 0.25f,//5
            -2.75f, -1.75f, 0.25f,//8

    };

    float[] platform = {

            -3f, -3f, 0,//2
            -2f, -3f, 0,//5
            -3f, -2.75f,0,//8
            -2f, -2.75f, 0//11
    };

    float[] platform2 = {
            //x   y
            0f, -3f, 0,//2
            1f, -3f, 0,//5
            0f, -2.75f,0,//8
            1f, -2.75f, 0//11
    };

    float[]gamePadVertices = {
            1.0f, -0.3f, 0f,
            1.0f, -0.5f, 0f,

            1.0f, -0.3f, 0f,
            0.9f, -0.35f, 0,

            1.0f, -0.3f, 0f,
            1.1f, -0.35f, 0,
    };

    float[] staticObj = {
            // ось X
            -3f, 0, 0,
            3f, 0, 0,

            // ось Y
            0, -3f, 0,
            0, 3f, 0,

            // ось Z
            0, 0, -3f,
            0, 0, 3f
    };


    //init render
    static OpenGLRenderer render;

    //init Игрока с физикой и контроллер
    gameObject triangle = new gameObject(vertices);
    gameController controller = new gameController(triangle);

    //init объектов
    gameObject platformObj = new gameObject(platform);
    gameObject platformObj2 = new gameObject(platform2);
    //gameObject staticObjects = new gameObject(staticObj);
    //gameObject gamePad = new gameObject(gamePadVertices);

    float step= 0f;
//===================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!supportES2()) {
            Toast.makeText(this, "OpenGL ES 2.0 is not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //Инициализация рендера
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        render = new OpenGLRenderer(this);

        //Грузим корды объектов
        render.prepareDynamicModels(triangle);
        //render.prepareStaticModels(staticObjects);
        //render.prepareGamePad(gamePad);

        render.preparePlatform(platformObj);
        render.preparePlatform(platformObj2);

        //Рендер на весь экран
        glSurfaceView.setRenderer(render);
        setContentView(glSurfaceView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        step = 0.1f;
//        controller.walk();
        if(event.getX()<100) {
            controller.jump();
        } else {controller.walk();}
        return true;
    }

    private boolean supportES2() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }

}