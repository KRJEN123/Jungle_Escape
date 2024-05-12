package com.example.zakljucna2;

import android.content.Context;
import android.media.MediaPlayer;

public class Sound {
    private MediaPlayer mediaPlayer;
    private boolean sound=true;
    private boolean isSoundOn = true; // Default value

    // Constructor
    public Sound(Context context, int soundResourceId) {
        mediaPlayer = MediaPlayer.create(context, soundResourceId);
    }

    public void playMusic() {
        if (sound && mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            mediaPlayer.setLooping(true); // Loop music if desired
        }
    }

    public void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }
    public boolean getSoundState(){
        return isSoundOn;

    }
    public void setSound(boolean sound){
        this.sound=sound;

    }

    public void setSoundState(boolean soundState) {
        isSoundOn = soundState;
        this.sound = soundState;  // Update the internal sound state based on the toggle
        if (isSoundOn) {
            playMusic();
        } else {
            pauseMusic();
        }
    }


    public void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
