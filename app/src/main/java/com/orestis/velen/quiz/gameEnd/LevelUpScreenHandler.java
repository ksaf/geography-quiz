package com.orestis.velen.quiz.gameEnd;

import android.support.annotation.Nullable;

import com.orestis.velen.quiz.skillUpgrades.LevelUpScreenClosedListener;

public interface LevelUpScreenHandler {
    void showLevelUpScreen(@Nullable LevelUpScreenClosedListener levelUpScreenClosedListener);
}
