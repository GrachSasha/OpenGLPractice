package com.example.openglpractice;

import android.view.MotionEvent;

public class playerController{

    dynamicObject gObject;
    Thread controlDeamon;
    MotionEvent eventPool[] = new MotionEvent[10];

    public playerController(dynamicObject gObject) {
        this.gObject = gObject;
    }

    public void walkLeft(){
        gObject.doStepLeft();
    }

    public void walkRight(){
        gObject.doStepRight();
    }

    public void jump(){
        gObject.doJump();
    }

    public void openInventory(){gObject.openInventory();}

}
