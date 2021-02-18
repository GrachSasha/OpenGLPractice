//package com.example.openglpractice;
//
//import static com.example.openglpractice.LevelActivity.render;
//
//public class AI implements Runnable{
//
//    private float[] vertices;
//    private dynamicObjectInfo physic;
//    private playerController playerController;
//    private Thread controlThread;
//
//    AI(float[] vert) {
//        vertices = vert;
////        physic = new dynamicObjectInfo(vertices, false,"");
////        playerController = new playerController(physic);
//
//        controlThread = new Thread(this);
//        controlThread.start();
//    }
//
//    public void createModel() {
//        try {
//            render.prepareDynamicModelsForEnemy(physic);
//        } catch (NullPointerException exc) {
//            exc.printStackTrace();
//        }
//    }
//
//    @Override
//    public void run() {
//        do {
//            this.playerController.walkRight();
//            this.playerController.jump();
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }while (true);
//
//    }
//}