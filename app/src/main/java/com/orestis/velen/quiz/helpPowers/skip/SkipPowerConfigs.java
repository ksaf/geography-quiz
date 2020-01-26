package com.orestis.velen.quiz.helpPowers.skip;

import android.util.SparseIntArray;

import com.orestis.velen.quiz.helpPowers.PowerConfigs;

public class SkipPowerConfigs implements PowerConfigs {

    private SparseIntArray bonusChange;
    public static final int SKIP_MAX_LEVEL = 10;

    public int getBonusChangeForPowerLevel(int powerLevel) {
        populateBonusChange();
        return bonusChange.get(powerLevel);
    }

    private void populateBonusChange() {
        if(bonusChange == null) {
            bonusChange = new SparseIntArray();
            bonusChange.put(1, 100);
            bonusChange.put(2, 90);
            bonusChange.put(3, 70);
            bonusChange.put(4, 40);
            bonusChange.put(5, 0);
            bonusChange.put(6, 40);
            bonusChange.put(7, 70);
            bonusChange.put(8, 100);
            bonusChange.put(9, 140);
            bonusChange.put(10, 180);
        }
    }

    public String getBonusSign(int powerLevel) {
        String sign = powerLevel < 5 ? "-" : "+";
        if(powerLevel == 5) {
            sign = "";
        }
        return sign;
    }
}
