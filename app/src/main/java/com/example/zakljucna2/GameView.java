package com.example.zakljucna2;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class GameView extends View {
    private Bitmap spriteSheet;
    private Bitmap enemySpriteSheet;
    private Bitmap platformSheet;
    private int characterDir = 1; // 1 = Right, 0 = Left
    private List<platform> platforms;
    private character character;
    private List<Enemy> enemies = new ArrayList<>();
    private int currentFrame = 0;

    private List<Rect> leftAnimationFrames;
    private List<Rect> rightAnimationFrames;

    private List<Rect> leftEnemyFrames;
    private List<Rect> rightEnemyFrames;

    private final long frameDelay = 33; // Time between frames in milliseconds
    private Handler animationHandler;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        spriteSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.motw, options);
        enemySpriteSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.motw, options);
        platformSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.ggwp1, options);
        Log.d("GameView", "Sprite Sheet Size: " + spriteSheet.getWidth() + "x" + spriteSheet.getHeight());

        leftAnimationFrames = Arrays.asList(
                new Rect(116, 85, 144, 144), // Frame 1 (left)
                new Rect(65, 83, 92, 144), // Frame 2 (left)
                new Rect(12, 85, 40, 144) // Frame 3 (left)
        );

        rightAnimationFrames = Arrays.asList(
                new Rect(8, 158, 40, 216), // Frame 4 (right)
                new Rect(60, 156, 92, 215), // Frame 5 (right)
                new Rect(111, 158, 144, 217) // Frame 6 (right)
        );

        leftEnemyFrames = Arrays.asList(
                new Rect(424, 79, 457, 144),
                new Rect(373, 77, 404, 144),
                new Rect(321, 78, 352, 144)
        );

        rightEnemyFrames = Arrays.asList(
                new Rect(320, 151, 352, 215),
                new Rect(374, 149, 404, 215),
                new Rect(425, 150, 456, 215)
        );

        animationHandler = new Handler();
    }

    public void startLeftAnimation() {
        currentFrame = 0;
        animationHandler.postDelayed(characterRunnableLeft, frameDelay);
    }

    public void stopLeftAnimation() {
        animationHandler.removeCallbacks(characterRunnableLeft);
    }

    public void startRightAnimation() {
        currentFrame = 0;
        animationHandler.postDelayed(characterRunnableRight, frameDelay);
    }

    public void stopRightAnimation() {
        animationHandler.removeCallbacks(characterRunnableRight);
    }

    private Runnable characterRunnableLeft = new Runnable() {
        @Override
        public void run() {
            currentFrame = (currentFrame + 1) % leftAnimationFrames.size();
            invalidate();
            animationHandler.postDelayed(this, frameDelay);
        }
    };

    private Runnable characterRunnableRight = new Runnable() {
        @Override
        public void run() {
            currentFrame = (currentFrame + 1) % rightAnimationFrames.size();
            invalidate();
            animationHandler.postDelayed(this, frameDelay);
        }
    };

    private Runnable enemyUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            updateAllEnemies();
            animationHandler.postDelayed(this, frameDelay);
        }
    };

    private Runnable enemyRunnableLeft = new Runnable() {
        @Override
        public void run() {
            for (Enemy enemy : enemies) {
                if (enemy.getDirection() == 2) {
                    enemy.setCurrentFrame((enemy.getCurrentFrame() + 1) % leftEnemyFrames.size());
                }
            }
            invalidate();
            animationHandler.postDelayed(this, frameDelay);
        }
    };

    private Runnable enemyRunnableRight = new Runnable() {
        @Override
        public void run() {
            for (Enemy enemy : enemies) {
                if (enemy.getDirection() == 1) {
                    enemy.setCurrentFrame((enemy.getCurrentFrame() + 1) % rightEnemyFrames.size());
                }
            }
            invalidate();
            animationHandler.postDelayed(this, frameDelay);
        }
    };

    public void startEnemy() {
        animationHandler.post(enemyUpdateRunnable);
        startEnemyLeft();
        startEnemyRight();
    }

    public void stopEnemy() {
        animationHandler.removeCallbacks(enemyUpdateRunnable);
        stopEnemyLeft();
        stopEnemyRight();
    }

    public void startEnemyLeft() {
        animationHandler.postDelayed(enemyRunnableLeft, frameDelay);
    }

    public void stopEnemyLeft() {
        animationHandler.removeCallbacks(enemyRunnableLeft);
    }

    public void startEnemyRight() {
        animationHandler.postDelayed(enemyRunnableRight, frameDelay);
    }

    public void stopEnemyRight() {
        animationHandler.removeCallbacks(enemyRunnableRight);
    }

    public void setPlatforms(List<platform> platforms) {
        this.platforms = platforms;
    }

    public void setCharacterDir(int characterDir) {
        this.characterDir = characterDir;
    }

    public void setCharacter(character character) {
        this.character = character;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public void updateAllEnemies() {
        for (Enemy enemy : enemies) {
            updateEnemyPos(enemy);
        }
        invalidate(); // Trigger redraw to reflect all enemy movements
    }

    public void updateEnemyPos(Enemy enemy) {
        if (enemy == null) return;

        float newX = enemy.getX();
        int enemyDir = enemy.getDirection();

        if (enemyDir == 1) {
            newX += 10;
            if (newX >= enemy.getEndingPosition()) {
                enemyDir = 2;
                enemy.setDirection(enemyDir);
            } else {
                enemy.setX(newX);
            }
        } else {
            newX -= 10;
            if (newX <= enemy.getStartingPosition()) {
                enemyDir = 1;
                enemy.setDirection(enemyDir);
            } else {
                enemy.setX(newX);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw each enemy
        for (Enemy enemy : enemies) {
            Rect sourceRect;
            if (enemy.getDirection() == 1) {
                sourceRect = rightEnemyFrames.get(enemy.getCurrentFrame() % rightEnemyFrames.size());
            } else {
                sourceRect = leftEnemyFrames.get(enemy.getCurrentFrame() % leftEnemyFrames.size());
            }

            Rect destRect = new Rect(
                    (int) enemy.getX(),
                    (int) enemy.getY(),
                    (int) (enemy.getX() + enemy.getWidth()),
                    (int) (enemy.getY() + enemy.getHeight())
            );

            canvas.drawBitmap(enemySpriteSheet, sourceRect, destRect, null);
        }

        // Draw platforms
        if (platformSheet != null && platforms != null) {
            for (platform platform : platforms) {
                Rect sourceRect = new Rect(20, 25, 405, 113);

                Rect destRect = new Rect(
                        (int) platform.getX(),
                        (int) platform.getY(),
                        (int) (platform.getX() + platform.getWidth()),
                        (int) (platform.getY() + platform.getHeight())
                );

                canvas.drawBitmap(platformSheet, sourceRect, destRect, null);
            }
        }

        // Draw the character
        if (character != null && spriteSheet != null) {
            Rect sourceRect;
            if (characterDir == 2) {
                sourceRect = rightAnimationFrames.get(currentFrame);
            } else {
                sourceRect = leftAnimationFrames.get(currentFrame);
            }

            Rect destRect = new Rect(
                    (int) character.getX(),
                    (int) character.getY(),
                    (int) (character.getX() + character.getWidth()),
                    (int) (character.getY() + character.getHeight())
            );

            canvas.drawBitmap(spriteSheet, sourceRect, destRect, null);
        }
    }
}
