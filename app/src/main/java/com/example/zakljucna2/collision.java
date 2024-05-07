package com.example.zakljucna2;

import java.util.List;

public class collision {
    public static boolean platformCollisionBelow(character character, List<platform> platforms){
        for (platform plat : platforms) {
            // Check if the character's bounding box intersects with the platform's bounding box
            if (character.getX() + character.getWidth() >= plat.getX() &&
                    character.getX() <= plat.getX() + plat.getWidth() &&
                    character.getY() <= plat.getY() + plat.getHeight() &&  // Character's top edge is below the platform's bottom edge
                    character.getY() + character.getHeight() > plat.getY()) {
                // Collision detected
                return true;
            }
        }
        // No collision detected
        return false;
    }
    public static boolean platformCollisionAbove(character character,List<platform>platforms){
        for (platform plat : platforms) {
            float epsilon = 10f; // Tolerance to account for minor floating-point inaccuracies
            if (character.getX() + character.getWidth() >= plat.getX() &&
                    character.getX() <= plat.getX() + plat.getWidth() &&
                    character.getY() + character.getHeight() >= plat.getY() - epsilon &&
                    character.getY() + character.getHeight() <= plat.getY() + epsilon) {
                // This indicates the character is on the platform
                return true;
            }
        }
        return false;  // No platform above
    }



    }













