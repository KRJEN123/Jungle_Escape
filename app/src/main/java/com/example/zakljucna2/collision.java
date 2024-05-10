package com.example.zakljucna2;

import java.util.List;

public class collision {
    // Detects if the character is colliding below with any platform
    public static boolean platformCollisionBelow(character character, List<platform> platforms) {
        for (platform plat : platforms) {
            // Check if the character's bounding box intersects with the platform's top bounding box
            if (character.getX() + character.getWidth() >= plat.getX() &&
                    character.getX() <= plat.getX() + plat.getWidth() &&
                    character.getY() + character.getHeight() >= plat.getY() &&
                    character.getY() + character.getHeight() <= plat.getY() + plat.getHeight()) {
                // Collision detected below
                return true;
            }
        }
        // No collision detected below
        return false;
    }

    // Detects if the character is colliding above with any platform
    public static boolean platformCollisionAbove(character character, List<platform> platforms) {
        for (platform plat : platforms) {
            float epsilon = 10f; // Tolerance to account for minor floating-point inaccuracies
            if (character.getX() + character.getWidth() >= plat.getX() &&
                    character.getX() <= plat.getX() + plat.getWidth() &&
                    character.getY() >= plat.getY() + plat.getHeight() - epsilon &&
                    character.getY() <= plat.getY() + plat.getHeight() + epsilon) {
                // Collision detected above
                return true;
            }
        }
        // No collision detected above
        return false;
    }

    // Returns the index of the platform directly below the character
    public static int getIndexBelow(character character, List<platform> platforms) {
        for (int i = 0; i < platforms.size(); i++) {
            platform plat = platforms.get(i);
            if (character.getX() + character.getWidth() >= plat.getX() &&
                    character.getX() <= plat.getX() + plat.getWidth() &&
                    character.getY() + character.getHeight() >= plat.getY() &&
                    character.getY() + character.getHeight() <= plat.getY() + plat.getHeight()) {
                // Platform found below
                return i;
            }
        }
        // No platform below
        return -1;
    }

    // Returns the index of the platform directly above the character
    public static int getIndexAbove(character character, List<platform> platforms) {
        for (int i = 0; i < platforms.size(); i++) {
            platform plat = platforms.get(i);
            float epsilon = 10f; // Tolerance to account for minor floating-point inaccuracies
            if (character.getX() + character.getWidth() >= plat.getX() &&
                    character.getX() <= plat.getX() + plat.getWidth() &&
                    character.getY() >= plat.getY() + plat.getHeight() - epsilon &&
                    character.getY() <= plat.getY() + plat.getHeight() + epsilon) {
                // Platform found above
                return i;
            }
        }
        // No platform above
        return -1;
    }
}
