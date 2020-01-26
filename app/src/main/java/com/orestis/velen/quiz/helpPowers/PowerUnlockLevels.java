package com.orestis.velen.quiz.helpPowers;

import java.util.HashMap;
import java.util.Map;

import static com.orestis.velen.quiz.helpPowers.PowerType.EXTRA_TIME;
import static com.orestis.velen.quiz.helpPowers.PowerType.FIFTY_FIFTY;
import static com.orestis.velen.quiz.helpPowers.PowerType.FREEZE_TIME;
import static com.orestis.velen.quiz.helpPowers.PowerType.SHIELD;
import static com.orestis.velen.quiz.helpPowers.PowerType.SKIP;

public class PowerUnlockLevels {
    private Map<String, Integer> unlockLevels = new HashMap<>();

    public PowerUnlockLevels() {
        unlockLevels.put(SKIP, 1);
        unlockLevels.put(FIFTY_FIFTY, 3);
        unlockLevels.put(SHIELD, 7);
        unlockLevels.put(FREEZE_TIME, 10);
        unlockLevels.put(EXTRA_TIME, 1);
    }

    public int getUnlockLevelFor(String powerType) {
        return unlockLevels.get(powerType);
    }
}
