package com.example.openglpractice;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.res.AssetManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;

import utils.ResourceLoader;

import static com.example.openglpractice.Game.textWriter;

public class MainActivity extends Activity{
    //=================================================
    private GLSurfaceView glSurfaceView;

    //init Game Control
    Handler controlHandler;
    Game game;
    static int screenWidth;
    static int screenHeight;
    final String TEST_PO = "TEST_PO";
    public static AssetManager assetManager;
    static Menu menu;

    //init render
    static gameRenderer render;
    Thread renderThread;
//===================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!supportES2()) {
            Toast.makeText(this, "OpenGL ES 2.0 is not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

//        assetManager =  this.getAssets();
//========TEST PO===========
        ResourceLoader.getResourceLoader(this).loadLevel("level1");
//========TEST PO===========

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenWidth = displaymetrics.widthPixels;
        screenHeight = displaymetrics.heightPixels;
        Toast.makeText(this, "WIDTH = " + screenWidth + "HEIGHT = " + screenHeight, Toast.LENGTH_LONG).show();

        //Контроллер
        controlHandler = new Handler();

        //Инит рендера
        menu = new Menu();

        //Инициализация рендера
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        render = new gameRenderer(this, (byte) 0, screenWidth, screenHeight);

        //Рендер на весь экран
        glSurfaceView.setRenderer(render);

        setContentView(glSurfaceView);

        game = new Game();
        game.createMenu();

        renderThread = new Thread();
        renderThread.start();

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
            startActivity(new Intent(this, Menu.class));
        }
        game.getTouchEvent(event);
        return true;
    }

    private boolean supportES2() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }

    private void loadResource(String lvl) {
        try {
            String[] files = assetManager.list("levels");
            Log.i(TEST_PO, files[0]);
            String level = "levels/ + " + lvl + " + .xml";

            // получаем фабрику
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            // включаем поддержку namespace (по умолчанию выключена)
            factory.setNamespaceAware(true);
            // создаем парсер
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(assetManager.open(level),"utf-8");

            //=============================================================
            String tmp="";
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    // начало документа
                    case XmlPullParser.START_DOCUMENT:
                        Log.d(TEST_PO, "START_DOCUMENT");
                        break;
                    // начало тэга
                    case XmlPullParser.START_TAG:
                        Log.d(TEST_PO, "START_TAG: name = " + xpp.getName()
                                + ", depth = " + xpp.getDepth() + ", attrCount = "
                                + xpp.getAttributeCount());
                        tmp = "";
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            tmp = tmp + xpp.getAttributeName(i) + " = "
                                    + xpp.getAttributeValue(i) + ", ";
                        }
                        if (!TextUtils.isEmpty(tmp))
                            Log.d(TEST_PO, "Attributes: " + tmp);
                        break;
                    // конец тэга
                    case XmlPullParser.END_TAG:
                        Log.d(TEST_PO, "END_TAG: name = " + xpp.getName());
                        break;
                    // содержимое тэга
                    case XmlPullParser.TEXT:
                        Log.d(TEST_PO, "text = " + xpp.getText());
                        break;

                    default:
                        break;
                }
                // следующий элемент
                xpp.next();
            }
            Log.d(TEST_PO, "END_DOCUMENT");
            //=============================================================

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

}