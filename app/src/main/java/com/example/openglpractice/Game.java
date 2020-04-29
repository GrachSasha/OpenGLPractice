package com.example.openglpractice;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.Toast;

import static java.security.AccessController.getContext;
import static java.util.ResourceBundle.getBundle;

class Game {
    Thread thread;
    gameController gController;
    GLSurfaceView glSurfaceView;

    Game(gameController gm, GLSurfaceView glSurfaceView){
        gController = gm;
        this.glSurfaceView = glSurfaceView;
    }

    public Game(GLSurfaceView glSurfaceView) {
        this.glSurfaceView = glSurfaceView;
    }

    public void startGame(){

    }
}
