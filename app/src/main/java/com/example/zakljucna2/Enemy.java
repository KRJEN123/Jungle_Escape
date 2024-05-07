package com.example.zakljucna2;

public class Enemy {
    private float x;
    private float y;
    private float width;
    private float height;
    private float startingPosition;
    private float endingPosition;
    public Enemy(float x,float y,float height,float width,float startingPosition,float endingPosition){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.startingPosition=startingPosition;
        this.endingPosition=endingPosition;


    }
    public float getX(){
        return x;

    }public float getY(){
        return y;

    }public float getWidth(){

        return width;
    }public float getHeight(){

        return height;
    }public float getStartingPosition(){
        return startingPosition;

    }public float getEndingPosition(){
        return endingPosition;
    }
    public float setX(float x){
        this.x=x;
        return x;


    }public float setY(float y){
        this.y=y;
        return y;

    }



}
