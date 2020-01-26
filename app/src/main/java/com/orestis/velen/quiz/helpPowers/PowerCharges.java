package com.orestis.velen.quiz.helpPowers;

import android.util.SparseIntArray;

import java.util.HashMap;
import java.util.Map;

import static com.orestis.velen.quiz.helpPowers.PowerType.FIFTY_FIFTY;
import static com.orestis.velen.quiz.helpPowers.PowerType.FREEZE_TIME;
import static com.orestis.velen.quiz.helpPowers.PowerType.SHIELD;
import static com.orestis.velen.quiz.helpPowers.PowerType.SKIP;
import static com.orestis.velen.quiz.helpPowers.fiftyFifty.FiftyFiftyPowerConfigs.FIFTY_FIFTY_MAX_LEVEL;
import static com.orestis.velen.quiz.helpPowers.freezeTime.FreezeTimePowerConfigs.FREEZE_TIME_MAX_LEVEL;
import static com.orestis.velen.quiz.helpPowers.shield.ShieldPowerConfigs.SHIELD_MAX_LEVEL;
import static com.orestis.velen.quiz.helpPowers.skip.SkipPowerConfigs.SKIP_MAX_LEVEL;

public class PowerCharges {

    private Map<String, SparseIntArray> chargesMap = new HashMap<>();

    public PowerCharges() {
        SparseIntArray shieldCharges = new SparseIntArray();
        SparseIntArray freezeTimeCharges = new SparseIntArray();
        SparseIntArray fiftyFiftyCharges = new SparseIntArray();
        SparseIntArray skipCharges = new SparseIntArray();

        int shieldChargesCount = 0;
        for(int i = 0; i<=SHIELD_MAX_LEVEL; i++) {
            if(i == 1 || i == 3 || i == 6 || i == 10) {
                shieldChargesCount++;
            }
            shieldCharges.put(i, shieldChargesCount);
        }

        int freezeTimeCount = 0;
        for(int i = 0; i<=FREEZE_TIME_MAX_LEVEL; i++) {
            if(i == 1 || i == 3 || i == 6 || i == 10) {
                freezeTimeCount++;
            }
            freezeTimeCharges.put(i, freezeTimeCount);
        }

        int fiftyFiftyCount = 0;
        for(int i = 0; i<=FIFTY_FIFTY_MAX_LEVEL; i++) {
            if(i == 1 || i == 3 || i == 6 || i == 10) {
                fiftyFiftyCount++;
            }
            fiftyFiftyCharges.put(i, fiftyFiftyCount);
        }

        int skipChargesCount = 0;
        for(int i = 0; i<=SKIP_MAX_LEVEL; i++) {
            if(i == 1 || i == 3 || i == 6 || i == 10) {
                skipChargesCount++;
            }
            skipCharges.put(i, skipChargesCount);
        }

        chargesMap.put(SHIELD, shieldCharges);
        chargesMap.put(FREEZE_TIME, freezeTimeCharges);
        chargesMap.put(FIFTY_FIFTY, fiftyFiftyCharges);
        chargesMap.put(SKIP, skipCharges);
    }

    public int getChargesFor(String powerType, int powerLevel) {
        return chargesMap.get(powerType).get(powerLevel);
    }
}
