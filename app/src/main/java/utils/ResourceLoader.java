package utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;


public class ResourceLoader{

    private AssetManager resourceLoaderAssetManager;
    private final String RESOURCE_LOADER = "RESOURCE_LOADER";

    private Context resourceLoaderContext;

    public ResourceLoader(Context context){
        resourceLoaderContext = context;
    }

    public void loadResource(String lvl) {
        try {
            String[] files = resourceLoaderAssetManager.list("levels");
            Log.i(RESOURCE_LOADER, files[0]);
            String level = "levels/ + " + lvl + " + .xml";

            // получаем фабрику
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            // включаем поддержку namespace (по умолчанию выключена)
            factory.setNamespaceAware(true);
            // создаем парсер
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(resourceLoaderAssetManager.open(level),"utf-8");

            //=============================================================
            String tmp="";
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    // начало документа
                    case XmlPullParser.START_DOCUMENT:
                        Log.d(RESOURCE_LOADER, "START_DOCUMENT");
                        break;
                    // начало тэга
                    case XmlPullParser.START_TAG:
                        Log.d(RESOURCE_LOADER, "START_TAG: name = " + xpp.getName()
                                + ", depth = " + xpp.getDepth() + ", attrCount = "
                                + xpp.getAttributeCount());
                        tmp = "";
                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
                            tmp = tmp + xpp.getAttributeName(i) + " = "
                                    + xpp.getAttributeValue(i) + ", ";
                        }
                        if (!TextUtils.isEmpty(tmp))
                            Log.d(RESOURCE_LOADER, "Attributes: " + tmp);
                        break;
                    // конец тэга
                    case XmlPullParser.END_TAG:
                        Log.d(RESOURCE_LOADER, "END_TAG: name = " + xpp.getName());
                        break;
                    // содержимое тэга
                    case XmlPullParser.TEXT:
                        Log.d(RESOURCE_LOADER, "text = " + xpp.getText());
                        break;

                    default:
                        break;
                }
                // следующий элемент
                xpp.next();
            }
            Log.d(RESOURCE_LOADER, "END_DOCUMENT");
            //=============================================================

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

}

//    final AssetManager resourceLoaderAssetManager = getApplicationContext().getAssets();
//        try {
//        String[] files = resourceLoaderAssetManager.list("levels");
//        Log.i(RESOURCE_LOADER, files[0]);
//        InputStream inputStream = resourceLoaderAssetManager.open("levels/level1.xml");
//        byte[] buffer = null;
//        int size = inputStream.available();
//        buffer = new byte[size];
//        inputStream.read(buffer);
//        inputStream.close();
//        String dataFromXML = new String(buffer);
//        Log.i(RESOURCE_LOADER, dataFromXML);
//    } catch (IOException e) {
//        e.printStackTrace();
//    }

