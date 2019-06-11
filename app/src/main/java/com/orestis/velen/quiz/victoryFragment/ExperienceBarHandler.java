package com.orestis.velen.quiz.victoryFragment;

import android.widget.ProgressBar;

import com.orestis.velen.quiz.leveling.LevelUpListener;
import com.orestis.velen.quiz.player.Player;

import java.util.List;

public class ExperienceBarHandler extends PreciseCountdown {

    private ProgressBar progressBar;
    private int currentProgress;
    private int endProgress;
    private int progressToAdd;
    private int tickTimes;
    private Player player;
    private List<Integer> xpRangesPassedAfterCurrent;
    private int currentBarIteration;
    private LevelUpListener levelUpListener;
    private VictoryPhaseEndListener victoryPhaseEndListener;
    private RewardPhase rewardPhase;

    public ExperienceBarHandler(long millisInFuture, long countDownInterval, ProgressBar progressBar, Player player, LevelUpListener levelUpListener, VictoryPhaseEndListener victoryPhaseEndListener, RewardPhase rewardPhase) {
        super(millisInFuture, countDownInterval);
        this.progressBar = progressBar;
        this.tickTimes = (int) (millisInFuture / countDownInterval);
        this.player = player;
        this.levelUpListener = levelUpListener;
        this.victoryPhaseEndListener = victoryPhaseEndListener;
        this.rewardPhase = rewardPhase;
    }

    public void setScoreToProgress(int score) {
        this.currentProgress = player.getCurrentXP();
        xpRangesPassedAfterCurrent = player.addXP(score);
        this.endProgress = player.getCurrentXP();
        this.progressToAdd = score / tickTimes;
        this.progressBar.setMax(getCurrentXpRange());
    }

    @Override
    public void onTick(long millisUntilFinished) {
        currentProgress += progressToAdd;
        progressBar.setProgress(currentProgress);
        checkBarFull();
    }

    @Override
    public void onFinish() {
        progressBar.setProgress(endProgress);
        checkBarFull();
        dispose();
        victoryPhaseEndListener.onPhaseEnd(rewardPhase);
    }

    private void checkBarFull() {
        if(currentProgress > getCurrentXpRange()) {
            currentProgress -= getCurrentXpRange();
            progressBar.setProgress(currentProgress - getCurrentXpRange());
            advanceXpRange();
            levelUpListener.onLevelUp();
            this.progressBar.setMax(getCurrentXpRange());
        }
    }

    private int getCurrentXpRange() {
        return xpRangesPassedAfterCurrent.get(currentBarIteration);
    }

    private void advanceXpRange() {
        currentBarIteration++;
    }
}
