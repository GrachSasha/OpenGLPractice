package com.example.openglpractice;

import android.opengl.GLSurfaceView;

import static java.util.ResourceBundle.getBundle;

class Game {
    Thread thread;
    playerController gController;
    GLSurfaceView glSurfaceView;

    Game(playerController gm, GLSurfaceView glSurfaceView){
        gController = gm;
        this.glSurfaceView = glSurfaceView;
    }

    public Game(GLSurfaceView glSurfaceView) {
        this.glSurfaceView = glSurfaceView;
    }

    public void startGame(){

    }
}
