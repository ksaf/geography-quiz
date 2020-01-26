package com.orestis.velen.quiz.helpPowers.extraTime;

import android.util.SparseIntArray;

import com.orestis.velen.quiz.helpPowers.PowerConfigs;

public class ExtraTimePowerConfigs implements PowerConfigs {

    private SparseIntArray durations;
    public static final int EXTRA_TIME_MAX_LEVEL = 10;

    public int getExtraTimeForPowerLevel(int powerLevel) {
        populateDurations();
        return durations.get(powerLevel);
    }

    private void populateDurations() {
        if(durations == null) {
            durations = new SparseIntArray();
            durations.append(0, 0);
            durations.append(1, 2000);
            durations.append(2, 4000);
            durations.append(3, 6000);
            durations.append(4, 8000);
            durations.append(5, 10000);
            durations.append(6, 12000);
            durations.append(7, 14000);
            durations.append(8, 16000);
            durations.append(9, 18000);
            durations.append(10, 20000);
        }
    }
}
