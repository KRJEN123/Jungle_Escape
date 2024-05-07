package com.example.zakljucna2;

public class character {
private float height;
private float width;
private float x;
private float y;
private float weight=2;
private float jumpVelocity=0;
private float jumpStrenght=40;
private boolean falling=true;
public character(float height,float width,float x,float y){
    this.x=x;
    this.y=y;
    this.width=width;
    this.height=height;

}

    public float getHeight() {
        return height;
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
    public boolean setFalling(boolean falling){

    this.falling=falling;
    return falling;
    }

}
