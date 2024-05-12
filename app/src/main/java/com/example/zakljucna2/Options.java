package com.example.zakljucna2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
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
import java.io.*;

public class Options extends AppCompatActivity {

    Sound sound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       enableFullscreenWithCutout();
        setContentView(R.layout.activity_options);
        ImageView one=findViewById(R.id.one);
        ImageView two=findViewById(R.id.two);
        ImageView three=findViewById(R.id.three);
        ImageView back=findViewById(R.id.back);
        int desiredWidth = 500;
        int desiredHeight = 500;
       int backWidth=250;
       int backHeight=250;
        ViewGroup.LayoutParams layoutParams4 = three.getLayoutParams();
        ViewGroup.LayoutParams layoutParams3 = two.getLayoutParams();
        ViewGroup.LayoutParams layoutParams2 = one.getLayoutParams();
        ViewGroup.LayoutParams layoutParams1 = back.getLayoutParams();

        layoutParams4.width=backWidth;
        layoutParams4.height=backHeight;
        layoutParams3.width=backWidth;
        layoutParams3.height=backHeight;
        layoutParams1.width=backWidth;
        layoutParams1.height=backHeight;
        layoutParams2.width=backWidth;
        layoutParams2.height=backHeight;
        back.setLayoutParams(layoutParams1);
        boolean isSoundOn = loadSoundState();
       saveSoundState(isSoundOn);
        one.setLayoutParams(layoutParams2);
        two.setLayoutParams(layoutParams3);
        three.setLayoutParams(layoutParams4);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Options.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Options.this, Game.class);
                startActivity(intent);
                finish();


            }
        });
two.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Options.this, Game2.class);
        startActivity(intent);
        finish();
    }
});
three.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Options.this, Game3.class);
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
    private boolean loadSoundState() {
        FileInputStream fis = null;
        try {
            fis = openFileInput("sound_state.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String state = br.readLine();
            Log.d("SoundState", "Read from file: " + state);  // Add logging to see what's read
            return Boolean.parseBoolean(state);
        } catch (FileNotFoundException e) {
            // If the file doesn't exist, assume sound is on by default
            Log.d("SoundState", "File not found, defaulting to true");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveSoundState(boolean state) {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput("sound_state.txt", MODE_PRIVATE);
            fos.write(Boolean.toString(state).getBytes());
            Log.d("SoundState", "Saved state: " + state);  // Log the saved state
        } catch (Exception e) {
            Log.e("SoundState", "Failed to save state", e);
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    }