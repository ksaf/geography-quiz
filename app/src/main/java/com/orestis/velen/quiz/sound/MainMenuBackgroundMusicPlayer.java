package com.orestis.velen.quiz.sound;

import android.content.Context;
import android.media.MediaPlayer;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.login.localStorage.LocalStorageManager;

public class MainMenuBackgroundMusicPlayer {
    private final MediaPlayer mediaPlayer;
    private final LocalStorageManager localStorageManager;
    private boolean isPaused = false;
    private final static float MAX_VOLUME = 0.15f;

    public MainMenuBackgroundMusicPlayer(Context context) {
        localStorageManager = new LocalStorageManager(context);
        mediaPlayer = MediaPlayer.create(context, R.raw.main_menu_background_music_4);
        setVolume(localStorageManager.getMusicVolumeSettings());
        mediaPlayer.setLooping(true);
    }

    public void setVolume(float volume) {
        mediaPlayer.setVolume(volume * MAX_VOLUME, volume * MAX_VOLUME);
    }

    public void start() {
        if(localStorageManager.getMusicSettings() && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void resume() {
        if(localStorageManager.getMusicSettings() && !mediaPlayer.isPlaying() && isPaused) {
            isPaused = false;
            mediaPlayer.start();
        }
    }

    public void pause() {
        mediaPlayer.pause();
        isPaused = true;
    }

    public void release() {
        mediaPlayer.release();
    }
}
