package com.orestis.velen.quiz.helpPowers.freezeTime;

import com.orestis.velen.quiz.player.Player;

import static com.orestis.velen.quiz.helpPowers.PowerType.FREEZE_TIME;

public class FreezeTimePowerConfig {

    private int powerLevel;
    private FreezeTimePowerLevels freezeTimePowerLevels;

    public FreezeTimePowerConfig(Player player) {
        this.powerLevel = player.getPowers().get(FREEZE_TIME).getPowerLevel();
        this.freezeTimePowerLevels = new FreezeTimePowerLevels();
    }

    public int getTimeDuration() {
        return freezeTimePowerLevels.getTurnDurationForPowerLevel(powerLevel);
    }
}
