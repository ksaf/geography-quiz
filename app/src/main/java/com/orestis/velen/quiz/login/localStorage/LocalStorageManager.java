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

    private void removeLocalStorage() {
        localStorage.edit().remove("playerID").apply();
        localStorage.edit().remove(LAST_UPDATE_TIME).apply();
    }

}
