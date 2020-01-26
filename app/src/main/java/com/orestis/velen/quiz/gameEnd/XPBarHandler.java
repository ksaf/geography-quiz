package com.orestis.velen.quiz.gameEnd;

import android.app.Activity;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.victoryFragment.PreciseCountdown;

import java.util.List;

public class XPBarHandler extends PreciseCountdown {

    private RoundCornerProgressBar progressBar;
    private int currentProgress;
    private int endProgress;
    private int progressToAdd;
    private int tickTimes;
    private Player player;
    private List<Integer> xpRangesPassedAfterCurrent;
    private int currentBarIteration;
    private Activity activity;
    private LevelUpListener levelUpListener;
    private ExperienceTextHandler experienceTextHandler;
    private ExperienceGainEndListener experienceGainEndListener;

    public XPBarHandler(long millisInFuture, long countDownInterval, RoundCornerProgressBar progressBar,
                        Player player, Activity activity, LevelUpListener levelUpListener,
                        ExperienceTextHandler experienceTextHandler, ExperienceGainEndListener experienceGainEndListener) {
        super(millisInFuture, countDownInterval);
        this.progressBar = progressBar;
        this.tickTimes = (int) (millisInFuture / countDownInterval);
        this.player = player;
        this.activity = activity;
        this.levelUpListener = levelUpListener;
        this.experienceTextHandler = experienceTextHandler;
        this.experienceGainEndListener = experienceGainEndListener;
    }

    public void setScoreToProgress(int score) {
        this.currentProgress = player.getCurrentXP();
        xpRangesPassedAfterCurrent = player.addXP(score);
        this.endProgress = player.getCurrentXP();
        experienceTextHandler.initWithXP(currentProgress, endProgress, xpRangesPassedAfterCurrent, score);
        if(tickTimes > 0) {
            this.progressToAdd = score / tickTimes;
        }
        setMax(getCurrentXpRange());
        this.start();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        currentProgress += progressToAdd;
        setProgress(currentProgress);
        checkBarFull();
    }

    @Override
    public void onFinish() {
        setProgress(endProgress);
        checkBarFull();
        dispose();
        experienceGainEndListener.onExperienceGainEnd();
    }

    private void checkBarFull() {
        if(currentProgress > getCurrentXpRange()) {
            currentProgress -= getCurrentXpRange();
            setProgress(currentProgress - getCurrentXpRange());
            advanceXpRange();
            levelUpListener.onLevelUp();
            setMax(getCurrentXpRange());
        }
    }

    private void setMax(final float max) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setMax(max);
            }
        });
    }

    private void setProgress(final float progress) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(progress);
            }
        });
    }

    private int getCurrentXpRange() {
        return xpRangesPassedAfterCurrent.get(currentBarIteration);
    }

    private void advanceXpRange() {
        currentBarIteration++;
    }
}
