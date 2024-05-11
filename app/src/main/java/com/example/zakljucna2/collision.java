package com.example.zakljucna2;

import java.util.List;

public class collision {
    // Detects if the character is colliding below with any platform
    public static boolean platformCollisionBelow(character character, List<platform> platforms) {
        final int tolerance = 0;  // Set tolerance in pixels, adjust based on gameplay needs

        for (platform plat : platforms) {
            // Check if the character's bounding box intersects with the platform's top bounding box
            if (character.getX() + character.getWidth() >= plat.getX() &&
                    character.getX() <= plat.getX() + plat.getWidth() &&
                    character.getY() + character.getHeight() >= plat.getY() - tolerance &&  // Allow for some tolerance above the platform
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
            float epsilon = 10; // Tolerance to account for minor floating-point inaccuracies
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
    public static int  platformCollisionSide(character character, List<platform> platforms) {
        for (platform plat : platforms) {
            if (character.getY() + character.getHeight() > plat.getY() &&  // Check vertical overlap
                    character.getY() < plat.getY() + plat.getHeight()) {

                // Check collision on the left side of the character
                if (character.getX() < plat.getX() + plat.getWidth() &&
                        character.getX() > plat.getX() &&
                        character.getX() + character.getWidth() > plat.getX() + plat.getWidth()) {
                    return -1;  // Collision on the left
                }

                // Check collision on the right side of the character
                if (character.getX() + character.getWidth() > plat.getX() &&
                        character.getX() < plat.getX() &&
                        character.getX() + character.getWidth() < plat.getX() + plat.getWidth()) {
                    return 1;  // Collision on the right
                }
            }
        }
        return 0;  // No side collision
    }
    public static int getPlatformIndexFromSideCollision(character character, List<platform> platforms, int side) {
        for (int i = 0; i < platforms.size(); i++) {
            platform plat = platforms.get(i);
            if (character.getY() + character.getHeight() > plat.getY() &&  // Check vertical overlap
                    character.getY() < plat.getY() + plat.getHeight()) {

                if (side == -1) {  // Check for left side collision
                    if (character.getX() < plat.getX() + plat.getWidth() &&
                            character.getX() > plat.getX() &&
                            character.getX() + character.getWidth() > plat.getX() + plat.getWidth()) {
                        return i;  // Collision on the left
                    }
                } else if (side == 1) {  // Check for right side collision
                    if (character.getX() + character.getWidth() > plat.getX() &&
                            character.getX() < plat.getX() &&
                            character.getX() + character.getWidth() < plat.getX() + plat.getWidth()) {
                        return i;  // Collision on the right
                    }
                }
            }
        }
        return -1;  // No collision from the specified side
    }

    // Other existing methods...
}

