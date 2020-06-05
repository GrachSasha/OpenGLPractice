package com.example.openglpractice;

import static com.example.openglpractice.MainActivity.render;

public class dynamicObject{

    //init
    static int gameObjectCounter = 0;
    public static dynamicObject dynamicObjectPool[] = new dynamicObject[physicForObject.MAXOBJECTS];

    //fields
    int renderNumber;
    public boolean player;
    physicForObject physic;
    private boolean invetoryOpen = false;
    private int objectId;

    public dynamicObject(float[] vertices, boolean pl){
        objectId = this.hashCode();
        player = pl;
        physic = new physicForObject(vertices, this);
        addToPool(this);
    }

    private void addToPool(dynamicObject dynamicObject) {
        if(gameObjectCounter < physicForObject.MAXOBJECTS){
            dynamicObjectPool[gameObjectCounter] = dynamicObject;
            gameObjectCounter++;
        }
    }

    public void openInventory() { invetoryOpen = true;}


    public void createModel() {
        try {
            if(player){render.prepareAndChangeModelForPlayer(physic.getObjVertices());}
                else {renderNumber = render.prepareDynamicModelsForEnemy(this);}
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }
    }
}
