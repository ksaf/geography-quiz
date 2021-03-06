package com.orestis.velen.quiz.helpPowers.freezeTime;

import android.util.SparseIntArray;

import com.orestis.velen.quiz.helpPowers.PowerConfigs;

public class FreezeTimePowerConfigs implements PowerConfigs {

    private SparseIntArray durations;
    public static final int FREEZE_TIME_MAX_LEVEL = 10;

    public int getTurnDurationForPowerLevel(int powerLevel) {
        populateDurations();
        return durations.get(powerLevel);
    }

    private void populateDurations() {
        if(durations == null) {
            durations = new SparseIntArray();
            durations.append(1, 5000);
            durations.append(2, 6000);
            durations.append(3, 7000);
            durations.append(4, 8000);
            durations.append(5, 9000);
            durations.append(6, 10000);
            durations.append(7, 11000);
            durations.append(8, 12000);
            durations.append(9, 13000);
            durations.append(10, 14000);
        }
    }
}
