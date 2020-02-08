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
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glLineWidth;

public class OpenGLRenderer implements Renderer {

    private final static int POSITION_COUNT = 3;
    private final static long TIME = 1000L;

    private Context context;

    private FloatBuffer dynamicObjects;
    private FloatBuffer staticObjects;


    private int uColorLocation;
    private int uMatrixLocation;
    private int programId;

    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    private float[] mMatrix = new float[16];
    public boolean mooved;


    public OpenGLRenderer(Context context) {
        this.context = context;
    }
    @Override
    public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {

        //чистит экран
        glClearColor(0f, 0f, 0f, 1f);

        //разрешает проекции
        glEnable(GL_DEPTH_TEST);

        //берет шейдер файлы
        int vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader);

        //создает ид рендера
        programId = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);
        glUseProgram(programId);

        //создает камеру
        createViewMatrix();

    }

    @Override
    public void onSurfaceChanged(GL10 arg0, int width, int height) {
        //full screen
        glViewport(0, 0, width, height);

        //создает проекцию
        createProjectionMatrix(width, height);

        //биндит матрицу
        bindMatrix();
    }

    @Override
    public void onDrawFrame(GL10 arg0) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        //Сцена
        //Берем переменные шейдера, передаем массив данных для текущих объектов
        bindData(staticObjects);
        drawScene();

        //Треугольник
        //Берем переменные шейдера, передаем массив данных для текущих объектов
        bindData(dynamicObjects);
        drawTriangle();
    }


    private void bindData(FloatBuffer floatBuffer) {
        // примитивы
        int aPositionLocation = glGetAttribLocation(programId, "a_Position");
        floatBuffer.position(0);

        // из какого массива брать данные и по каким правилам
        glVertexAttribPointer(aPositionLocation, POSITION_COUNT, GL_FLOAT,
                false, 0, floatBuffer);
        glEnableVertexAttribArray(aPositionLocation);

        // цвет
        uColorLocation = glGetUniformLocation(programId, "u_Color");

        // матрица
        uMatrixLocation = glGetUniformLocation(programId, "u_Matrix");
    }

    private void drawScene() {
        Matrix.setIdentityM(mModelMatrix, 0);
        bindMatrix();

        glLineWidth(3);
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 0, 2);

        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_LINES, 2, 2);

        glUniform4f(uColorLocation, 1.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 4, 2);
    }

    private void createProjectionMatrix(int width, int height) {

        float left = -1;
        float right = 1;
        float bottom = -1;
        float top = 1;

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

    private void createViewMatrix() {
        // точка положения камеры
        float eyeX = 2;
        float eyeY = 2;
        float eyeZ = 3;

        // точка направления камеры
        float centerX = 0;
        float centerY = 0;
        float centerZ = 0;

        // up-вектор
        float upX = 0;
        float upY = 1;
        float upZ = 0;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }


    private void bindMatrix() {
        Matrix.multiplyMM(mMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMatrix, 0, mProjectionMatrix, 0, mMatrix, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);
    }

    public void prepareDynamicModels(gameObject gObject) {
        dynamicObjects =  ByteBuffer
                .allocateDirect(gObject.getVertices().length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        dynamicObjects.put(gObject.getVertices()).position(0);
    }

    public void prepareStaticModels(gameObject gObject){
        staticObjects = ByteBuffer
                .allocateDirect(gObject.getVertices().length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        staticObjects.put(gObject.getVertices()).position(0);
    }

    private void drawTriangle() {
        Matrix.setIdentityM(mModelMatrix, 0);
        setModelMatrix();
        //if(mooved){dragTriangle();}
        bindMatrix();

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }


    public void dragTriangle(){
        Matrix.translateM(mModelMatrix, 0, 1f, 1f, 1f);
        mooved = false;
    }

    public void dragTriangle(float x){
        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.translateM(mModelMatrix, 0,  x, x, x);
        bindMatrix();
        mooved = true;
    }
    private void setModelMatrix() {
//        float angle = (float)(SystemClock.uptimeMillis() % TIME) / TIME * 360;
//        Matrix.rotateM(mModelMatrix, 0, angle, 0, 1, 1);

        Matrix.translateM(mModelMatrix, 0, 1.0f, 1.0f, 1.0f);
    }
}