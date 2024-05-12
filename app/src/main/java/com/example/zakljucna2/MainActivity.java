package com.example.zakljucna2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.*;

public class MainActivity extends AppCompatActivity {
    Sound sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       enableFullscreenWithCutout();


        setContentView(R.layout.activity_main);
        ImageView start=findViewById(R.id.start);
        ImageView options=findViewById(R.id.options);
        ImageView exit=findViewById(R.id.exit);
        ImageView soundOn=findViewById(R.id.soundOn);
        // Set desired width and height in pixels
        int desiredWidth = 500; // in pixels
        int desiredHeight = 150; // in pixels
        int soundWidth=300;
        int soundHeight=300;

        // Create new LayoutParams and set them
        ViewGroup.LayoutParams layoutParams3 = soundOn.getLayoutParams();
        ViewGroup.LayoutParams layoutParams2 = exit.getLayoutParams();
        ViewGroup.LayoutParams layoutParams1 = options.getLayoutParams();
        ViewGroup.LayoutParams layoutParams = start.getLayoutParams();
       layoutParams3.width=soundWidth;
       layoutParams3.height=soundHeight;
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
        soundOn.setLayoutParams(layoutParams3);

        boolean isSoundOn = loadSoundState();
        sound = new Sound(this, R.raw.menumusic);
        sound.setSoundState(isSoundOn);
        soundOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle sound state
                boolean currentSoundState = sound.getSoundState();
                sound.setSoundState(!currentSoundState);

                // Save the new sound state to the file
                saveSoundState(sound.getSoundState());

                // Update the image based on the new sound state
                updateSoundIcon(sound.getSoundState());
            }
        });
    start.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, Game.class);
            startActivity(intent);
            finish();
        }
    });

                // Toggle sound state


        options.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(MainActivity.this, Options.class);
            startActivity(intent);
            finish();

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

    private void updateSoundIcon(boolean isSoundOn) {
        ImageView soundOn = findViewById(R.id.soundOn);
        if (isSoundOn) {
            soundOn.setImageResource(R.drawable.soundon);
        } else {
            soundOn.setImageResource(R.drawable.soundoff);
        }
    }


}
