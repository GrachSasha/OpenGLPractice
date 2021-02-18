package com.example.openglpractice;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static com.example.openglpractice.MenuActivity.render;

public class TextWriter implements RenderCommandsForDynamicObjects{

    //===fields===//
//    volatile FloatBuffer textBuffer;

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

    private void fillAlphavit() {
        //use fontturned
        alphavit.put("\"", new float[]{0, 0.05f, 0, 0.10f, 0.10f, 0.05f, 0.10f, 0.10f});
        alphavit.put("9", new float[]{0.11f, 0.03f, 0.11f, 0.10f, 0.20f, 0.03f, 0.20f, 0.10f,});
        alphavit.put("0", new float[]{0.22f, 0.03f, 0.22f, 0.10f, 0.31f, 0.03f, 0.31f, 0.10f,});
        alphavit.put("s", new float[]{0.33f, 0.03f, 0.33f, 0.10f, 0.42f, 0.03f, 0.42f, 0.10f,});
        alphavit.put("j", new float[]{0.448f, 0.03f, 0.448f, 0.10f, 0.53f, 0.03f, 0.53f, 0.10f,});
        alphavit.put("a", new float[]{0.534f, 0.03f, 0.534f, 0.10f, 0.64f, 0.03f, 0.64f, 0.10f,});
        //===//
        alphavit.put("/", new float[]{0,    0.115f, 0,      0.22f, 0.10f, 0.115f, 0.10f, 0.22f});
        alphavit.put(".", new float[]{0.11f, 0.115f, 0.11f, 0.20f, 0.20f, 0.10f, 0.20f, 0.20f,});
        alphavit.put("1", new float[]{0.22f, 0.115f, 0.22f, 0.20f, 0.31f, 0.10f, 0.31f, 0.20f,});
        alphavit.put("t", new float[]{0.33f, 0.115f, 0.33f, 0.20f, 0.42f, 0.10f, 0.42f, 0.20f,});
        alphavit.put("k", new float[]{0.448f, 0.115f, 0.448f, 0.20f, 0.53f, 0.10f, 0.53f, 0.20f,});
        alphavit.put("b", new float[]{0.534f, 0.115f, 0.534f, 0.20f, 0.64f, 0.10f, 0.64f, 0.20f,});
        //===//
        alphavit.put("?", new float[]{0,     0.20f,  0,     0.32f, 0.10f, 0.20f, 0.10f, 0.32f});
        alphavit.put(",", new float[]{0.11f, 0.20f, 0.11f, 0.32f, 0.20f, 0.20f, 0.20f, 0.32f,});
        alphavit.put("2", new float[]{0.22f, 0.20f, 0.22f, 0.32f, 0.31f, 0.20f, 0.31f, 0.32f,});
        alphavit.put("u", new float[]{0.33f, 0.20f, 0.33f, 0.32f, 0.42f, 0.20f, 0.42f, 0.32f,});
        alphavit.put("l", new float[]{0.448f, 0.20f, 0.448f, 0.32f, 0.53f, 0.20f, 0.53f, 0.32f,});
        alphavit.put("o", new float[]{0.534f, 0.20f, 0.534f, 0.32f, 0.64f, 0.20f, 0.64f, 0.32f,});
        //===//
        alphavit.put("%", new float[]{0,     0.308f,  0,     0.418f, 0.10f, 0.30f, 0.10f, 0.42f});
        alphavit.put(";", new float[]{0.11f, 0.308f, 0.11f, 0.418f, 0.20f, 0.30f, 0.20f, 0.42f,});
        alphavit.put("3", new float[]{0.22f, 0.308f, 0.22f, 0.418f, 0.31f, 0.30f, 0.31f, 0.42f,});
        alphavit.put("v", new float[]{0.33f, 0.308f, 0.33f, 0.418f, 0.42f, 0.30f, 0.42f, 0.42f,});
        alphavit.put("m", new float[]{0.448f, 0.308f, 0.448f, 0.418f, 0.53f, 0.30f, 0.53f, 0.42f,});
        alphavit.put("d", new float[]{0.534f, 0.308f, 0.534f, 0.418f, 0.64f, 0.30f, 0.64f, 0.42f,});
        //===//

        alphavit.put(":", new float[]{0.11f, 0.4f, 0.11f, 0.5f, 0.20f, 0.4f, 0.20f, 0.5f,});
        alphavit.put("4", new float[]{0.22f, 0.4f, 0.22f, 0.5f, 0.31f, 0.4f, 0.31f, 0.5f,});
        alphavit.put("w", new float[]{0.33f, 0.4f, 0.33f, 0.5f, 0.42f, 0.4f, 0.42f, 0.5f,});
        alphavit.put("n", new float[]{0.448f, 0.4f, 0.448f, 0.5f, 0.53f, 0.4f, 0.53f, 0.5f,});
        alphavit.put("e", new float[]{0.534f, 0.4f, 0.534f, 0.5f, 0.64f, 0.4f, 0.64f, 0.5f,});
    }

    @Override
    public void drawDynamicObject(int texture) {
        for(int i = 0; i < chars.length; i++) {
            FloatBuffer buffer = prepareCoordinatesAndConvert2(chars[i]);
            render.bindData(buffer);
            render.setTexture(buffer, texture, false);
            render.setMatrixForStaticObject();
            render.bindMatrixForLevel();
            render.drawArraysForStaticObject(GL_TRIANGLE_STRIP, 0, 4);
        }
    }

    @Override
    public void prepareCoordinatesAndConvert(float[] gObject) {
//        textBuffer =  ByteBuffer
//                .allocateDirect(gObject.length * 4)
//                .order(ByteOrder.nativeOrder())
//                .asFloatBuffer();
//        textBuffer.put(gObject).position(0);
    }

    //todo add some interface
    public FloatBuffer prepareCoordinatesAndConvert2(float[] gObject) {
        FloatBuffer textBuffer;
        textBuffer =  ByteBuffer
                .allocateDirect(gObject.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        textBuffer.put(gObject).position(0);
        return textBuffer;
    }

    public void setText(String text, float signSize, float[] coordinates){
        float shift = signSize;
        for(int i = 0; i <  text.length(); i++){
            if(i != 0){
                chars[i] = createSign(text.charAt(i), signSize, coordinates, shift);
                shift += signSize;
            } else {
                chars[i] = createSign(text.charAt(i), signSize, coordinates, 0);
            }
        }
    }

    private float[] createSign(char charAt, float size, float[] coordinates, float shift) {
        float [] sign = new float[20];
        float [] textureCoordinates = alphavit.get(String.valueOf(charAt));

        sign[0] = coordinates[0] + shift;
        sign[1] = coordinates[1];
        sign[2] = 0;
        sign[3] = textureCoordinates[0];
        sign[4] = textureCoordinates[1];

        sign[5] = sign[0] + size;
        sign[6] = sign[1];
        sign[7] = 0;
        sign[8] = textureCoordinates[2];
        sign[9] = textureCoordinates[3];

        sign[10] = sign[0] ;
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
