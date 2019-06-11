package com.orestis.velen.quiz.helpPowers.skip;

import android.util.SparseIntArray;

class SkipPowerLevels {

    private SparseIntArray bonusLost;

    int getTurnDurationForPowerLevel(int powerLevel) {
        populateBonusLost();
        return bonusLost.get(powerLevel);
    }

    private void populateBonusLost() {
        if(bonusLost == null) {
            bonusLost = new SparseIntArray();
            bonusLost.append(1, 100);
            bonusLost.append(2, 90);
            bonusLost.append(3, 70);
            bonusLost.append(4, 40);
            bonusLost.append(5, 0);
            bonusLost.append(6, 0);
            bonusLost.append(7, 0);
            bonusLost.append(8, 0);
            bonusLost.append(9, 0);
            bonusLost.append(10, 0);
        }
    }

    private void populateBonusGained() {
        if(bonusLost == null) {
            bonusLost = new SparseIntArray();
            bonusLost.append(1, 0);
            bonusLost.append(2, 0);
            bonusLost.append(3, 0);
            bonusLost.append(4, 0);
            bonusLost.append(5, 0);
            bonusLost.append(6, 40);
            bonusLost.append(7, 70);
            bonusLost.append(8, 100);
            bonusLost.append(9, 140);
            bonusLost.append(10, 180);
        }
    }
}
