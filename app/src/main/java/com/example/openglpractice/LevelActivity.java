package com.example.openglpractice;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import utils.ResourceLoader;

import static com.example.openglpractice.MenuActivity.render;

public class LevelActivity extends Activity{
    //=================================================
    private GLSurfaceView glSurfaceView;

    //init Level Control
    Handler controlHandler;
    Level level;
    static int screenWidth;
    static int screenHeight;
    final String TEST_PO = "TEST_PO";

//===================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (!supportES2()) {
            Toast.makeText(this, "OpenGL ES 2.0 is not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

//========TEST PO===========
//        assetManager =  this.getAssets();
//        ResourceLoader.getResourceLoader(this).loadLevel("level1");
//========TEST PO===========

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenWidth = displaymetrics.widthPixels;
        screenHeight = displaymetrics.heightPixels;
        Toast.makeText(this, "WIDTH = " + screenWidth + "HEIGHT = " + screenHeight, Toast.LENGTH_LONG).show();

        //Контроллер
        controlHandler = new Handler();

        //Инициализация рендера
        render.drawSelector = 1;
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);

        //Рендер на весь экран
        glSurfaceView.setRenderer(render);
        setContentView(glSurfaceView);

        Button b = new Button(this);
        b.setText("Hello World");
        this.addContentView(b,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        List<String> resources=  new ResourceLoader(this).loadResource("level1");
        level = new Level(resources);
//        level = new Level();
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

        float sector = screenWidth/3;
        float cord = event.getX();
        if((cord > sector) && (cord < sector*2)) {
            startActivity(new Intent(this, MenuActivity.class));
        }
        level.getTouchEvent(event);
        return true;
    }

    private boolean supportES2() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }

//    private void loadResource(String lvl) {
//        try {
//            String[] files = assetManager.list("levels");
//            Log.i(TEST_PO, files[0]);
//            String level = "levels/ + " + lvl + " + .xml";
//
//            // получаем фабрику
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            // включаем поддержку namespace (по умолчанию выключена)
//            factory.setNamespaceAware(true);
//            // создаем парсер
//            XmlPullParser xpp = factory.newPullParser();
//
//            xpp.setInput(assetManager.open(level),"utf-8");
//
//            //=============================================================
//            String tmp="";
//            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
//                switch (xpp.getEventType()) {
//                    // начало документа
//                    case XmlPullParser.START_DOCUMENT:
//                        Log.d(TEST_PO, "START_DOCUMENT");
//                        break;
//                    // начало тэга
//                    case XmlPullParser.START_TAG:
//                        Log.d(TEST_PO, "START_TAG: name = " + xpp.getName()
//                                + ", depth = " + xpp.getDepth() + ", attrCount = "
//                                + xpp.getAttributeCount());
//                        tmp = "";
//                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
//                            tmp = tmp + xpp.getAttributeName(i) + " = "
//                                    + xpp.getAttributeValue(i) + ", ";
//                        }
//                        if (!TextUtils.isEmpty(tmp))
//                            Log.d(TEST_PO, "Attributes: " + tmp);
//                        break;
//                    // конец тэга
//                    case XmlPullParser.END_TAG:
//                        Log.d(TEST_PO, "END_TAG: name = " + xpp.getName());
//                        break;
//                    // содержимое тэга
//                    case XmlPullParser.TEXT:
//                        Log.d(TEST_PO, "text = " + xpp.getText());
//                        break;
//
//                    default:
//                        break;
//                }
//                // следующий элемент
//                xpp.next();
//            }
//            Log.d(TEST_PO, "END_DOCUMENT");
//            //=============================================================
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        }
//    }

}