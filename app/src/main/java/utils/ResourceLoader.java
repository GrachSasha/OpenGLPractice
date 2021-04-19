package utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ResourceLoader{

    private AssetManager resourceLoaderAssetManager;
    private Context resourceLoaderContext;
//    private float [][] platforms;
    private List<String> platforms;
    private final String RESOURCE_LOADER = "ё";

    public ResourceLoader(Context context){
        resourceLoaderContext = context;
        resourceLoaderAssetManager = resourceLoaderContext.getAssets();
        platforms = new ArrayList<>();
        Log.i(RESOURCE_LOADER, "Context: " + resourceLoaderContext.toString() + " Assets: " + resourceLoaderAssetManager);
    }

    //todo ВАЖНО! ДОБАВИТЬ ТЕГ С УТОЧЕНИЕМ ТИПА РЕСУРСОВ!
    public List<String> loadResource(String lvl) {
        try {

            String tmp="";
            String[] files = resourceLoaderAssetManager.list("levels");
            String level = "levels/" + lvl + ".xml";

            Log.i(RESOURCE_LOADER, files[0]);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(resourceLoaderAssetManager.open(level),"utf-8");

            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                if(xpp.getEventType() == XmlPullParser.START_TAG){
                    if(xpp.getName().equals("vertices")){
                        xpp.next();
                        tmp = xpp.getText();
                        platforms.add(tmp);
                        Log.i(RESOURCE_LOADER, "text: " + tmp);
                    }
                }
                xpp.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return platforms;
    }

//    public void loadResource(String lvl) {
//        try {
//            String[] files = resourceLoaderAssetManager.list("levels");
//            Log.i(RESOURCE_LOADER, files[0]);
//            String level = "levels/" + lvl + ".xml";
//
//            // получаем фабрику
//            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//            // включаем поддержку namespace (по умолчанию выключена)
//            factory.setNamespaceAware(true);
//            // создаем парсер
//            XmlPullParser xpp = factory.newPullParser();
//
//            xpp.setInput(resourceLoaderAssetManager.open(level),"utf-8");
//
//            //=============================================================
//            String tmp="";
//            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
//                switch (xpp.getEventType()) {
//                    // начало документа
//                    case XmlPullParser.START_DOCUMENT:
//                        Log.i(RESOURCE_LOADER, "START_DOCUMENT");
//                        break;
//                    // начало тэга
//                    case XmlPullParser.START_TAG:
//                        Log.i(RESOURCE_LOADER, "START_TAG: name = " + xpp.getName()
//                                + ", depth = " + xpp.getDepth() + ", attrCount = "
//                                + xpp.getAttributeCount());
//                        tmp = "";
//                        for (int i = 0; i < xpp.getAttributeCount(); i++) {
//                            tmp = tmp + xpp.getAttributeName(i) + " = "
//                                    + xpp.getAttributeValue(i) + ", ";
//                        }
//                        if (!TextUtils.isEmpty(tmp))
//                            Log.i(RESOURCE_LOADER, "Attributes: " + tmp);
//                        break;
//                    // конец тэга
//                    case XmlPullParser.END_TAG:
//                        Log.i(RESOURCE_LOADER, "END_TAG: name = " + xpp.getName());
//                        break;
//                    // содержимое тэга
//                    case XmlPullParser.TEXT:
//                            Log.i(RESOURCE_LOADER, "text = " + xpp.getText());
//                        break;
//
//                    default:
//                        break;
//                }
//                // следующий элемент
//                xpp.next();
//            }
//            Log.i(RESOURCE_LOADER, "END_DOCUMENT");
//            //=============================================================
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        }
//    }

}


