package com.example.openglpractice;

public class physicForObject {

    //init
    static final int MAXOBJECTS = 10;

    public static physicForObject objectPhysicPool[] = new physicForObject[MAXOBJECTS];
    static int objectCounter = 0;

    //fields
    private int physicId;
    private int xCoord;
    private int yCoord;

    physicForObject(gameObject gObject){
        physicId = gObject.objectId;
        xCoord = 1;
        yCoord = 1;
        addToPool(this);
    }


    private void addToPool(physicForObject objPhysic) {
        if(objectCounter < MAXOBJECTS ){
            objectPhysicPool[objectCounter] = objPhysic;
            objectCounter++;
        }
    }

    //in thread

}
