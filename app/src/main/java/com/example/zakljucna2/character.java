package com.example.zakljucna2;

public class character {
private float height;
private float width;
private boolean onPlatform;
private float x;
private float y;
private float weight=1.1f;
private float jumpVelocity=0;
private float jumpStrenght=30;
private boolean falling=true;
private boolean jumping=false;
public character(float height,float width,float x,float y){
    this.x=x;
    this.y=y;
    this.width=width;
    this.height=height;

}

    public float getHeight() {
        return height;
    }
    public boolean getJumping(){
    return jumping;
    }

    public float getjumpStrenght() {
        return jumpStrenght;
    }

    public float getjumpVelocity() {
        return jumpVelocity;
    }
    public boolean getFalling(){
        return falling;

    }
    public boolean getPlatform(){
        return onPlatform;

    }
    public float getWeight() {
        return weight;
    }

    public float getWidth() {
        return width;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    public float setX(float x){
        this.x=x;
        return x;

    }public float setY(float y){
        this.y=y;
        return y;

    }public float setjumpVelocity(float jumpVelocity){
        this.jumpVelocity=jumpVelocity;
        return jumpVelocity;

    }
    public void setJumping(boolean jumping){
        this.jumping=jumping;
    }
    public boolean setFalling(boolean falling){

    this.falling=falling;
    return falling;
    }
    public boolean setOnPlatform(boolean onPlatform){
        this.onPlatform=onPlatform;
        return true;
    }

}
