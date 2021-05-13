package com.orestis.velen.quiz.skillUpgrades;

import android.content.Context;
import android.view.View;

import com.orestis.velen.quiz.helpPowers.Power;
import com.orestis.velen.quiz.player.Player;

abstract class SkillUpgradesItem {

    private SkillPlusListener skillPlusListener;
    private SkillMinusListener skillMinusListener;
    private int skillLevel;
    private int skillLevelTimesIncreased;
    private boolean canBeIncremented;
    private Player player;

    abstract public int getBackGroundColorId();

    abstract public int getIconDrawableId();

    abstract public String getSkillName();

    public int getSkillLevel() {
        return skillLevel;
    }

    public int getSkillLevelTimesIncreased() {
        return skillLevelTimesIncreased;
    }

    public boolean isMaxLevel() {
        return skillLevel == player.getPowers().get(getSkillName()).maxLevel();
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.skillLevel = player.getPowers().get(getSkillName()).getPowerLevel();
    }

    protected Power getPower() {
        return player.getPowers().get(getSkillName());
    }

    public void incrementSkillLevel() {
        this.skillLevel++;
        this.skillLevelTimesIncreased++;
    }

    public void decreaseSkillLevel() {
        this.skillLevel--;
        this.skillLevelTimesIncreased--;
    }

    public boolean canBeIncremented() {
        return canBeIncremented;
    }

    public void setCanBeIncremented(boolean canBeIncremented) {
        this.canBeIncremented = canBeIncremented;
    }

    public boolean canBeDecremented() {
        return this.skillLevelTimesIncreased > 0;
    }

    public void setSkillPlusListener(SkillPlusListener skillPlusListener) {
        this.skillPlusListener = skillPlusListener;
    }

    public void setSkillMinusListener(SkillMinusListener skillMinusListener) {
        this.skillMinusListener = skillMinusListener;
    }

    public View.OnClickListener getPlusClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skillPlusListener.onSkillPlus(getSkillName());
            }
        };
    }

    public View.OnClickListener getMinusClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skillMinusListener.onSkillMinus(getSkillName());
            }
        };
    }

    public String getDescription(Context context) {
        return getPower().currentLevelDescription(context);
    }

    public String getDescriptionForSelectedLevel(Context context) {
        return getPower().descriptionForLevel(getSkillLevel(), context);
    }

    public boolean isUnlocked() {
        return player.getCurrentLevel() >= getPower().unlockLevel();
    }

    public boolean isUpgradeAvailable() {
        return player.getRemainingSkillPoints() > 0;
    }

    public int getUnlockLevel() {
        return getPower().unlockLevel();
    }
}
