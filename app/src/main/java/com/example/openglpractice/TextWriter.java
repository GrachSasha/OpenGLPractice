package com.example.openglpractice;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static com.example.openglpractice.MainActivity.render;

public class TextWriter implements RenderCommandsForDynamicObjects{

    //===fields===//
    volatile FloatBuffer textBuffer;

    volatile private float[] charVertices = {
                //x    y          //x     y
    /*1*/        -2f, -1f, 0,     0.11f, 0.03f,
    /*2*/        3f, -1f, 0,         0.11f, 0.10f,
    /*3*/        -2f, 2f,0,            0.20f, 0.03f,
    /*4*/        3f, 2f, 0,              0.20f, 0.10f,
    };
    private float[][] chars = new float[1024][20];

    //===fields===//

    private Map<String, float[]> alphavit;

    public TextWriter(){
        alphavit = new HashMap<String, float[]>();
        fillAlphavit();
    }

    float [] sign = {0, 0.05f, 0, 0.10f, 0.10f, 0.05f, 0.10f, 0.10f};
    float [] nine = {0.11f, 0.03f, 0.11f, 0.10f,0.20f, 0.03f, 0.20f, 0.10f,};

    private void fillAlphavit() {
        alphavit.put("\"", new float[]{0, 0.05f, 0, 0.10f, 0.10f, 0.05f, 0.10f, 0.10f});
        alphavit.put("9", new float[]{0.11f, 0.03f, 0.11f, 0.10f,0.20f, 0.03f, 0.20f, 0.10f,});
    }

    @Override
    public void drawDynamicObject(int texture) {
//        render.setTexture(textBuffer, texture, false);
//        //работает и без этого render.setMatrixForDynamicObject();
//        render.setMatrixForText();
//        render.bindMatrixForText();
//        render.drawArraysForDynamicObject(GL_TRIANGLE_STRIP, 0, 4);

        for(int i = 0; i < chars.length; i++) {
            prepareCoordinatesAndConvert(chars[i]);
            render.bindData(textBuffer);
            render.setTexture(textBuffer, texture, false);
            render.setMatrixForStaticObject();
            render.bindMatrix();
            render.drawArraysForStaticObject(GL_TRIANGLE_STRIP, 0, 4);
        }
    }

    @Override
    public void prepareCoordinatesAndConvert(float[] gObject) {
        textBuffer =  ByteBuffer
                .allocateDirect(gObject.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        textBuffer.put(gObject).position(0);
    }

    public void setText(String text, float fontSize, float[] coordinates){
//        if(text.equals("9")){
//            float[] buffer = alphavit.get("9");
//            charVertices[3] = buffer[0];
//            charVertices[4] = buffer[1];
//            charVertices[8] = buffer[2];
//            charVertices[9] = buffer[3];
//            charVertices[13] = buffer[4];
//            charVertices[14] = buffer[5];
//            charVertices[18] = buffer[6];
//            charVertices[19] = buffer[7];
//
//        }else{
//            float[] buffer = alphavit.get("\"");
//            charVertices[3] = buffer[0];
//            charVertices[4] = buffer[1];
//            charVertices[8] = buffer[2];
//            charVertices[9] = buffer[3];
//            charVertices[13] = buffer[4];
//            charVertices[14] = buffer[5];
//            charVertices[18] = buffer[6];
//            charVertices[19] = buffer[7];
//        }
        float shift = 0f;
        for(int i = 0; i <  text.length(); i++){

                chars[i] = createSign(text.charAt(i), fontSize, coordinates, 0);

        }
    }

    private float[] createSign(char charAt, float size, float[] coordinates, float shift) {
        float [] sign = new float[20];
        float [] textureCoordinates = alphavit.get(String.valueOf(charAt));

        sign[0] = coordinates[0];
        sign[1] = coordinates[1];
        sign[2] = 0;
        sign[3] = textureCoordinates[0];
        sign[4] = textureCoordinates[1];

        sign[5] = sign[0] + size;
        sign[6] = sign[1];
        sign[7] = 0;
        sign[8] = textureCoordinates[2];
        sign[9] = textureCoordinates[3];

        sign[10] = sign[0];
        sign[11] = sign[1] + size;
        sign[12] = 0;
        sign[13] = textureCoordinates[4];
        sign[14] = textureCoordinates[5];

        sign[15] = sign[0] + size;
        sign[16] = sign[1] + size;
        sign[17] = 0;
        sign[18] = textureCoordinates[6];
        sign[19] = textureCoordinates[7];

        return sign;
    }
}
