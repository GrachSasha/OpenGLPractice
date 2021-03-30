package com.example.openglpractice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import utils.TextureUtil;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static com.example.openglpractice.LevelActivity.screenHeight;

public class MenuActivity extends AppCompatActivity implements RenderCommandsForStaticObjects {

//    public static MenuActivity menu = new MenuActivity();
    GLSurfaceView glSurfaceView;
    int screenWidth;
    private String MENU = "MENU_LOG";
    byte touchCount;
    static gameRenderer render;
    public static AssetManager assetManager;

    float[] backgroundVertices = { -5f, -5f, 0f, 0, 0,
                                 5f, -5f, 0f, 0, 1,
                                  -5f, 5f, 0f, 1, 0,
                                    5f, 5f,  0f, 1, 1 };

    float[] newGameButtonVertices = {-3f, -1f, 1f, 0, 0,
                                         -1.5f, -1f, 1f, 0, 1,
                                            -3f, 1f, 1f, 1, 0,
                                                -1.5f, 1f, 1f, 1, 1};

    float menuObjectVertices[][] = new float[50][];
    private FloatBuffer menuBuffer;

    //todo сделать буффер для объектов, рисуется одинаково так один и тот же буффер

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadResource("menu");

        menuObjectVertices[0] = backgroundVertices;
        menuObjectVertices[1] = newGameButtonVertices;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenWidth = displaymetrics.widthPixels;
        screenHeight = displaymetrics.heightPixels;
        Toast.makeText(this, "WIDTH = " + screenWidth + "HEIGHT = " + screenHeight, Toast.LENGTH_LONG).show();

        //Инициализация рендера
        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        render = new gameRenderer(this, (byte) 0, screenWidth, screenHeight);

        //Рендер на весь экран
        glSurfaceView.setRenderer(render);
        setContentView(glSurfaceView);

        render.setMenuInstance(this);
        render.drawSelector = 2;

        Button b = new Button(this);
        b.setText("Start");
        this.addContentView(b,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ImageButton imageButton = new ImageButton(this);
//        imageButton.setImageResource(R.drawable.start);

//        imageButton.setRotation(90);
//        imageButton.setX(10);
//        imageButton.setY(10);
//        imageButton.setMaxHeight(5);
//        imageButton.setMaxWidth(5);
//        this.addContentView(imageButton,
//        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

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
//            menuBuffer.reset();
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
                .allocateDirect(gObject.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        menuBuffer.put(gObject).position(0);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchCount++;
//        float touchX = event.getX();
//        float touchY = event.getY();
//
//        Log.i(MENU, "x = " + touchX);
//        Log.i(MENU, "y = " + touchY);

        if(touchCount > 2) {
            //todo дорогая операция

//
            float sector = screenWidth / 3;
            float cord = event.getX();
            if ((cord > sector) && (cord < sector * 2)) {
                startActivity(new Intent(this, LevelActivity.class));
            }
//            if(touchX > newGameButtonVertices[0] && touchX < newGameButtonVertices[5]){
//                if(touchY > newGameButtonVertices[1] && touchX < newGameButtonVertices[11]){
//                                    startActivity(new Intent(this, LevelActivity.class));
//                }
//            }
            touchCount = 0;
        }
//        level.getTouchEvent(event);
        return true;
    }


    public void drawMenu(int... textures){
        for(int i =0; i < textures.length; i++){
            drawStaticObject(textures[i], menuObjectVertices[i]);
        }
    }

    private void loadResource(String lvl) {
        assetManager = getApplicationContext().getAssets();
        try {
            String[] files = assetManager.list("levels");
            Log.i(MENU, files[1]);
            String level = "levels/" + lvl + ".xml";

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
                        Log.d(MENU, "START_DOCUMENT");
                        break;
                    // начало тэга
                    case XmlPullParser.START_TAG:
                        Log.d(MENU, "START_TAG: name = " + xpp.getName()
                                + ", depth = " + xpp.getDepth() + ", attrCount = "
                                + xpp.getAttributeCount());
                        tmp = "";
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            tmp = tmp + xpp.getAttributeName(i) + " = "
                                    + xpp.getAttributeValue(i) + ", ";
                        }
                        if (!TextUtils.isEmpty(tmp))
                            Log.d(MENU, "Attributes: " + tmp);
                        break;
                    // конец тэга
                    case XmlPullParser.END_TAG:
                        Log.d(MENU, "END_TAG: name = " + xpp.getName());
                        break;
                    // содержимое тэга
                    case XmlPullParser.TEXT:
                        Log.d(MENU, "text = " + xpp.getText());
                        break;

                    default:
                        break;
                }
                // следующий элемент
                xpp.next();
            }
            Log.d(MENU, "END_DOCUMENT");
            //=============================================================

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

}

