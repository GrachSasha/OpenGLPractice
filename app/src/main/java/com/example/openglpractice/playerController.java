package com.example.openglpractice;

public class playerController {

    dynamicObject gObject;

    public playerController(dynamicObject gObject) {
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
