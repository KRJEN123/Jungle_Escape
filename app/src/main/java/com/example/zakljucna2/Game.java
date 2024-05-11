package com.example.zakljucna2;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;

import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game extends AppCompatActivity {
    private float height;
    Runnable checkPlatform;
    private Thread gameThread;
    private Enemy enemy;
    private Enemy enemy1;
    private boolean fall;
    private int enemyPosition = 1;
    private float x;
    private float y;
    private int gravity=0;
    private GameView gameView;
    CharacterMovementTask jumpFall;
    private List<platform> platforms;
    private boolean isScheduled = false;
    private boolean falling;
    private boolean jumping;
    private boolean onPlatform = true;
    coin coin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        enableFullscreenWithCutout();
        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.game_view);
        int desiredWidth = 230;
        int desiredHeight = 230;
        ImageView left = findViewById(R.id.left);
        ImageView right = findViewById(R.id.right);
        ImageView space = findViewById(R.id.jump);
        ViewGroup.LayoutParams layoutParams2 = left.getLayoutParams();
        ViewGroup.LayoutParams layoutParams = space.getLayoutParams();
        ViewGroup.LayoutParams layoutParams1 = right.getLayoutParams();
        layoutParams.width=desiredWidth;
        layoutParams.height=desiredHeight;
        layoutParams2.width=desiredWidth;
        layoutParams2.height=desiredHeight;
        layoutParams1.width=desiredWidth;
        layoutParams1.height=desiredHeight;
        left.setLayoutParams(layoutParams2);
        space.setLayoutParams(layoutParams);
        right.setLayoutParams(layoutParams1);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        platforms = new ArrayList<>();
        platforms.add(new platform(100, 400, 300, 50));  // x, y, width, height
        platforms.add(new platform(500, 700, 400, 50));
        platforms.add(new platform(1100,700,100,50));
        platforms.add(new platform(1400, 800, 400, 50));
        platforms.add(new platform(700, 400, 150, 50));
        coin=new coin(500,700-100,100,100);
        gameView.setPlatforms(platforms);
        character character = new character(150, 80, 0, (height - 150));
        falling = character.getFalling();
        gameView.setCharacter(character);
        enemy1 = new Enemy(500, 700 - 150, 80, 150, 500, 500 + 400);
        enemy = new Enemy(920, (height - 150), 80, 150, 900, 1400);
        List<Enemy> enemies = Arrays.asList(enemy, enemy1);
        gameView.setEnemies(enemies);
        gameView.startEnemy();
        jumping=character.getJumping();
        character.setFalling(false);
        falling=false;
        character.setjumpVelocity(0);
        Handler mHandler = new Handler();
        jumpFall=new CharacterMovementTask(mHandler,character,gameView,height,platforms);
        gameView.setCoin(coin);
        Runnable coinCollision=new Runnable() {
            @Override
            public void run() {
                if(collision.characterCollisionWithCoin(character,coin)){
                    mHandler.removeCallbacksAndMessages(null);
                    levelCleared();
                }else{
                    mHandler.postDelayed(this,100);
                }
            }
        };
        mHandler.post(coinCollision);
        Runnable enemyCollision = new Runnable() {
            @Override
            public void run() {
                if (collision.characterCollisionWithEnemy(character, enemies)) {
                    // Stop the game
                    mHandler.removeCallbacksAndMessages(null);  // Clear all callbacks
                    runGameOverActivity();  // Transition to Game Over screen
                } else {

                    // Re-post the Runnable to keep checking for collisions
                    mHandler.postDelayed(this, 100); // adjust delay as needed
                }

            }
        };
        mHandler.post(enemyCollision);
        Runnable rLeft = new Runnable() {
            @Override
            public void run() {
                x = character.getX();
                character.setX(x - 15);
                gameView.invalidate();
                mHandler.postDelayed(this, 20);
                if (!character.getFalling() && !collision.platformCollisionBelow(character, platforms)) {
                    mHandler.post(checkPlatform);
                }
            }
        };

        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        gameView.setCharacterDir(1);
                        gameView.startLeftAnimation();
                        mHandler.post(rLeft);
                        break;
                    case MotionEvent.ACTION_UP:
                        gameView.stopLeftAnimation();
                        mHandler.removeCallbacks(rLeft);
                        break;
                }
                return true;
            }
        });


        Runnable rRight = new Runnable() {
            @Override
            public void run() {
                   character.setX(character.getX() + 15);
                   gameView.invalidate();
                   mHandler.postDelayed(this, 20);
                if (!character.getFalling() && !collision.platformCollisionBelow(character, platforms)) {
                    mHandler.post(checkPlatform);
                }
            }
        };

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        gameView.setCharacterDir(2);
                        gameView.startRightAnimation();
                        mHandler.post(rRight);
                        break;
                    case MotionEvent.ACTION_UP:
                        gameView.stopRightAnimation();
                        mHandler.removeCallbacks(rRight);
                        break;
                }
                return true;
            }
        });




        checkPlatform = new Runnable() {
            @Override
            public void run() {
                // Check if the character is above a platform
                if (!collision.platformCollisionBelow(character, platforms) && character.getY() + character.getHeight() < height) {
                    // If not colliding and not at ground level, start falling
                    character.setFalling(true);
                    character.setOnPlatform(false);
                    updateFalling();  // Handle the falling movement
                } else {
                    // If there is a platform below or character is at ground level
                    int index = collision.getIndexAbove(character, platforms);
                    if (index != -1) {
                        // Align character on top of the platform
                        character.setY(platforms.get(index).getY() - character.getHeight());
                        character.setFalling(false);
                        character.setOnPlatform(true);
                    } else if (character.getY() + character.getHeight() >= height) {
                        // Handle ground collision
                        character.setY(height - character.getHeight());
                        character.setFalling(false);
                        character.setOnPlatform(true);
                    }
                }

                // If still falling, continue to check
                if (character.getFalling()) {
                    mHandler.postDelayed(this, 20);  // Continue checking every 20 ms
                }
            }

            private void updateFalling() {
                // This method will update the character's vertical position while falling
                float newY = character.getY() + gravity;
                character.setY(newY);
                gameView.invalidate();
            }
        };







        space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("JumpDebug", "button clicked.");
                jump(character,jumpFall);
            }
        });



    }
    private void enableFullscreenWithCutout() {
        Window window = getWindow();

        // Fullscreen flags to cover entire screen
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        View decorView = window.getDecorView();

        // Set system UI visibility flags to manage cutout display
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        // Set WindowInsetsController to hide the notch
        WindowInsetsController insetsController = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            insetsController = decorView.getWindowInsetsController();
        }
        if (insetsController != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        }

        // Allow content to extend into the cutout area
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        window.setAttributes(layoutParams);
    }
    public void jump(character character, CharacterMovementTask jumpFall) {
        if (!character.getFalling() && !fall) {
            character.setFalling(true);
            character.setOnPlatform(false);  // Ensure character is no longer considered on a platform
            character.setjumpVelocity(character.getjumpStrenght());  // Set a strong initial upward velocity
            gravity = 0;  // Reset gravity so the initial jump is not affected
            jumpFall.start();
        }
    }
    private void runGameOverActivity() {
        // Run on the UI thread because startActivity is a UI operation
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Game.this, GameOver.class);
                startActivity(intent);
                finish();  // Optionally call finish if you don't want the user to return to this game activity
            }
        });
    }
    private void levelCleared(){
        runOnUiThread(new Runnable() {
            public void run() {

                Intent intent = new Intent(Game.this, levelCleared.class);
                startActivity(intent);
                finish();
            }
        });
    }


}
