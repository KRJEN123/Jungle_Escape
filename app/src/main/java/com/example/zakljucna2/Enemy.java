package com.example.zakljucna2;
public class Enemy {
    private float x, y;
    private int width, height;
    private int direction = 1; // 1 = right, 2 = left
    private float startingPosition, endingPosition;
    private int currentFrame = 0;

    public Enemy(float x, float y, int width, int height, float startingPosition, float endingPosition) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startingPosition = startingPosition;
        this.endingPosition = endingPosition;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getStartingPosition() {
        return startingPosition;
    }

    public float getEndingPosition() {
        return endingPosition;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int frame) {
        this.currentFrame = frame;
    }
}
