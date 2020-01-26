package com.orestis.velen.quiz.helpPowers.shield;

import com.orestis.velen.quiz.player.Player;

import static com.orestis.velen.quiz.helpPowers.PowerType.SHIELD;

public class ShieldPowerConfig {

    private int powerLevel;
    private ShieldPowerConfigs shieldPowerLevels;

    public ShieldPowerConfig(Player player) {
        this.powerLevel = player.getPowers().get(SHIELD).getPowerLevel();
        this.shieldPowerLevels = new ShieldPowerConfigs();
    }

    public int getTurnsDuration() {
        return shieldPowerLevels.getTurnDurationForPowerLevel(powerLevel);
    }
}
