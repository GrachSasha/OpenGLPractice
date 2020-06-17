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
    float width;
    float height;

    private FloatBuffer dynamicObjects;
    private FloatBuffer gamePad;
    private FloatBuffer[]platforms = new FloatBuffer[10];
    private FloatBuffer[]enemies = new FloatBuffer[10];
    private int objCounter = 0;
    private int enemyCounter = 0;

    private int aPositionLocation;
    private int uColorLocation;
    private int uMatrixLocation;
    private int aTextureLocation;
    private int uTextureUnitLocation;

    private int programId;
    private float dynamicObjectCordX;
    private float dynamicObjectCordY;
    private int texture;

    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    private float[] mMatrix = new float[16];

    private float[] mProjectionMatrixForInterface = new float[16];
    private float[] mViewMatrixForInterface = new float[16];
    private float[] mModelMatrixForInterface = new float[16];
    private float[] mMatrixForInterface = new float[16];

    //текстуры

    public OpenGLRenderer(Context context) {
        this.context = context;
    }
    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {

        //чистит экран
        glClearColor(0f, 0f, 0f, 1f);

        //разрешает проекции
        glEnable(GL_DEPTH_TEST);

        createAndUseProgramm();
        getLocations();
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
//        Log.i("RENDER LOG", String.valueOf(texture));
    }

    private void createAndUseProgramm() {

        //берет шейдер файлы
        int vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader);

        //создает ид рендера
        programId = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);
        glUseProgram(programId);
    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        //full screen
        glViewport(0, 0, width, height);
        this.width = width;
        this.height = height;

        //создает проекцию
        createProjectionMatrix(width, height);
        createProjectionMatrixForInterface(width, height);

        //биндит матрицу
        bindMatrix();
        bindMatrixForInterface();
    }

    @Override
    public void onDrawFrame(GL10 arg0) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        //Камера для игрока
        createViewMatrix();

        //Камера для интерфейса
        createViewMatrixForInterface();

//        bindMatrix();
//        bindMatrixForInterface();

        //Игрок
        //Берем переменные шейдера, передаем массив данных для текущих объектов
        drawPlayer(dynamicObjects);

        //Вражины
        for(int i=0; i < enemies.length; i++){
            if(enemies[i] != null) {
                bindData(enemies[i]);
                drawEnemies();
            }
        }

        //Платформа
        //Берем переменные шейдера, передаем массив данных для текущих объектов
        for(int i=0; i < platforms.length; i++){
            if(platforms[i] != null) {
                bindData(platforms[i]);
                drawPlatform();
            }
        }

        //Гейм - пад
        if(gamePad != null) {
            bindData(gamePad);
            drawGamePad();
        }

    }


    private void bindData(FloatBuffer floatBuffer) {

        floatBuffer.position(0);

        // из какого массива брать данные и по каким правилам
        glVertexAttribPointer(aPositionLocation, POSITION_COUNT, GL_FLOAT,
                false, 20, floatBuffer);
        glEnableVertexAttribArray(aPositionLocation);

}

    private void drawPlayer(FloatBuffer floatBuffer) {
        bindData(floatBuffer);
        setTexture(floatBuffer);
        Matrix.setIdentityM(mModelMatrix, 0);
        bindMatrix();

//        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }

    private void setTexture(FloatBuffer floatBuffer) {

        // координаты текстур
        // параметр отвечает за точку отсчета в массиве
        floatBuffer.position(3);

        //параметры 2 - количество занчений которые нужно взять, 5 - количество байт до следующей точки
        glVertexAttribPointer(aTextureLocation, 2, GL_FLOAT,
                false, 20, floatBuffer);
        glEnableVertexAttribArray(aTextureLocation);

        // помещаем текстуру в target 2D юнита 0
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, texture);

        // юнит текстуры
        glUniform1i(uTextureUnitLocation, 0);
    }

    private void drawEnemies() {
        Matrix.setIdentityM(mModelMatrix, 0);
        bindMatrix();

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }

    private void drawGamePad() {

        Matrix.setIdentityM(mModelMatrixForInterface, 0);
        bindMatrixForInterface();

        glUniform4f(uColorLocation, 0.0f, 1.0f, 1.0f, 1.5f);
        glDrawArrays(GL_TRIANGLES, 0, 3);

        glDrawArrays(GL_TRIANGLES, 3, 3);

        glDrawArrays(GL_TRIANGLES, 6, 3);

    }

    private void drawPlatform(){
        Matrix.setIdentityM(mModelMatrix, 0);

        glUniform4f(uColorLocation, 1.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    }

    private void createViewMatrix() {
        // точка положения камеры
        float eyeX = dynamicObjectCordX;
        float eyeY = 0;
        float eyeZ = 3;

        // точка направления камеры
        float centerX = dynamicObjectCordX;
        float centerY = 0;
        float centerZ = 0;

        // up-вектор
        float upX = 0;
        float upY = 1;
        float upZ = 0;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    private void createViewMatrixForInterface() {
        // точка положения камеры
        float eyeX = 0;
        float eyeY = 0;
        float eyeZ = 3;

        // точка направления камеры
        float centerX = 0;
        float centerY = 0;
        float centerZ = 0;

        // up-вектор
        float upX = 0;
        float upY = 1;
        float upZ = 0;

        Matrix.setLookAtM(mViewMatrixForInterface, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    private void createProjectionMatrix(int width, int height) {

        float left = -3;
        float right = 3;
        float bottom = -3;
        float top = 3;

        float near = 2;
        float far = 12;

        float ratio = 1;
        if (width > height) {
            ratio = (float) width / height;
            left *= ratio;
            right *= ratio;
        } else {
            ratio = (float) height / width;
            bottom *= ratio;
            top *= ratio;
        }

        Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
        //Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    private void createProjectionMatrixForInterface(int width, int height) {

        float left = -5;
        float right = 5;
        float bottom = -5;
        float top = 5;

        float near = 2;
        float far = 12;

        float ratio = 1;
        if (width > height) {
            ratio = (float) width / height;
            left *= ratio;
            right *= ratio;
        } else {
            ratio = (float) height / width;
            bottom *= ratio;
            top *= ratio;
        }

        Matrix.orthoM(mProjectionMatrixForInterface, 0, left, right, bottom, top, near, far);

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