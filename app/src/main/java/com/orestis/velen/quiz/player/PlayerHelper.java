package com.orestis.velen.quiz.player;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PlayerHelper {

    private static final String PLAYER = "player";

    public static Intent addPlayerToIntent(Intent intent, Player player) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String playerInfo = gson.toJson(player);
        intent.putExtra(PLAYER, playerInfo);
        return intent;
    }

    public static Player getPlayerFromIntent(Intent intent) {
        Gson gson = new Gson();
        String playerInfo = intent.getStringExtra(PLAYER);
        return gson.fromJson(playerInfo, Player.class);
    }
}
