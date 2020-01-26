package com.orestis.velen.quiz.skillUpgrades;

import com.orestis.velen.quiz.R;

import static com.orestis.velen.quiz.helpPowers.PowerType.FREEZE_TIME;

public class FreezeTimeUpgradeItem extends SkillUpgradesItem {

    @Override
    public int getBackGroundColorId() {
        return R.drawable.help_stop_time_button_default;
    }

    @Override
    public int getIconDrawableId() {
        return R.drawable.freeze_icon;
    }

    @Override
    public String getSkillName() {
        return FREEZE_TIME;
    }

}
