package com.example.openglpractice;

public class dynamicObject{

    //init
    static int gameObjectCounter = 0;
    public static dynamicObject dynamicObjectPool[] = new dynamicObject[physicForObject.MAXOBJECTS];

    //fields
    private int objectId;
    public boolean player = false;
    physicForObject physic;
    Thread physicThread;
    private boolean invetoryOpen = false;

    public dynamicObject(float[] vertices, boolean pl){
        objectId = this.hashCode();
        player = pl;
        //alternativePhysic = new alternativePhysicForObject(vertices, this);
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

}
