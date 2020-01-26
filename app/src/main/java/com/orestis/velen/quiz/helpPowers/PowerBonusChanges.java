package com.orestis.velen.quiz.helpPowers;

import android.util.SparseArray;

import com.orestis.velen.quiz.helpPowers.fiftyFifty.FiftyFiftyPowerConfigs;
import com.orestis.velen.quiz.helpPowers.skip.SkipPowerConfigs;

import java.util.HashMap;
import java.util.Map;

import static com.orestis.velen.quiz.helpPowers.PowerType.FIFTY_FIFTY;
import static com.orestis.velen.quiz.helpPowers.PowerType.SKIP;
import static com.orestis.velen.quiz.helpPowers.fiftyFifty.FiftyFiftyPowerConfigs.FIFTY_FIFTY_MAX_LEVEL;
import static com.orestis.velen.quiz.helpPowers.skip.SkipPowerConfigs.SKIP_MAX_LEVEL;

public class PowerBonusChanges {

    private Map<String, SparseArray<NumberAndSign>> bonusChangesMap = new HashMap<>();

    public PowerBonusChanges() {
        SkipPowerConfigs skipPowerConfigs = new SkipPowerConfigs();
        SparseArray<NumberAndSign> skipBonusChanges = new SparseArray<>();
        for (int i = 0; i < SKIP_MAX_LEVEL; i++) {
            skipBonusChanges.put(i+1, new NumberAndSign(skipPowerConfigs.getBonusChangeForPowerLevel(i+1),
                    skipPowerConfigs.getBonusSign(i+1)));
        }
        FiftyFiftyPowerConfigs fiftyFiftyPowerConfigs = new FiftyFiftyPowerConfigs();
        SparseArray<NumberAndSign> fiftyFiftyBonusChanges = new SparseArray<>();
        for (int i = 0; i < FIFTY_FIFTY_MAX_LEVEL; i++) {
            fiftyFiftyBonusChanges.put(i+1, new NumberAndSign(fiftyFiftyPowerConfigs.getBonusChangeForPowerLevel(i+1),
                    fiftyFiftyPowerConfigs.getBonusSign(i+1)));
        }
        bonusChangesMap.put(SKIP, skipBonusChanges);
        bonusChangesMap.put(FIFTY_FIFTY, fiftyFiftyBonusChanges);
    }

    public NumberAndSign getBonusChangesFor(String powerType, int powerLevel) {
        return bonusChangesMap.get(powerType).get(powerLevel);
    }
}
