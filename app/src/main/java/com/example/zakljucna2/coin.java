package com.example.zakljucna2;

public class coin {
    public float x;
    public float y;
    public float height;
    public float width;

    public coin(float x,float y,float height,float width){
        this.x=x;
        this.y=y;
        this.height=height;
        this.width=width;

    }
    public float getX(){
        return x;
    }
    public  float getY(){
        return y;
    }public float getHeight(){
        return height;
    }public float  getWidth(){
        return width;
    }public void setX(float x){
        this.x=x;

    }public void setY(float y){
        this.y=y;
    }public void setWidth(float setWidth){
        this.width=width;
    }public void setHeight(float setHeight){
        this.height=height;
    }
}
