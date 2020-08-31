package com.example.openglpractice;

import android.app.Activity;
import android.content.res.AssetManager;
import android.util.Log;


import java.io.IOException;
import java.io.InputStream;

import static com.example.openglpractice.MainActivity.assetManager;

public class ResourceLoader {

    private final String RESOURCE_LOADER = "RESOURCE_LOADER";


    public void openLibrary() {
        try {
            String[] files = assetManager.list("levels");
//            Log.i(RESOURCE_LOADER, files[0]);
//            InputStream inputStream = assetManager.open("levels/level1.xml");
//            byte[] buffer = null;
//            int size = inputStream.available();
//            buffer = new byte[size];
//            inputStream.read(buffer);
//            inputStream.close();
//            String dataFromXML = new String(buffer);
//            Log.i(RESOURCE_LOADER, dataFromXML);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    final AssetManager assetManager = getApplicationContext().getAssets();
//        try {
//        String[] files = assetManager.list("levels");
//        Log.i(TEST_PO, files[0]);
//        InputStream inputStream = assetManager.open("levels/level1.xml");
//        byte[] buffer = null;
//        int size = inputStream.available();
//        buffer = new byte[size];
//        inputStream.read(buffer);
//        inputStream.close();
//        String dataFromXML = new String(buffer);
//        Log.i(TEST_PO, dataFromXML);
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
}
