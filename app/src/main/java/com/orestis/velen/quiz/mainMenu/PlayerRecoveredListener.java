package com.orestis.velen.quiz.mainMenu;

import com.orestis.velen.quiz.player.Player;

public interface PlayerRecoveredListener {
    void onPlayerRecovered(Player player, boolean fromLocalStorage);
    void onPlayerRecoveryFromFirebaseFailed();
}
