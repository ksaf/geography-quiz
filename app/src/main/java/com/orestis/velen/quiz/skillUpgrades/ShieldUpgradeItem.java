package com.orestis.velen.quiz.skillUpgrades;

import com.orestis.velen.quiz.R;

import static com.orestis.velen.quiz.helpPowers.PowerType.SHIELD;

public class ShieldUpgradeItem extends SkillUpgradesItem {

    @Override
    public int getBackGroundColorId() {
        return R.drawable.help_second_chance_button_default;
    }

    @Override
    public int getIconDrawableId() {
        return R.drawable.shield_icon;
    }

    @Override
    public String getSkillName() {
        return SHIELD;
    }
}
