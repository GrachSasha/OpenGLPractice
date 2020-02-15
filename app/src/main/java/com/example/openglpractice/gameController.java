package com.example.openglpractice;

public class gameController {

    gameObject gObject;


    public gameController(gameObject gObject) {
        this.gObject = gObject;
    }

    public void walk(){
        gObject.physic.doStep();
    }

    public void jump(){
        gObject.physic.doJump();
    }
}
