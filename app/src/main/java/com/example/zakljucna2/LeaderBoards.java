package com.example.zakljucna2;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LeaderBoards extends AppCompatActivity {

    private static final String TAG = "LeaderBoards";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_boards);
        enableFullscreenWithCutout();
        LinearLayout levelsContainer = findViewById(R.id.levelsContainer);
        ImageView back = findViewById(R.id.back);
        int backWidth = 250;
        int backHeight = 250;
        ViewGroup.LayoutParams layoutParams1 = back.getLayoutParams();
        layoutParams1.width = backWidth;
        layoutParams1.height = backHeight;
        back.setLayoutParams(layoutParams1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderBoards.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        long bestTime = Long.MAX_VALUE;

        for (int i = 1; i <= 10; i++) {
            LinearLayout levelLayout = new LinearLayout(this);
            levelLayout.setOrientation(LinearLayout.HORIZONTAL);
            levelLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            TextView levelTextView = new TextView(this);
            levelTextView.setText("Level " + i + ":");
            levelTextView.setTextSize(24);
            levelTextView.setTypeface(null, Typeface.BOLD); // Set text to bold
            levelTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
            ));

            EditText valueEditText = new EditText(this);
            valueEditText.setHint("Value");
            valueEditText.setFocusable(false); // Make EditText read-only
            valueEditText.setClickable(false); // Make EditText not clickable
            valueEditText.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
            ));

            // Reduce the space between the TextView and EditText
            ((LinearLayout.LayoutParams) levelTextView.getLayoutParams()).setMargins(16, 0, 0, 0);
            ((LinearLayout.LayoutParams) valueEditText.getLayoutParams()).setMargins(16, 0, 0, 0);

            String elapsedTimeFileName = "elapsedTime" + i + ".txt";
            String elapsedTime = readFile(elapsedTimeFileName);
            if (!elapsedTime.isEmpty()) {
                valueEditText.setText(elapsedTime.trim());
                try {
                    long time = Long.parseLong(elapsedTime.trim());
                    Log.d(TAG, "Parsed time for " + elapsedTimeFileName + ": " + time);
                    if (time < bestTime) {
                        bestTime = time;
                        Log.d(TAG, "New best time found: " + bestTime);
                    }
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Invalid time format in file: " + elapsedTimeFileName, e);
                }
            } else {
                valueEditText.setText("No data");
            }

            levelLayout.addView(levelTextView);
            levelLayout.addView(valueEditText);

            levelsContainer.addView(levelLayout);
        }

        // Add best time below the last level
        LinearLayout bestTimeLayout = new LinearLayout(this);
        bestTimeLayout.setOrientation(LinearLayout.HORIZONTAL);
        bestTimeLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        TextView bestTimeTextView = new TextView(this);
        bestTimeTextView.setText("Best Time:");
        bestTimeTextView.setTextSize(24);
        bestTimeTextView.setTypeface(null, Typeface.BOLD); // Set text to bold
        bestTimeTextView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        ));

        EditText bestTimeEditText = new EditText(this);
        bestTimeEditText.setHint("Best Time");
        bestTimeEditText.setFocusable(false); // Make EditText read-only
        bestTimeEditText.setClickable(false); // Make EditText not clickable
        bestTimeEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
        ));
        if (bestTime != Long.MAX_VALUE) {
            bestTimeEditText.setText(String.valueOf(bestTime));
            Log.d(TAG, "Best time set to: " + bestTime);
        } else {
            bestTimeEditText.setText("No data");
            Log.d(TAG, "No valid best time found.");
        }

        bestTimeLayout.addView(bestTimeTextView);
        bestTimeLayout.addView(bestTimeEditText);

        levelsContainer.addView(bestTimeLayout);
    }

    private String readFile(String fileName) {
        File file = new File(getFilesDir(), fileName);
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            Log.e(TAG, "Error reading file: " + fileName, e);
        }

        return content.toString();
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
