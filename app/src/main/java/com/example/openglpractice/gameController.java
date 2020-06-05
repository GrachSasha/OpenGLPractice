package com.example.openglpractice;

public class gameController {

    dynamicObject gObject;

    public gameController(dynamicObject gObject) {
        this.gObject = gObject;
    }

    public void walkLeft(){
        gObject.physic.doStepLeft();
    }

    public void walkRight(){
        gObject.physic.doStepRight();
    }

    public void jump(){
        gObject.physic.doJump();
    }

    public void openInventory(){gObject.openInventory();}

}
