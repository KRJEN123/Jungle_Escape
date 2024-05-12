package com.example.zakljucna2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class levelCleared extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_cleared);
        enableFullscreenWithCutout();
        ImageView next=findViewById(R.id.next);
        ImageView home=findViewById(R.id.home);
        int desiredWidth = 300; // in pixels
        int desiredHeight = 300;
        ViewGroup.LayoutParams layoutParams2 = next.getLayoutParams();
        ViewGroup.LayoutParams layoutParams1 = home.getLayoutParams();
        layoutParams2.width=desiredWidth;
        layoutParams2.height=desiredHeight;
        layoutParams1.width=desiredWidth;
        layoutParams1.height=desiredHeight;
        next.setLayoutParams(layoutParams2);
        home.setLayoutParams(layoutParams1);
next.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       int level=readLevelNumber();
        launchActivityBasedOnLevel(level);



    }
});

home.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(levelCleared.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
});




    }
    private void enableFullscreenWithCutout () {
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
    private int readLevelNumber() {
        File file = new File(getFilesDir(), "playerLevel.txt");
        if (!file.exists()) {
            Log.e("FileRead", "File does not exist");
            return -1; // File does not exist
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine(); // Read the first line
            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

            // Assuming the file contains a simple integer as "Level number: X"
            return Integer.parseInt(line.split(": ")[1].trim());
        } catch (IOException e) {
            Log.e("FileRead", "Error reading the file", e);
            return -1;
        } catch (NumberFormatException e) {
            Log.e("FileRead", "Error parsing the level number", e);
            return -1;
        }
    }
    private void launchActivityBasedOnLevel(int levelNumber) {
        Intent intent;
        switch (levelNumber) {
            case 1:
                intent = new Intent(levelCleared.this, Game.class);
                break;
            case 2:
                intent = new Intent(levelCleared.this, Game2.class);
                break;
            // Add more cases as needed
            case 3:
                intent = new Intent(levelCleared.this, Game3.class);
                break;
            default:
                intent = new Intent(levelCleared.this, MainActivity.class); // Default or error handling
                break;
        }
        startActivity(intent);
        finish();
    }
}