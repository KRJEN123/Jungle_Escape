package com.example.zakljucna2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;

public class Options extends AppCompatActivity {
    private boolean isSoundOn = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       enableFullscreenWithCutout();
        setContentView(R.layout.activity_options);
        ImageView soundButton = findViewById(R.id.sound);
        ImageView back=findViewById(R.id.back);
        int desiredWidth = 500;
        int desiredHeight = 500;
       int backWidth=250;
       int backHeight=250;

        ViewGroup.LayoutParams layoutParams2 = soundButton.getLayoutParams();
        ViewGroup.LayoutParams layoutParams1 = back.getLayoutParams();
        layoutParams2.width=desiredWidth;
        layoutParams2.height=desiredHeight;
        layoutParams1.width=backWidth;
        layoutParams1.height=backHeight;
        soundButton.setLayoutParams(layoutParams2);
        back.setLayoutParams(layoutParams1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Options.this, MainActivity.class);
                startActivity(intent);


            }
        });

        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSoundButton(soundButton);
            }
        });


    }
    private void toggleSoundButton(ImageView soundButton) {
        if (isSoundOn) {
            soundButton.setImageResource(R.drawable.soundoff);
            isSoundOn = false;
        } else {
            soundButton.setImageResource(R.drawable.soundon);
            isSoundOn = true;
        }
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