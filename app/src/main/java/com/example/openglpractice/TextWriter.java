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
    private float eyeX;

    volatile private float[] charVertices = {
                            //x     y
            -2f, -1f, 0,     0.11f, 0.03f,
            3f, -1f, 0,         0.11f, 0.10f,
            -2f, 2f,0,            0.20f, 0.03f,
            3f, 2f, 0,              0.20f, 0.10f,
    };
    //===fields===//

    private Map<String, float[]> alphavit;

    float[] nine = {0.11f, 0.03f, 0.11f, 0.10f,0.20f, 0.03f, 0.20f, 0.10f};
    float[] symbol = {0, 0.05f, 0, 0.10f, 0.10f, 0.05f, 0.10f, 0.10f};

    public TextWriter(){
        alphavit = new HashMap<String, float[]>();
        fillAlphavit();
    }

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

        prepareCoordinatesAndConvert(charVertices);
        render.bindData(textBuffer);
        render.setTexture(textBuffer, texture, false);
        render.setMatrixForStaticObject();
        render.bindMatrix();
        render.drawArraysForStaticObject(GL_TRIANGLE_STRIP,0,4);
    }

    @Override
    public void prepareCoordinatesAndConvert(float[] gObject) {
        textBuffer =  ByteBuffer
                .allocateDirect(charVertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        textBuffer.put(charVertices).position(0);
    }

    public void setText(String text){
        if(text.equals("9")){
            charVertices[3] = nine[0];
            charVertices[4] = nine[1];
            charVertices[8] = nine[2];
            charVertices[9] = nine[3];
            charVertices[13] = nine[4];
            charVertices[14] = nine[5];
            charVertices[18] = nine[6];
            charVertices[19] = nine[7];

        }else{
            charVertices[3] = symbol[0];
            charVertices[4] = symbol[1];
            charVertices[8] = symbol[2];
            charVertices[9] = symbol[3];
            charVertices[13] = symbol[4];
            charVertices[14] = symbol[5];
            charVertices[18] = symbol[6];
            charVertices[19] = symbol[7];
        }
    }
}
