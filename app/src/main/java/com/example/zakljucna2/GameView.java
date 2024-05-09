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








public class GameView extends View {

    private Bitmap spriteSheet;


    private Bitmap platformSheet;// Sprite sheet bitmap
    private List<platform> platforms; // List of platforms
    private character character;
    private Enemy enemy;
    private boolean movingRight = true;// Character object


    private int currentFrame = 0;
    private int currentEnemyFrame=0;

    private int characterDir;
    private int enemyDir;
    private List<Rect> leftAnimationFrames;
    private List<Rect> rightAnimationFrames;

    private List<Rect> leftEnemyFrames;
    private List<Rect> rightEnemyFrames;


    private long frameDelay = 33; // Time between frames in milliseconds
    private Handler animationHandler;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);



        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false; // Prevents automatic scaling based on screen density
        spriteSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.motw, options);

        platformSheet=BitmapFactory.decodeResource(context.getResources(), R.drawable.platform, options);
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
        leftEnemyFrames=Arrays.asList(
                new Rect(424,79,457,144),
                new Rect(373,77,404,144),
                new Rect(321,78,352,144)



        );
        rightEnemyFrames=Arrays.asList(
                new Rect(320,151,352,215),
                new Rect(374,149,404,215),
                new Rect(425,150,456,215)



        );



        // Initialize the animation handler
        animationHandler = new Handler();
    }
    public void startLeftAnimation() {
        currentFrame = 0; // Reset frame index
        animationHandler.postDelayed(animationRunnableLeft, frameDelay);
    }

    public void stopLeftAnimation() {
        animationHandler.removeCallbacks(animationRunnableLeft);
    }

    public void startRightAnimation() {
        currentFrame = 0; // Reset frame index
        animationHandler.postDelayed(animationRunnableRight, frameDelay);
    }

    public void stopRightAnimation() {
        animationHandler.removeCallbacks(animationRunnableRight);
    }
    public void startEnemyLeft(){
        currentFrame=0;
        animationHandler.postDelayed(enemyRunnableLeft,frameDelay);


    }
public void startEnemyRight(){
        currentFrame=0;
        animationHandler.postDelayed(getEnemyRunnableRight,frameDelay);

}
public void stopEnemyLeft(){animationHandler.removeCallbacks(animationRunnableLeft);}
public void stopEnemyRight(){animationHandler.removeCallbacks(animationRunnableRight);}



    private Runnable enemyRunnableLeft=new Runnable() {
        @Override
        public void run() {
            currentEnemyFrame=(currentEnemyFrame+1)%leftEnemyFrames.size();
            invalidate();
            animationHandler.postDelayed(this,frameDelay);


        }
    };
    private Runnable getEnemyRunnableRight=new Runnable() {
        @Override
        public void run() {
            currentEnemyFrame=(currentEnemyFrame+1)%rightEnemyFrames.size();
            invalidate();
            animationHandler.postDelayed(this,frameDelay);


        }
    };

    private Runnable animationRunnableLeft = new Runnable() {
        @Override
        public void run() {
            currentFrame = (currentFrame + 1) % leftAnimationFrames.size();
            invalidate(); // Trigger re-draw
            animationHandler.postDelayed(this, frameDelay); // Schedule next frame
        }
    };

    private Runnable animationRunnableRight = new Runnable() {
        @Override
        public void run() {
            currentFrame = (currentFrame + 1) % rightAnimationFrames.size();

            invalidate(); // Trigger re-draw
            animationHandler.postDelayed(this, frameDelay); // Schedule next frame
        }
    };


    public void setPlatforms(List<platform> platforms) {
        this.platforms = platforms;
    }



    public int setCharacterDir(int characterDir){
        this.characterDir=characterDir;
        return characterDir;

    }


    public void setCharacter(character character) {
        this.character = character;
    }
    public void setEnemy(Enemy enemy){this.enemy=enemy;}



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (enemy != null && spriteSheet != null) {
            Rect sourceRect;
            if (enemyDir == 1) { // Left direction
                sourceRect = leftEnemyFrames.get(currentFrame);
            } else { // Right direction
                sourceRect = rightEnemyFrames.get(currentFrame);
            }

            Rect destRect = new Rect(
                    (int) enemy.getX(),
                    (int) enemy.getY(),
                    (int) (enemy.getX() + enemy.getWidth()),
                    (int) (enemy.getY() + enemy.getHeight())
            );

            canvas.drawBitmap(spriteSheet, sourceRect, destRect, null);

        }


        if (platformSheet != null && platforms != null) {
            for (platform platform : platforms) {

                Rect sourceRect = new Rect(417, 552,912 ,680  );


                Rect destRect = new Rect(
                        (int) platform.getX(),
                        (int) platform.getY(),
                        (int) (platform.getX() + platform.getWidth()),
                        (int) (platform.getY() + platform.getHeight())
                );

                // Draw the platform from the sprite sheet
                canvas.drawBitmap(platformSheet, sourceRect, destRect, null);
            }
        }

        // Draw character as a sprite
        if (character != null && spriteSheet != null) {
            Rect sourceRect;
            if (characterDir == 1) { // Left direction
                sourceRect = leftAnimationFrames.get(currentFrame);
            } else { // Right direction
                sourceRect = rightAnimationFrames.get(currentFrame);
            }

            Rect destRect = new Rect(
                    (int) character.getX(),
                    (int) character.getY(),
                    (int) (character.getX() + character.getWidth()),
                    (int) (character.getY() + character.getHeight())
            );

            canvas.drawBitmap(spriteSheet, sourceRect, destRect, null);
        }

        // Define the destination rectangle where the sprite should be drawn



    }
}

        // Draw platforms


