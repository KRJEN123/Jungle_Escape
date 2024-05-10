package com.example.zakljucna2;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;

import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game extends AppCompatActivity {
    private float height;
    private Enemy enemy;
    private Enemy enemy1;
    private int enemyPosition = 1;
    private float x;
    private float y;

    private GameView gameView;
    private List<platform> platforms;

    private boolean falling=false;
    private boolean onPlatform = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableFullscreenWithCutout();
        setContentView(R.layout.activity_game);
        gameView = findViewById(R.id.game_view);

        Button left = findViewById(R.id.left);
        Button right = findViewById(R.id.right);
        Button space = findViewById(R.id.jump);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        platforms = new ArrayList<>();
        platforms.add(new platform(100, 500, 300, 50));  // x, y, width, height
        platforms.add(new platform(500, 800, 400, 50));
        platforms.add(new platform(1400, 600, 400, 60));
        platforms.add(new platform(700, 500, 150, 50));
        gameView.setPlatforms(platforms);
        character character = new character(150, 80, 0, (height - 150));
        falling = character.getFalling();
        gameView.setCharacter(character);
        enemy1 = new Enemy(500, 800 - 150, 80, 150, 500, 500 + 400);
        enemy = new Enemy(920, (height - 150), 80, 150, 900, 1400);
        List<Enemy> enemies = Arrays.asList(enemy, enemy1);
        gameView.setEnemies(enemies);
        gameView.startEnemy();

        Handler mHandler = new Handler();

        Runnable rLeft = new Runnable() {
            @Override
            public void run() {
                x = character.getX();
                character.setX(x - 15);
                gameView.invalidate();
                mHandler.postDelayed(this, 20);
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
                x = character.getX();
                character.setX(x + 15);
                gameView.invalidate();
                mHandler.postDelayed(this, 20);
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

        Runnable rFallCheck = new Runnable() {
            @Override
            public void run() {
                // Check if the character should be falling
                if (!collision.platformCollisionBelow(character, platforms) && character.getY() + character.getHeight() < height) {
                    falling = true;
                } else {
                    falling = false;
                }

                // Update the Y position of the character if falling
                if (falling) {
                    float newY = character.getY() + character.getWeight(); // Adjusted falling speed for a smooth fall
                    if (newY >= height - character.getHeight()) {
                        // Ground collision
                        character.setY(height - character.getHeight());
                        falling = false;
                        onPlatform = false;
                        character.setjumpVelocity(0);
                    } else if (collision.platformCollisionBelow(character, platforms)) {
                        // Platform collision from below
                        int index = collision.getIndexBelow(character, platforms);
                        if (index != -1) {
                            character.setY(platforms.get(index).getY() - character.getHeight());
                            falling = false;
                            onPlatform = true;
                            character.setjumpVelocity(0);
                        }
                    } else {
                        // Otherwise, continue falling
                        character.setY(newY);
                    }

                    gameView.invalidate();
                }

                mHandler.postDelayed(this, 5);
            }
        };

        Runnable rJump = new Runnable() {
            @Override
            public void run() {
                if (falling) {
                    x = character.getX();
                    y = character.getY();

                    // Update jump velocity and calculate new Y position
                    character.setjumpVelocity(character.getjumpVelocity() - character.getWeight());
                    float newY = y - character.getjumpVelocity();

                    if (newY >= height - character.getHeight()) {
                        // Ground collision
                        falling = false;
                        onPlatform = false;
                        character.setjumpVelocity(0);
                        character.setY(height - character.getHeight());
                    } else if (collision.platformCollisionAbove(character, platforms)) {
                        // Platform collision from above
                        if (!onPlatform) {
                            character.setjumpVelocity(0);
                            falling = true;
                            onPlatform = true;
                            int index1 = collision.getIndexAbove(character, platforms);
                            if (index1 != -1) {
                                character.setY(platforms.get(index1).getY() - character.getHeight());
                            }
                        } else {
                            character.setY(newY);
                        }
                    } else if (collision.platformCollisionBelow(character, platforms)) {
                        // Platform collision from below
                        character.setjumpVelocity(0);
                        int index = collision.getIndexBelow(character, platforms);
                        if (index != -1) {
                            character.setY(platforms.get(index).getY() - character.getHeight());
                        }
                        falling = false;
                        onPlatform = true;
                    } else {
                        // Otherwise, continue jumping
                        character.setY(newY);
                        onPlatform = false;
                    }

                    gameView.invalidate();
                    mHandler.postDelayed(this, 5);
                    Log.d("GG", "Falling: " + falling);
                    Log.d("EE", "Jump Velocity: " + character.getjumpVelocity());
                }
            }
        };

        space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!falling) {
                    falling = true;
                    onPlatform = false;
                    character.setjumpVelocity(character.getjumpStrenght());
                    mHandler.post(rJump);
                }
            }
        });

        mHandler.post(rFallCheck); // Make sure this starts early


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
}
