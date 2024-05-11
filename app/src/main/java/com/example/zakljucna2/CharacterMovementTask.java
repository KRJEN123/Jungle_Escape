package com.example.zakljucna2;

import android.os.Handler;
import android.util.Log;

import java.util.List;

public class CharacterMovementTask {
    private Runnable rCharacterMovement;
    private boolean isScheduled = false;
    private Handler mHandler;
    private character character; // Assuming character is a class you use to manage character state
    private GameView gameView; // This needs to be passed in
    private float gravity;
    private List<platform> platforms;
    private float height; // Assuming height is your game area's height

    public CharacterMovementTask(Handler handler, character character, GameView gameView, float gameHeight, List<platform> platforms) {
        this.mHandler = handler;
        this.character = character;
        this.gameView = gameView;
        this.height = gameHeight;
        this.platforms=platforms;


        this.rCharacterMovement = new Runnable() {
            @Override
            public void run() {
                float newY = character.getY();
                if (character.getFalling()) {
                    if (character.getjumpVelocity() > 0) {
                        character.setjumpVelocity(character.getjumpVelocity() - character.getWeight());
                        // Gravity is reset when the jump starts, ensure it only affects after jump peak is reached
                    } else {
                        gravity += 2;  // Gravity takes over once upward velocity is depleted
                    }

                    newY = character.getY() - character.getjumpVelocity() + gravity;

                    // Check for ground or platform collisions
                    if (newY >= height - character.getHeight()) {
                        newY = height - character.getHeight();
                        resetMovement();
                    } else {
                        handleCollisions(newY);
                    }
                }

                character.setY(newY);
                gameView.invalidate();

                if (isScheduled) {
                    mHandler.postDelayed(this, 11);
                }
            }

            private void handleCollisions(float newY) {
                if (collision.platformCollisionAbove(character, platforms)) {
                    int index = collision.getIndexAbove(character, platforms);
                    if (index != -1) {
                        newY = platforms.get(index).getY() + platforms.get(index).getHeight();
                        resetMovement();
                        character.setFalling(true);
                    }
                }
                if (collision.platformCollisionBelow(character, platforms)) {
                    int index = collision.getIndexBelow(character, platforms);
                    if (index != -1) {
                        float platformTop = platforms.get(index).getY() - character.getHeight();
                        if (newY > platformTop) {  // Ensure character is moved to platform top only if they are falling onto it
                            newY = platformTop;
                        }
                        character.setY(newY);  // Update character's position immediately to avoid clipping
                        resetMovement();
                        character.setOnPlatform(true);
                        character.setFalling(false);  // Ensure falling is stopped as the character is now supported by a platform
                    }
                } else {
                    character.setOnPlatform(false);  // Character is not on any platform
                }
            }

                private void resetMovement() {
                character.setjumpVelocity(0);
                gravity = 0;
                character.setFalling(false);
                character.setOnPlatform(false);
            }



        };
    }

    public void start() {
        if (!isScheduled) {
            character.setFalling(true); // Ensure falling is set to true when starting movement
            mHandler.post(rCharacterMovement);

            isScheduled = true;
        }
    }

    public void stop() {
        mHandler.removeCallbacks(rCharacterMovement);
        isScheduled = false;
        character.setFalling(false);
    }
}
