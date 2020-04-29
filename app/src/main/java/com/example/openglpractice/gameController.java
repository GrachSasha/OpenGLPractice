package com.example.openglpractice;

public class gameController {



    dynamicObject gObject;

    public gameController(dynamicObject gObject) {
        this.gObject = gObject;
    }

    public void walk(){
        gObject.physic.doStep();
    }

    public void jump(){
        gObject.physic.doJump();
    }

    public void openInventory(){gObject.openInventory();}

}
