package com.example.openglpractice;

public class gameObject {

    //init
    static int gameObjectCounter = 0;
    public static gameObject gameObjectPool[] = new gameObject[physicForObject.MAXOBJECTS];

    //fields
    private int objectId;
    physicForObject physic;
    private boolean invetoryOpen = false;

    public gameObject(float[] vertices){
        objectId = this.hashCode();
        physic = new physicForObject(vertices, this);
        addToPool(this);
    }

    private void addToPool(gameObject gameObject) {
        if(gameObjectCounter < physicForObject.MAXOBJECTS){
            gameObjectPool[gameObjectCounter] = gameObject;
            gameObjectCounter++;
        }
    }

    public void openInventory() { invetoryOpen = true;}

}
