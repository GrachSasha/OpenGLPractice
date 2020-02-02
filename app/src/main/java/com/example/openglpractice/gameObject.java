package com.example.openglpractice;

public class gameObject {
    //init
    static int gameObjectCounter = 0;
    static gameObject gameObjectPool[] = new gameObject[physicForObject.MAXOBJECTS];

    //fields
    int objectId;
    float[] model;
    physicForObject objectPhysic;
    int speed;
    boolean isVisible = false;


    public gameObject(int idToDowload){
        model = downloadModel(idToDowload);
        objectId = this.hashCode();
        addToPool(this);
    }

    //or inner class

    public void initInComponents(){
        sendToPhysic(this);
    }
    private void sendToPhysic(gameObject gObject){
         objectPhysic = new physicForObject(gObject);
    }


    private float[] downloadModel(int idToDowload) {
        model[0]= 1.0f;
        return model;
    }

    private void addToPool(gameObject gameObject) {
        if(gameObjectCounter < physicForObject.MAXOBJECTS){
            gameObjectPool[gameObjectCounter] = gameObject;
            gameObjectCounter++;
        }
    }
}
