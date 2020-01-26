package com.orestis.velen.quiz.skillUpgrades;

import com.orestis.velen.quiz.R;

import static com.orestis.velen.quiz.helpPowers.PowerType.EXTRA_TIME;

public class ExtraTimeUpgradeItem extends SkillUpgradesItem {

    @Override
    public int getBackGroundColorId() {
        return R.drawable.extra_time_button_default;
    }

    @Override
    public int getIconDrawableId() {
        return R.drawable.extra_time_icon;
    }

    @Override
    public String getSkillName() {
        return EXTRA_TIME;
    }
}
