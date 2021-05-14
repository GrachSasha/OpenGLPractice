package com.example.openglpractice;

import android.util.Log;
import android.view.MotionEvent;

public class playerController{

    dynamicObject gObject;
    Thread controlDeamon;
    MotionEvent eventPool[] = new MotionEvent[10];
    private String PLAYER_CONTROLLER = "PLAYER_CONTROLLER";

    public playerController(dynamicObject gObject) {
        this.gObject = gObject;
    }

    public void walkLeft(){
        gObject.doStepLeft();
        Log.i(PLAYER_CONTROLLER,gObject.TEXTURE_NAME + "walLeft");
    }

    public void walkRight(){
        gObject.doStepRight();
        Log.i(PLAYER_CONTROLLER,gObject.TEXTURE_NAME + "walRight");
    }

    public void walkUp(){
        gObject.doStepRight();
        Log.i(PLAYER_CONTROLLER,gObject.TEXTURE_NAME + "walUp");
    }

    public void walkDown(){
        gObject.doStepRight();
        Log.i(PLAYER_CONTROLLER,gObject.TEXTURE_NAME + "walDown");
    }

    public void jump(){
        gObject.doJump();
    }

    public void openInventory(){gObject.openInventory();}

}
