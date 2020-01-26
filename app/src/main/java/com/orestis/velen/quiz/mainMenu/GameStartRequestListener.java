package com.orestis.velen.quiz.mainMenu;

import com.orestis.velen.quiz.questions.Difficulty;

public interface GameStartRequestListener {
    void onGameStartRequest(Class activityToStart, String gameType);
    void onGameStartConfirm(Class activityToStart, Difficulty difficulty, boolean xpBoostEnabled);
}
