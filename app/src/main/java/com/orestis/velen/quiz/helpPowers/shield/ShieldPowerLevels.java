package com.orestis.velen.quiz.helpPowers.shield;

import android.util.SparseIntArray;

class ShieldPowerLevels {

    private SparseIntArray durations;

    int getTurnDurationForPowerLevel(int powerLevel) {
        populateDurations();
        return durations.get(powerLevel);
    }

    private void populateDurations() {
        if(durations == null) {
            durations = new SparseIntArray();
            durations.append(1, 1);
            durations.append(2, 2);
            durations.append(3, 3);
            durations.append(4, 4);
            durations.append(5, 5);
            durations.append(6, 6);
            durations.append(7, 7);
            durations.append(8, 8);
            durations.append(9, 9);
            durations.append(10, 100);
        }
    }
}
