package com.orestis.velen.quiz.login.localStorage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orestis.velen.quiz.leveling.StartingPlayerStats;
import com.orestis.velen.quiz.player.Player;

import java.util.Date;

public class LocalStorageManager {

    private static final String LAST_UPDATE_TIME = "com.orestis.velen.quiz.lastUpdateTime";
    private static final String XP_BOOST_ENABLED_TIME = "com.orestis.velen.quiz.xpBoostEnabledTime";
    private static final String MUSIC_SETTING = "com.orestis.velen.quiz.musicSetting";
    private static final String MUSIC_VOLUME_SETTING = "com.orestis.velen.quiz.musicVolumeSetting";
    private static final String SOUND_SETTING = "com.orestis.velen.quiz.soundSetting";
    private static final String SOUND_VOLUME_SETTING = "com.orestis.velen.quiz.soundVolumeSetting";
    private static final String AUTO_LOGIN_ATTEMPTS = "com.orestis.velen.quiz.autoLoginAttempts";
    private SharedPreferences localStorage;

    public LocalStorageManager(Context context) {
        localStorage = context.getSharedPreferences("com.orestis.velen.quiz", Context.MODE_PRIVATE);
    }

    public void updatePlayer(Player player) {
        SharedPreferences.Editor prefsEditor = localStorage.edit();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(player);
        prefsEditor.putString("playerID", json);
        prefsEditor.apply();
        saveLastUpdateTime();
    }

    public Player getPlayer() {
        Gson gson = new Gson();
        String json = localStorage.getString("playerID", StartingPlayerStats.getStartingPlayer());
        return gson.fromJson(json, Player.class);
    }

    public long getLastUpdateTime() {
        return localStorage.getLong(LAST_UPDATE_TIME, 0L);
    }

    private void saveLastUpdateTime() {
        localStorage.edit().putLong(LAST_UPDATE_TIME, new Date().getTime()).apply();
    }

    public void saveXpBoostEnabledTime() {
        localStorage.edit().putLong(XP_BOOST_ENABLED_TIME, new Date().getTime()).apply();
    }

    public long getXpBoostEnabledTime() {
        return localStorage.getLong(XP_BOOST_ENABLED_TIME, 0);
    }

    public void removeLocalStorage() {
        localStorage.edit().remove("playerID").apply();
        localStorage.edit().remove(LAST_UPDATE_TIME).apply();
    }

    public void setSoundSetting(boolean enabled) {
        localStorage.edit().putBoolean(SOUND_SETTING, enabled).apply();
    }

    public void setSoundVolumeSetting(float volume) {
        localStorage.edit().putFloat(SOUND_VOLUME_SETTING, volume).apply();
    }

    public void setMusicSetting(boolean enabled) {
        localStorage.edit().putBoolean(MUSIC_SETTING, enabled).apply();
    }

    public void setMusicVolumeSetting(float volume) {
        localStorage.edit().putFloat(MUSIC_VOLUME_SETTING, volume).apply();
    }

    public boolean getSoundSettings() {
        return localStorage.getBoolean(SOUND_SETTING, true);
    }

    public float getSoundVolumeSettings() {
        return localStorage.getFloat(SOUND_VOLUME_SETTING, 1.0f);
    }

    public boolean getMusicSettings() {
        return localStorage.getBoolean(MUSIC_SETTING, true);
    }

    public float getMusicVolumeSettings() {
        return localStorage.getFloat(MUSIC_VOLUME_SETTING, 1.0f);
    }

    public int getAutoLoginAttempts() {
        return localStorage.getInt(AUTO_LOGIN_ATTEMPTS, 0);
    }

    public void resetAutoLoginAttempts() {
        localStorage.edit().putInt(AUTO_LOGIN_ATTEMPTS, 0).apply();
    }

    public void incrementAutoLoginAttempts() {
        localStorage.edit().putInt(AUTO_LOGIN_ATTEMPTS, getAutoLoginAttempts() + 1).apply();
    }
}
