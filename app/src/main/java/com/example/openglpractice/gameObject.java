package com.example.openglpractice;

public class gameObject {

    //init
    static int gameObjectCounter = 0;
    static gameObject gameObjectPool[] = new gameObject[physicForObject.MAXOBJECTS];

    //fields

    float[] objTexture;
    int objectId;
    physicForObject physic;

    public gameObject(float[] vertices){

        objectId = this.hashCode();
        addToPool(this);
        createPhysic(vertices, this);
    }

    private physicForObject createPhysic(float[] vert, gameObject gameObject){
         return physic = new physicForObject(vert, gameObject);
    }


    private void addToPool(gameObject gameObject) {
        if(gameObjectCounter < physicForObject.MAXOBJECTS){
            gameObjectPool[gameObjectCounter] = gameObject;
            gameObjectCounter++;
        }
    }

//    private void loadTexture(){
//
//    }

//    public void initInComponents(){
//        createPhysic(this);
//    }

//    public gameObject(int idToDowload){
//        objTexture = downloadModel(idToDowload);
//        objectId = this.hashCode();
//        addToPool(this);
//    }

//    private float[] downloadModel(int idToDowload) {
//        objTexture[0]= 1.0f;
//        return objTexture;
//    }
}
