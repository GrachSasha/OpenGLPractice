package com.example.openglpractice;

public class dynamicObject{

    //init
    static int gameObjectCounter = 0;
    public static dynamicObject dynamicObjectPool[] = new dynamicObject[physicForObject.MAXOBJECTS];

    //fields
    private int objectId;
    physicForObject physic;
    alternativePhysicForObject alternativePhysic;
    private boolean invetoryOpen = false;

    public dynamicObject(float[] vertices){
        objectId = this.hashCode();
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
