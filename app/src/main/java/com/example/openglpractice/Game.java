package com.example.openglpractice;

class Game implements Runnable {
    Thread thread;
    gameController gController;

    Game(gameController gm){
        gController = gm;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        do {
            gController.jump();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while (true);
    }
}
