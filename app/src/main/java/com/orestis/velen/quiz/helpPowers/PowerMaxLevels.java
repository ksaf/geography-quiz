package com.orestis.velen.quiz.helpPowers;

import java.util.HashMap;
import java.util.Map;

import static com.orestis.velen.quiz.helpPowers.PowerType.EXTRA_TIME;
import static com.orestis.velen.quiz.helpPowers.PowerType.FIFTY_FIFTY;
import static com.orestis.velen.quiz.helpPowers.PowerType.FREEZE_TIME;
import static com.orestis.velen.quiz.helpPowers.PowerType.SHIELD;
import static com.orestis.velen.quiz.helpPowers.PowerType.SKIP;
import static com.orestis.velen.quiz.helpPowers.extraTime.ExtraTimePowerConfigs.EXTRA_TIME_MAX_LEVEL;
import static com.orestis.velen.quiz.helpPowers.fiftyFifty.FiftyFiftyPowerConfigs.FIFTY_FIFTY_MAX_LEVEL;
import static com.orestis.velen.quiz.helpPowers.freezeTime.FreezeTimePowerConfigs.FREEZE_TIME_MAX_LEVEL;
import static com.orestis.velen.quiz.helpPowers.shield.ShieldPowerConfigs.SHIELD_MAX_LEVEL;
import static com.orestis.velen.quiz.helpPowers.skip.SkipPowerConfigs.SKIP_MAX_LEVEL;

public class PowerMaxLevels {
    private Map<String, Integer> maxLevels = new HashMap<>();

    public PowerMaxLevels() {
        maxLevels.put(SKIP, SKIP_MAX_LEVEL);
        maxLevels.put(FIFTY_FIFTY, FIFTY_FIFTY_MAX_LEVEL);
        maxLevels.put(SHIELD, SHIELD_MAX_LEVEL);
        maxLevels.put(FREEZE_TIME, FREEZE_TIME_MAX_LEVEL);
        maxLevels.put(EXTRA_TIME, EXTRA_TIME_MAX_LEVEL);
    }

    public int getMaxLevelFor(String powerType) {
        return maxLevels.get(powerType);
    }
}
