package com.orestis.velen.quiz.skillUpgrades;

import com.orestis.velen.quiz.R;

import static com.orestis.velen.quiz.helpPowers.PowerType.SKIP;

public class SkipUpgradeItem extends SkillUpgradesItem {

    @Override
    public int getBackGroundColorId() {
        return R.drawable.help_free_pass_button_default;
    }

    @Override
    public int getIconDrawableId() {
        return R.drawable.skip_icon;
    }

    @Override
    public String getSkillName() {
        return SKIP;
    }
}
