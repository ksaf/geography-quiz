package com.orestis.velen.quiz.skillUpgrades;

import android.content.Context;
import android.view.View;

import com.orestis.velen.quiz.helpPowers.Power;
import com.orestis.velen.quiz.player.Player;

import java.util.ArrayList;
import java.util.List;

abstract class SkillUpgradesItem {

    private SkillPlusListener skillPlusListener;
    private SkillMinusListener skillMinusListener;
    private SkillDescriptionListener skillDescriptionListener;
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

    public void setSkillDescriptionListener(SkillDescriptionListener skillDescriptionListener) {
        this.skillDescriptionListener = skillDescriptionListener;
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

    public View.OnClickListener getDescriptionListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skillDescriptionListener.onSkillDescription(getSkillName());
            }
        };
    }

    public String getDescription(Context context) {
        return getPower().currentLevelDescription(context);
    }

    public String getDescriptionTitle(Context context) {
        return getPower().descriptionTitle(context);
    }

    public String getDescriptionBonusText(Context context) {
        return getPower().bonusText(context);
    }

    public String getDescriptionUsagesText(Context context) {
        return getPower().usagesText(context);
    }

    public String getDisplayName(Context context) {
        return getPower().displayName(context);
    }

    public String getDescriptionForSelectedLevel(Context context) {
        return getPower().descriptionForLevel(getSkillLevel(), context);
    }

    public List<SkillDescriptionItem> getDescriptionItems(Context context) {
        List<SkillDescriptionItem> descriptions = new ArrayList<>();
        for(int i = 1; i <= player.getPowers().get(getSkillName()).maxLevel(); i++) {
            descriptions.add(new SkillDescriptionItem(
                    i,
                    getPower().bonusForLevel(i, context),
                    getPower().bonusSignForLevel(i, context),
                    getPower().usagesForLevel(i, context),
                    getPower().getPowerLevel(),
                    getPower().displayName(context)
                    ));
        }
        return descriptions;
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
