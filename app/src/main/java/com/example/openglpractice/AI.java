package com.example.openglpractice;

public class AI extends dynamicObject implements Runnable{

    private gameController gameController;
    private Thread controlThread;

    AI(float[] vert) {
        super(vert, false);
        gameController = new gameController(this);

        controlThread = new Thread(this);
        controlThread.start();
    }

    @Override
    public void run() {
        do {
            this.gameController.walkRight();
            this.gameController.jump();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while (true);

    }
}