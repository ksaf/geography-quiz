package com.orestis.velen.quiz.skillUpgrades;

public class SkillDescriptionItem {
    private final int level;
    private final String bonus;
    private final String bonusSign;
    private final int usages;
    private final int currentLevel;
    private final String displayName;

    public SkillDescriptionItem(int level, String bonus, String bonusSign, int usages, int currentLevel, String displayName) {
        this.level = level;
        this.bonus = bonus;
        this.bonusSign = bonusSign;
        this.usages = usages;
        this.currentLevel = currentLevel;
        this.displayName = displayName;
    }

    public int getLevel() {
        return level;
    }

    public String getBonus() {
        return bonus;
    }

    public String getBonusSign() {
        return bonusSign;
    }

    public int getUsages() {
        return usages;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public String getDisplayName() {
        return displayName;
    }
}
