package com.example.zakljucna2;

import java.util.List;

public class platform{
    private float x;
    private float y;
    private float width;
    private float height;
    public platform(float x,float y,float width,float height){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;


    }

    public float getHeight() {
        return height;
    }
    public float getWidth(){
        return width;
    }
    public float getX(){
        return x;

    }public float getY(){
        return y;
    }
    public static int getIndexBelow(character character, List<platform> platforms){
        for(int i=0;i<platforms.size();i++){
            if (character.getX() + character.getWidth() >= platforms.get(i).getX() &&
                    character.getX() <= platforms.get(i).getX() + platforms.get(i).getWidth() &&
                    character.getY() <= platforms.get(i).getY() + platforms.get(i).getHeight() &&  // Character's top edge is below the platform's bottom edge
                    character.getY() + character.getHeight() > platforms.get(i).getY()) {
                // Collision detected
                return i;
            }



        }
        return 0;

    }
    public static int getIndexAbove(character character,List<platform>platforms){
        for(int i=0;i<platforms.size();i++){
            if (character.getX() + character.getWidth() >= platforms.get(i).getX() &&
                    character.getX() <= platforms.get(i).getX() + platforms.get(i).getWidth() &&
                    character.getY() + character.getHeight() <= platforms.get(i).getY()) {  // Character's bottom edge is above platform's top edge
                // Character is above the platform
                return i;
            }


        }
        return 0;

    }

}
