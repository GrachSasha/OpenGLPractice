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
            -0.5f, -0.25f, 0.25f,
            0.5f, -0.25f, 0.25f,
            0, 0.25f, 0.25f,

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

    gameObject gObject = new gameObject(vertices);
    gameObject staticObjects = new gameObject(staticObj);
    OpenGLRenderer render;
    float step= 0;
//===================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!supportES2()) {
            Toast.makeText(this, "OpenGL ES 2.0 is not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        render = new OpenGLRenderer(this);
        render.prepareDynamicModels(gObject);
        render.prepareStaticModels(staticObjects);
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
    public boolean onTouchEvent(MotionEvent e) {

        gObject.vertices = new float[]{-0.5f +step, -0.25f+step, 0.25f+step,
                                        0.5f+step, -0.25f+step, 0.25f+step,
                                        0+step, 0.25f+step, 0.25f+step,};
        step = step+0.1f;
        render.prepareDynamicModels(gObject);
        return true;
    }

    private boolean supportES2() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }

}