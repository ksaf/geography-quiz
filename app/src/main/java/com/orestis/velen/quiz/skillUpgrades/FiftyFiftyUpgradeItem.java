package com.orestis.velen.quiz.skillUpgrades;

import com.orestis.velen.quiz.R;

import static com.orestis.velen.quiz.helpPowers.PowerType.FIFTY_FIFTY;

public class FiftyFiftyUpgradeItem extends SkillUpgradesItem {

    @Override
    public int getBackGroundColorId() {
        return R.drawable.help_fiftyfifty_button_default;
    }

    @Override
    public int getIconDrawableId() {
        return R.drawable.fiftyfifty_icon;
    }

    @Override
    public String getSkillName() {
        return FIFTY_FIFTY;
    }
}
