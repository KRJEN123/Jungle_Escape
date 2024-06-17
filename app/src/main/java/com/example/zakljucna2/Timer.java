package com.example.zakljucna2;


public class Timer extends Thread {
    private volatile boolean running = true;
    private long startTime;

    public void startTimer() {
        startTime = System.currentTimeMillis();
        this.start();
    }

    public void stopTimer() {
        running = false;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1); // Sleep for 1 millisecond to keep it efficient
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
