package com.example.openglpractice;
import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDeleteTextures;
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
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.GL_TEXTURE0;

public class OpenGLRenderer implements Renderer {

    private final static int POSITION_COUNT = 3;

    private Context context;
    private float width;
    private float height;
    byte drawSelector;
    private float[] menuVertices;
    private int objCounter =0;
    private int enemyCounter = 0;

    //==буфферы для отрисовки===//
    private FloatBuffer dynamicObjects;
    private FloatBuffer gamePad;
    private FloatBuffer menu;
    private FloatBuffer[]platforms = new FloatBuffer[10];
    private FloatBuffer[]enemies = new FloatBuffer[10];
    //==буфферы для отрисовки===//

    //===текстуры==//
    private int menuTexture;
    private int texture;
    private int texture2;
    private int null_texture;
    //todo убрать хардкод
    private int[] textures = new int[10];
    private volatile byte textureCount = 0;
    //===текстуры==//

    //===служебные переменые===//
    private int aPositionLocation;
    private int uColorLocation;
    private int uMatrixLocation;
    private int aTextureLocation;
    private int uTextureUnitLocation;
    private int framesCount = 0;

    private int programId;
    private float dynamicObjectCordX;
    private float dynamicObjectCordY;

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
    float leftForLevel = -3;
    float rightForLevel  = 3;
    float bottomForLevel  = -3;
    float topForLevel  = 3;

    float nearForLevel  = 2;
    float farForLevel  = 12;
    float ratioForLevel  = 1;

    float leftForInterface = -5;
    float rightForInterface  = 5;
    float bottomForInterface  = -5;
    float topForInterface  = 5;

    float nearForInterface  = 2;
    float farForInterface  = 12;
    float ratioForInterface  = 1;

    float leftForMenu = -5;
    float rightForMenu  = 5;
    float bottomForMenu  = -5;
    float topForMenu  = 5;

    float nearForMenu  = 2;
    float farForMenu  = 6;
    float ratioForMenu  = 1;
    //===проекции===//

    public OpenGLRenderer(Context context, byte drawSelector) {
        this.context = context; this.drawSelector = drawSelector;
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
//        texture2 = TextureUtil.loadTexture(context, R.drawable.robo);
        //todo создать нормальный инит!
        createViewMatrixForInterface();
    }

    //вызывается при создании/пересоздании surface
    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {

        //чистит экран
        glClearColor(32f, 178f, 170f, 1f);

        //разрешает проекции
        glEnable(GL_DEPTH_TEST);

        createAndUseProgramm();
        getLocations();

    }

    //вызывается при изменении размера surface
    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        //full screen
        glViewport(0, 0, width, height);
        this.width = width;
        this.height = height;

        //создает проекцию
        createProjectionMatrix(width, height);
        createProjectionMatrixForInterface(width, height);
        createProjectionMatrixForMenu(width, height);

        //биндит матрицу
        bindMatrix();
        bindMatrixForInterface();
        bindMatrixForMenu();
    }

    @Override
    public void onDrawFrame(GL10 arg0) {
        drawSelector();
    }

    private void drawSelector(){
        if(drawSelector == 1){
            drawLevel();
        } else {drawMenu();}
    }

    private void drawLevel(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        //Камера для игрока
        createViewMatrix();

        //Гейм - пад
        if(gamePad != null) {
            drawGamePad();
        }

        //Игрок
        drawPlayer(dynamicObjects, texture2);

        //Вражины
        for(int i=0; i < enemies.length; i++){
            if(enemies[i] != null) {
                drawEnemies(enemies[i], texture);
            }
        }

        //Платформа
        //Берем переменные шейдера, передаем массив данных для текущих объектов
        for(int i=0; i < platforms.length; i++){
            if(platforms[i] != null) {
                drawPlatform(platforms[i], texture);
            }
        }

        //Камера для интерфейса
        //Работает без обработки в кадом кадре
//        createViewMatrixForInterface();

//        bindMatrix();
//        bindMatrixForInterface();

//        Log.i("RENDER LOG", Integer.toString(framesCount));
//        framesCount++;
    }

    private void drawMenu(){
        //todo растянуть меню на фулл скрин
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        createViewMatrixForMenu();

//        menuVertices = new float[]{ -3f, -3f, 0f, 0f, 0f,
//                                    3f, -3f, 0f, 0f, 1f,
//                                    -3f, 3f, 0f, 1f, 0f,
//                                    3f, 3f, 0f, 1f, 1f,};

        menuVertices = new float[]{ -3f, -3f, 0f, 0f, 0f,
                                      3f, -3f, 0f, 0f, 1f,
                                        -3f, 3f, 0f, 1f, 0f,
                                           3f, 3f, 0f, 1f, 1f,};
        menu = ByteBuffer
                .allocateDirect(menuVertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        menu.put(menuVertices).position(0);

        bindData(menu);
        setTexture(menu, menuTexture);
        Matrix.setIdentityM(mModelMatrixForMenu, 0);
        bindMatrixForMenu();

        glUniform4f(uColorLocation, 1.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }


    private void drawPlayer(FloatBuffer floatBuffer, int textureId) {
        bindData(floatBuffer);
        setTexture(floatBuffer, textureId);
        Matrix.setIdentityM(mModelMatrix, 0);
        bindMatrix();

//        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }

    private void drawPlatform(FloatBuffer floatBuffer, int textureId){
        bindData(floatBuffer);
        setTexture(floatBuffer, textureId);
        Matrix.setIdentityM(mModelMatrix, 0);
        bindMatrix();

        glUniform4f(uColorLocation, 1.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }

    private void drawEnemies(FloatBuffer floatBuffer, int textureId) {
        bindData(floatBuffer);
        setTexture(floatBuffer, textureId);
        Matrix.setIdentityM(mModelMatrix, 0);
        bindMatrix();

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }

    private void drawGamePad() {
        bindData(gamePad);
        setTexture(gamePad, null_texture);
        Matrix.setIdentityM(mModelMatrixForInterface, 0);
        bindMatrixForInterface();

        glUniform4f(uColorLocation, 0.0f, 1.0f, 1.0f, 1.5f);
        glDrawArrays(GL_TRIANGLES, 0, 4);

        glDrawArrays(GL_TRIANGLES, 15, 4);

        glDrawArrays(GL_TRIANGLES, 31, 4);

    }

    private void bindData(FloatBuffer floatBuffer) {

        floatBuffer.position(0);

        // из какого массива брать данные и по каким правилам
        glVertexAttribPointer(aPositionLocation, POSITION_COUNT, GL_FLOAT,
                false, 20, floatBuffer);
        glEnableVertexAttribArray(aPositionLocation);

    }

    private void setTexture(FloatBuffer floatBuffer, int textureId) {

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

    private void createViewMatrix() {
        float eyeXForPlayer = dynamicObjectCordX;
        float centerXForPlayer = dynamicObjectCordX;

        Matrix.setLookAtM(mViewMatrix, 0, eyeXForPlayer, eyeY, eyeZ, centerXForPlayer, centerY, centerZ, upX, upY, upZ);
    }

    private void createViewMatrixForInterface() {
        float eyeX = 0;
        Matrix.setLookAtM(mViewMatrixForInterface, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    private void createViewMatrixForMenu() {
        float eyeX = 0;
        Matrix.setLookAtM(mViewMatrixForMenu, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    private void createProjectionMatrix(int width, int height) {

        if (width > height) {
            ratioForLevel = (float) width / height;
            leftForLevel *= ratioForLevel;
            rightForLevel *= ratioForLevel;
        } else {
            ratioForLevel = (float) height / width;
            bottomForLevel *= ratioForLevel;
            topForLevel *= ratioForLevel;
        }

        Matrix.orthoM(mProjectionMatrix, 0, leftForLevel, rightForLevel, bottomForLevel, topForLevel, nearForLevel, farForLevel);
    }

    private void createProjectionMatrixForInterface(int width, int height) {

        if (width > height) {
            ratioForInterface = (float) width / height;
            leftForInterface *= ratioForInterface;
            rightForInterface *= ratioForInterface;
        } else {
            ratioForInterface = (float) height / width;
            bottomForInterface *= ratioForInterface;
            topForInterface *= ratioForInterface;
        }

        Matrix.orthoM(mProjectionMatrixForInterface, 0, leftForInterface, rightForInterface, bottomForInterface, topForInterface, nearForInterface, farForInterface);

    }

    private void createProjectionMatrixForMenu(int width, int height) {

        if (width > height) {
            ratioForMenu = (float) width / height;
            leftForMenu *= ratioForMenu;
            rightForMenu *= ratioForMenu;
        } else {
            ratioForMenu = (float) height / width;
            bottomForMenu *= ratioForMenu;
            topForMenu *= ratioForMenu;
        }

        Matrix.orthoM(mProjectionMatrixForMenu, 0, leftForMenu, rightForMenu, bottomForMenu, topForMenu, nearForMenu, farForMenu);

    }

    private void bindMatrixForInterface() {
        //Matrix.scaleM(mModelMatrixForInterface, 0, 2,2,2);
        float x = 2.25f, y = -0.8f;
        if (width > height) {
            x *= width / height;
            y *= width / height;
        }
        Matrix.translateM(mModelMatrixForInterface, 0, x, y, 1);
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

    private void bindMatrix() {
        Matrix.multiplyMM(mMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMatrix, 0, mProjectionMatrix, 0, mMatrix, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);
    }

    public void prepareDynamicModels(float[] gObject) {
        dynamicObjects =  ByteBuffer
                .allocateDirect(gObject.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        dynamicObjects.put(gObject).position(0);

        dynamicObjectCordX = gObject[0];
        dynamicObjectCordY = gObject[1];
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

    public void preparePlatform(staticObject staticObj){
        platforms[objCounter] = ByteBuffer
                .allocateDirect(staticObj.getVertices().length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        platforms[objCounter].put(staticObj.getVertices()).position(0);
        objCounter++;

    }


}