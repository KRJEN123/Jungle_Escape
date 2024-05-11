package com.example.zakljucna2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.WindowInsets;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    Sound sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       enableFullscreenWithCutout();
       sound = new Sound(this, R.raw.menumusic);




        setContentView(R.layout.activity_main);
        ImageView start=findViewById(R.id.start);
        ImageView options=findViewById(R.id.options);
        ImageView exit=findViewById(R.id.exit);

        // Set desired width and height in pixels
        int desiredWidth = 500; // in pixels
        int desiredHeight = 150; // in pixels

        // Create new LayoutParams and set them
        ViewGroup.LayoutParams layoutParams2 = exit.getLayoutParams();
        ViewGroup.LayoutParams layoutParams1 = options.getLayoutParams();
        ViewGroup.LayoutParams layoutParams = start.getLayoutParams();
       layoutParams2.width=desiredWidth;
       layoutParams2.height=desiredHeight;
        layoutParams1.width=desiredWidth;
       layoutParams1.height=desiredHeight;
        layoutParams.width = desiredWidth;
        layoutParams.height = desiredHeight;

        // Apply the new LayoutParams to the ImageButton
        start.setLayoutParams(layoutParams);
        options.setLayoutParams(layoutParams1);
        exit.setLayoutParams(layoutParams2);

    start.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, Game.class);
            startActivity(intent);
            sound.pauseMusic();
        }
    });
    options.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(MainActivity.this, Options.class);
            startActivity(intent);
            sound.pauseMusic();

        }
    });
    exit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finishAffinity(); // Close all activities
            System.exit(0); // E

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
}
