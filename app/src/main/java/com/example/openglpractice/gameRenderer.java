package com.example.openglpractice;
import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import utils.ShaderUtils;
import utils.TextureUtil;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_ONE_MINUS_CONSTANT_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.GL_TEXTURE0;
import static com.example.openglpractice.Game.dynamicObjectPool;
import static com.example.openglpractice.Game.staticObjectPool;

public class gameRenderer implements Renderer {

    private final static int POSITION_COUNT = 3;

    private Context context;
    private float width;
    private float height;
    byte drawSelector;
    private int enemyCounter = 0;
    private final String RENDER_LOG = "RENDER_LOG";
    private Menu menu;

    //==буфферы для отрисовки===//
    private FloatBuffer gamePad;
    private FloatBuffer[]enemies = new FloatBuffer[10];
    //==буфферы для отрисовки===//

    //===текстуры==//
    private int menuTexture;
    private int texture;
    private int texture2;
    private int null_texture;
    private int scottPilgrim;

    //todo убрать хардкод
//    Map<String, Integer> texturesMap = new HashMap<>();
    //===текстуры==//

    //===служебные переменые===//
    private int aPositionLocation;
    private int uColorLocation;
    private int uMatrixLocation;
    private int aTextureLocation;
    private int uTextureUnitLocation;
    private int framesCount = 0;

    private int programId;

    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    private float[] mMatrix = new float[16];

    private float[] mProjectionMatrixForInterface = new float[16];
    private float[] mViewMatrixForInterface = new float[16];
    private float[] mModelMatrixForInterface = new float[16];
    private float[] mMatrixForInterface = new float[16];

    private float[] mProjectionMatrixForMenu = new float[16];
    private float[] mViewMatrixForMenu = new float[16];
    private float[] mModelMatrixForMenu = new float[16];
    private float[] mMatrixForMenu = new float[16];
    //===служебные переменые===//

    //===камера==//
    // точка положения камеры
    float eyeY = 0;
    float eyeZ = 3;

    // точка направления камеры
    float centerY = 0;
    float centerZ = 0;
    float centerX = 0;

    // up-вектор
    float upX = 0;
    float upY = 1;
    float upZ = 0;
    //===камера==//

    //===проекции===//
    private float leftForLevel = -3;
    private float rightForLevel  = 3;
    private float bottomForLevel  = -3;
    private float topForLevel  = 3;

    private float nearForLevel  = 2;
    private float farForLevel  = 12;
    private float ratioForLevel  = 1;

    private float leftForInterface = -5;
    private float rightForInterface  = 5;
    private float bottomForInterface  = -5;
    private float topForInterface  = 5;

    private float nearForInterface  = 2;
    private float farForInterface  = 12;
    private float ratioForInterface  = 1;

    private float leftForMenu = -5;
    private float rightForMenu  = 5;
    private float bottomForMenu  = -5;
    private float topForMenu  = 5;

    private float nearForMenu  = 2;
    private float farForMenu  = 6;
    private float ratioForMenu  = 1;

    private float xMatrixForInterface = 2.25f;
    private float yMatrixForInterface = -0.8f;
    //===проекции===//

    public gameRenderer(Context context, byte drawSelector, int screenWidth, int screenHeight) {
        this.context = context; this.drawSelector = drawSelector;
        menu = new Menu();
        width = screenWidth;
        height = screenHeight;

        calculateValuesForProjections();
    }

    private void calculateValuesForProjections() {
        if (width > height) {
            ratioForLevel = width / height;
            leftForLevel *= ratioForLevel;
            rightForLevel *= ratioForLevel;
        } else {
            ratioForLevel =  height / width;
            bottomForLevel *= ratioForLevel;
            topForLevel *= ratioForLevel;
        }
        if (width > height) {
            ratioForInterface =  width / height;
            leftForInterface *= ratioForInterface;
            rightForInterface *= ratioForInterface;
        } else {
            ratioForInterface = height / width;
            bottomForInterface *= ratioForInterface;
            topForInterface *= ratioForInterface;
        }

        if (width > height) {
            xMatrixForInterface *= width / height;
            yMatrixForInterface *= width / height;
        }
    }

    private void createAndUseProgramm() {

        //берет шейдер файлы
        int vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader);

        //создает ид рендера
        programId = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);
        glUseProgram(programId);
    }

    private void getLocations() {
        // примитивы
        aPositionLocation = glGetAttribLocation(programId, "a_Position");

        // цвет
        uColorLocation = glGetUniformLocation(programId, "u_Color");

        // матрица
        uMatrixLocation = glGetUniformLocation(programId, "u_Matrix");

        //текстура
        aTextureLocation = glGetAttribLocation(programId, "a_Texture");
        uTextureUnitLocation = glGetUniformLocation(programId, "u_TextureUnit");

        //загружаем тестуру
        texture = TextureUtil.loadTexture(context, R.drawable.box);
        texture2 = TextureUtil.loadTexture(context, R.drawable.child_go);
        menuTexture = TextureUtil.loadTexture(context, R.drawable.menu);
        null_texture = TextureUtil.loadTexture(context, R.drawable.null_texture);
        scottPilgrim = TextureUtil.loadTexture(context, R.drawable.scott);
//        texture2 = TextureUtil.loadTexture(context, R.drawable.robo);

    }

    private void turnOnBlend() {
        //Works without fails
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_CONSTANT_ALPHA);
    }

    //вызывается при создании/пересоздании surface
    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {

        //чистит экран
        glClearColor(0, 0, 0, 0f);

        //разрешает проекции
        //Почему-то с этим параметром тестуры не работают как надо
//        glEnable(GL_DEPTH_TEST);

        //todo создать нормальный инит!
        createViewMatrixForInterface();
        createAndUseProgramm();
        getLocations();
        turnOnBlend();

    }

    //вызывается при изменении размера surface
    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        //full screen
        glViewport(0, 0, width, height);
        glClearColor(0, 0, 0, 0f);
        this.width = width;
        this.height = height;

        Log.i(RENDER_LOG, "width" + width);
        Log.i(RENDER_LOG, "height" + height);
        //создает проекцию
        createProjectionMatrix(width, height);
        createProjectionMatrixForInterface(width, height);
        //createProjectionMatrixForMenu(width, height);

        //биндит матрицу
        bindMatrix();
        bindMatrixForInterface();
        //bindMatrixForMenu();
    }

    @Override
    public void onDrawFrame(GL10 arg0) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        drawSelector();
    }

    private void drawSelector(){
        if(drawSelector == 1){
            drawLevel();
        } else {
            glClearColor(0, 0, 0, 0f);
            glClear(GL_COLOR_BUFFER_BIT);
            drawMenu();
//            menu.drawMenu(null_texture);
        }
    }

    private void drawLevel(){

        //Камера для игрока
        setCameraOnPlayer(dynamicObjectPool[0].getEye());

        //Гейм - пад
        if(gamePad != null) {
            drawGamePad();

        }
        //Игрок
        dynamicObjectPool[0].drawDynamicObject(scottPilgrim);

        for(int i=0; i < staticObjectPool.length; i++){
            if(staticObjectPool[i] != null) {
                staticObjectPool[i].drawStaticObject(texture);
            }
        }

    }

    private void drawMenu(){

//        menu.drawMenu(null_texture);
        float[] menuVertices = { -5f, -5f, 0f, 0, 0,
                                    5f, -5f, 0f, 0, 1,
                                      -5f, 5f, 0f, 1, 0,
                                           5f, 5f,  0f, 1, 1 };

        FloatBuffer menuE = ByteBuffer
                .allocateDirect(menuVertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        menuE.put(menuVertices).position(0);

        bindData(menuE);
        setTexture(menuE, null_texture, false);
        setMatrixForStaticObject();
        bindMatrix();
        drawArraysForStaticObject(GL_TRIANGLE_STRIP,0,4);
    }

    private void drawEnemies(FloatBuffer floatBuffer, int textureId) {
        bindData(floatBuffer);

        setTexture(floatBuffer, textureId, false);
        Matrix.setIdentityM(mModelMatrix, 0);
        bindMatrix();

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }

    private void drawGamePad() {
        bindData(gamePad);
        glDisable(GL_TEXTURE0);
//        setTexture(gamePad,0,false);
        //TODO скорее всего ошибка в матрице, поменять на mModelMatrix
        Matrix.setIdentityM(mModelMatrixForInterface, 0);
        bindMatrixForInterface();

        glUniform4f(uColorLocation, 0.0f, 1.0f, 1.0f, 1.5f);
        glDrawArrays(GL_TRIANGLES, 0, 3);

    }

    public void bindData(FloatBuffer floatBuffer) {
        if(floatBuffer != null) {
            floatBuffer.position(0);

            // из какого массива брать данные и по каким правилам
            glVertexAttribPointer(aPositionLocation, POSITION_COUNT, GL_FLOAT,
                    false, 20, floatBuffer);
            glEnableVertexAttribArray(aPositionLocation);
        }
    }

    public void setTexture(FloatBuffer floatBuffer, int textureId, boolean limpidity) {
        if(floatBuffer != null) {
            // координаты текстур
            // параметр отвечает за точку отсчета в массиве
            floatBuffer.position(3);

            //параметры 2 - количество занчений которые нужно взять, 5 - количество байт до следующей точки
            glVertexAttribPointer(aTextureLocation, 2, GL_FLOAT,
                    false, 20, floatBuffer);
            glEnableVertexAttribArray(aTextureLocation);

            // помещаем текстуру в target 2D юнита 0
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, textureId);

            // юнит текстуры
            glUniform1i(uTextureUnitLocation, 0);
        }
    }

    private void setCameraOnPlayer(float eyeX) {
        Log.i(RENDER_LOG, "setCameraOnPlayer eyeX" + eyeX);
        Log.i(RENDER_LOG, "setCameraOnPlayer eyeY" + eyeY);
        Log.i(RENDER_LOG, "setCameraOnPlayer eyeY" + eyeY);
        Log.i(RENDER_LOG, "setCameraOnPlayer eyeZ" + eyeZ);
        Log.i(RENDER_LOG, "setCameraOnPlayer centerY" + centerY);
        Log.i(RENDER_LOG, "setCameraOnPlayer centerZ" + centerZ);
        Log.i(RENDER_LOG, "setCameraOnPlayer upX" + upX);
        Log.i(RENDER_LOG, "setCameraOnPlayer upY" + upY);
        Log.i(RENDER_LOG, "setCameraOnPlayer upZ" + upZ);
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, eyeX, centerY, centerZ, upX, upY, upZ);
    }

    private void createViewMatrixForInterface() {
        float eyeX = 0;
        Matrix.setLookAtM(mViewMatrixForInterface, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    public void createViewMatrixForMenu() {
        float eyeX = 0;
        Matrix.setLookAtM(mViewMatrixForMenu, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    private void createProjectionMatrix(float width, float height) {
    Log.i(RENDER_LOG, "createProjectionMatrix width: " + width);
    Log.i(RENDER_LOG, "createProjectionMatrix height: " + height);
    Log.i(RENDER_LOG, "createProjectionMatrix ratioForLevel : " + ratioForLevel );
    Log.i(RENDER_LOG, "createProjectionMatrix leftForLevel : " + leftForLevel);
    Log.i(RENDER_LOG, "createProjectionMatrix rightForLevel : " + rightForLevel);
    Log.i(RENDER_LOG, "createProjectionMatrix ratioForLevel : " + ratioForLevel);
    Log.i(RENDER_LOG, "createProjectionMatrix bottomForLevel : " + bottomForLevel);
    Log.i(RENDER_LOG, "createProjectionMatrix topForLevel : " + topForLevel);
        Matrix.orthoM(mProjectionMatrix, 0, leftForLevel, rightForLevel, bottomForLevel, topForLevel, nearForLevel, farForLevel);
    }

    private void createProjectionMatrixForInterface(int width, int height) {
        Matrix.orthoM(mProjectionMatrixForInterface, 0, leftForInterface, rightForInterface, bottomForInterface, topForInterface, nearForInterface, farForInterface);
    }

    private void createProjectionMatrixForMenu(float width, float height) {

        if (width > height) {
            ratioForMenu = width / height;
            leftForMenu *= ratioForMenu;
            rightForMenu *= ratioForMenu;
        } else {
            ratioForMenu = height / width;
            bottomForMenu *= ratioForMenu;
            topForMenu *= ratioForMenu;
        }

        Matrix.orthoM(mProjectionMatrixForMenu, 0, leftForMenu, rightForMenu, bottomForMenu, topForMenu, nearForMenu, farForMenu);

    }

    private void bindMatrixForInterface() {

        Matrix.translateM(mModelMatrixForInterface, 0, xMatrixForInterface, yMatrixForInterface, 1);
        Matrix.scaleM(mModelMatrixForInterface, 0,1.25f, 1.25f, 0);
        Matrix.multiplyMM(mMatrixForInterface, 0, mViewMatrixForInterface, 0, mModelMatrixForInterface, 0);
        Matrix.multiplyMM(mMatrixForInterface, 0, mProjectionMatrixForInterface, 0, mMatrixForInterface, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrixForInterface, 0);

    }

    private void bindMatrixForMenu() {
        Matrix.multiplyMM(mMatrixForMenu, 0, mViewMatrixForMenu, 0, mModelMatrixForMenu, 0);
        Matrix.multiplyMM(mMatrixForMenu, 0, mProjectionMatrixForMenu, 0, mMatrixForMenu, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrixForMenu, 0);
    }

    public void bindMatrix() {
        Matrix.multiplyMM(mMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMatrix, 0, mProjectionMatrix, 0, mMatrix, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);
    }

    public void prepareDynamicModelsForEnemy(dynamicObject gObject) {
        //todo create seacrh
            enemies[enemyCounter] = ByteBuffer
                    .allocateDirect(gObject.physic.getObjVertices().length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            enemies[enemyCounter].put(gObject.physic.getObjVertices()).position(0);
            enemyCounter++;
    }

    public void changeDynamicModelsForEnemy(dynamicObject gObject) {
        //todo create seacrh
            enemies[0] = ByteBuffer
                    .allocateDirect(gObject.physic.getObjVertices().length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            enemies[0].put(gObject.physic.getObjVertices()).position(0);
    }

    public void prepareGamePad(float[] vertices){
        gamePad = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        gamePad.put(vertices).position(0);
    }


    public void setMatrixForDynamicObject() {
        Matrix.setIdentityM(mModelMatrix, 0);
    }
    public void setMatrixForStaticObject() {
        Matrix.setIdentityM(mModelMatrix, 0);
    }
    public void setMatrixForMenu() { Matrix.setIdentityM(mModelMatrixForMenu, 0);}

    public void drawArraysForDynamicObject(int glTriangleStrip, int position, int count) {
        glDrawArrays(glTriangleStrip, position, count);
    }
    public void drawArraysForStaticObject(int glTriangleStrip, int position, int count) {
        glDrawArrays(glTriangleStrip, position, count);
    }

}