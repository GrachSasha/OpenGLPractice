package com.example.openglpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import utils.ResourceLoader;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static com.example.openglpractice.MainActivity.render;

public class Menu extends AppCompatActivity implements RenderCommandsForStaticObjects {

    public static Menu menu = new Menu();
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
    public static AssetManager resourceLoaderAssetManager;

    //todo сделать буффер для объектов, рисуется одинаково так один и тот же буффер

    public Menu(){
//        loader = new ResourceLoader(this);
//        loader.loadResource("level1.xml");
//        menu.loadResources();
        render.setMenuInstance(menu);
        System.out.println("CONSTRUCTOR CALLS");
    }

//    private void loadResources() {
//        try {
//            String[] files = resourceLoaderAssetManager.list("levels");
//            Log.i(RESOURCE_LOADER, files[0]);
//            String level = "levels/ + " + lvl + " + .xml";
//
//            // получаем фабрику
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            // включаем поддержку namespace (по умолчанию выключена)
//            factory.setNamespaceAware(true);
//            // создаем парсер
//            XmlPullParser xpp = factory.newPullParser();
//
//            xpp.setInput(resourceLoaderAssetManager.open(level),"utf-8");
//
//            //=============================================================
//            String tmp="";
//            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
//                switch (xpp.getEventType()) {
//                    // начало документа
//                    case XmlPullParser.START_DOCUMENT:
//                        Log.d(RESOURCE_LOADER, "START_DOCUMENT");
//                        break;
//                    // начало тэга
//                    case XmlPullParser.START_TAG:
//                        Log.d(RESOURCE_LOADER, "START_TAG: name = " + xpp.getName()
//                                + ", depth = " + xpp.getDepth() + ", attrCount = "
//                                + xpp.getAttributeCount());
//                        tmp = "";
//                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
//                            tmp = tmp + xpp.getAttributeName(i) + " = "
//                                    + xpp.getAttributeValue(i) + ", ";
//                        }
//                        if (!TextUtils.isEmpty(tmp))
//                            Log.d(RESOURCE_LOADER, "Attributes: " + tmp);
//                        break;
//                    // конец тэга
//                    case XmlPullParser.END_TAG:
//                        Log.d(RESOURCE_LOADER, "END_TAG: name = " + xpp.getName());
//                        break;
//                    // содержимое тэга
//                    case XmlPullParser.TEXT:
//                        Log.d(RESOURCE_LOADER, "text = " + xpp.getText());
//                        break;
//
//                    default:
//                        break;
//                }
//                // следующий элемент
//                xpp.next();
//            }
//            Log.d(RESOURCE_LOADER, "END_DOCUMENT");
//            //=============================================================
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        }
//    }

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

