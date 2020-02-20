package com.example.openglpractice;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import java.io.Console;

public class MainActivity extends Activity {
//static Logger logger = Logger.getLogger();
    //=================================================
    private GLSurfaceView glSurfaceView;
             // треугольник
            // x    y   z
    float[] vertices = {

            -3f, -2f, 0.25f,
            -2.5f, -2f, 0.25f,
            -2.75f, -1.75f, 0.25f,

    };

    float[] platform1Vertices = {

            -3f, -3f, 0,
            -2f, -3f, 0,
            -3f, -2.75f,0,
            -2f, -2.75f, 0
    };

    float[] platform2Vertices = {

            0f, -3f, 0,
            1f, -3f, 0,
            0f, -2.75f,0,
            1f, -2.75f, 0
    };

    float[] platform3Vertices = {

            2f, -3f, 0,
            3f, -3f, 0,
            2f, -2.75f,0,
            3f, -2.75f, 0
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
    dynamicObject triangle = new dynamicObject(vertices);
    gameController controller = new gameController(triangle);

    //init объектов
//    dynamicObject platformObj = new dynamicObject(platform1Vertices);
//    dynamicObject platformObj2 = new dynamicObject(platform2Vertices);
//    dynamicObject platformObj3 = new dynamicObject(platform3Vertices);
    staticObject platform1 = new staticObject(platform1Vertices);
    staticObject platform2 = new staticObject(platform2Vertices);
    staticObject platform3 = new staticObject(platform3Vertices);



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

        render.preparePlatform(platform1);
        render.preparePlatform(platform2);
        render.preparePlatform(platform3);

        //Рендер на весь экран
        glSurfaceView.setRenderer(render);
        setContentView(glSurfaceView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
        Runtime.getRuntime().freeMemory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
        Runtime.getRuntime().freeMemory();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

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