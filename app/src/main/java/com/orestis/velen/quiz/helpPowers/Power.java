package com.orestis.velen.quiz.helpPowers;

import android.content.Context;

import com.google.gson.annotations.Expose;

public class Power {

    @Expose
    private int powerLevel;
    @Expose
    private String powerType;

    public Power(){}

    public Power(int powerLevel, String powerType) {
        this.powerLevel = powerLevel;
        this.powerType = powerType;
    }

    public void increasePowerLever() {
        powerLevel++;
    }

    public void setPowerLevel(int powerLevel) {
        this.powerLevel = powerLevel;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public int charges() {
        PowerCharges powerCharges = new PowerCharges();
        return powerCharges.getChargesFor(powerType, powerLevel);
    }

    public String getPowerType() {
        return powerType;
    }

    public int unlockLevel() {
        PowerUnlockLevels powerUnlockLevels = new PowerUnlockLevels();
        return powerUnlockLevels.getUnlockLevelFor(powerType);
    }

    public String currentLevelDescription(Context context) {
        PowerDescriptions descriptions = new PowerDescriptions(context);
        return descriptions.getDescriptionFor(powerType, powerLevel);
    }

    public String descriptionForLevel(int powerLevel, Context context) {
        PowerDescriptions descriptions = new PowerDescriptions(context);
        return descriptions.getDescriptionFor(powerType, powerLevel);
    }

    public String descriptionTitle(Context context) {
        PowerDescriptions descriptions = new PowerDescriptions(context);
        return descriptions.getDescriptionTitle(powerType);
    }

    public String bonusForLevel(int powerLevel, Context context) {
        PowerDescriptions descriptions = new PowerDescriptions(context);
        return descriptions.getBonus(powerType, powerLevel);
    }

    public String bonusSignForLevel(int powerLevel, Context context) {
        PowerDescriptions descriptions = new PowerDescriptions(context);
        return descriptions.getBonusSign(powerType, powerLevel);
    }

    public String bonusText(Context context) {
        PowerDescriptions descriptions = new PowerDescriptions(context);
        return descriptions.getBonusText(powerType);
    }

    public int usagesForLevel(int powerLevel, Context context) {
        PowerDescriptions descriptions = new PowerDescriptions(context);
        return descriptions.getUsages(powerType, powerLevel);
    }

    public String usagesText(Context context) {
        PowerDescriptions descriptions = new PowerDescriptions(context);
        return descriptions.getUsagesText(powerType);
    }

    public String displayName(Context context) {
        PowerDescriptions descriptions = new PowerDescriptions(context);
        return descriptions.getDisplayName(powerType);
    }

    public NumberAndSign bonusChanges() {
        PowerBonusChanges powerBonusChanges = new PowerBonusChanges();
        return powerBonusChanges.getBonusChangesFor(powerType, powerLevel);
    }

    public int maxLevel() {
        PowerMaxLevels powerMaxLevels = new PowerMaxLevels();
        return powerMaxLevels.getMaxLevelFor(powerType);
    }
}
