package com.orestis.velen.quiz.loadingBar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ProgressBar;

import com.orestis.velen.quiz.R;

import static com.orestis.velen.quiz.loadingBar.TimerDirection.DOWN;
import static com.orestis.velen.quiz.loadingBar.TimerDirection.UP;

public class LoadingBarHandler implements TimeIntervalListener, LoadingBarAnimationListener{

    private long duration;
    private static final int FRAMES_PER_SECOND = 60;

    private ProgressBar bar;
    private CountTimer countTimer;
    private long timeToStartFrom;
    private TimerDirection direction;
    private LoadingBarStateListener barStateListener;
    private int progressBeforeFreezeStatusChange;
    private boolean isFrozen = false;

    public LoadingBarHandler(ProgressBar bar, TimerDirection direction, LoadingBarStateListener barStateListener) {
        this.bar = bar;
        this.direction = direction;
        this.barStateListener = barStateListener;
    }

    public void startLoadingBar(long duration) {
        bar.setMax((int)(duration));
        this.duration = duration;
        timeToStartFrom = duration;
        resumeLoadingBar();
    }

    public void startLoadingBarWithFillingAnimation(long duration, int animationDuration) {
        this.duration = duration;
        bar.setMax((int)(duration));
        timeToStartFrom = duration;
        float from = direction == UP ? duration : 0;
        float to = direction == UP ? 0 : duration;
        LoadingBarAnimation anim = new LoadingBarAnimation(bar, from, to, this);
        anim.setDuration(animationDuration);
        bar.startAnimation(anim);
    }

    public void resumeLoadingBar(){
        if(!isFrozen) {
            countTimer = new CountTimer(this, direction, timeToStartFrom, 1000/FRAMES_PER_SECOND);
            countTimer.start();
        }
        barStateListener.onLoadingBarFillAnimationEnd();
    }

    @Override
    public void onTick(long msUntilFinished) {
        bar.setProgress((int)msUntilFinished);
        timeToStartFrom = msUntilFinished;
    }

    @Override
    public void onFinish() {
        int progress = direction == DOWN ? 0 : (int) duration;
        bar.setProgress(progress);
        barStateListener.onLoadingBarFinished();
    }

    public void incrementLoadingBar(long bonusTime, int msDuration){
        if(timeToStartFrom >= (duration - bonusTime)) {
            timeToStartFrom = duration - bonusTime;
        }
        timeToStartFrom += bonusTime;
        countTimer.cancel();
        LoadingBarAnimation anim = new LoadingBarAnimation(bar, bar.getProgress(), timeToStartFrom, this);
        anim.setDuration(msDuration);
        bar.startAnimation(anim);
    }

    public void decrementLoadingBar(long penaltyTime, int msDuration){
        timeToStartFrom -= penaltyTime;
        countTimer.cancel();
        LoadingBarAnimation anim = new LoadingBarAnimation(bar, timeToStartFrom + penaltyTime, timeToStartFrom, this);
        anim.setDuration(msDuration);
        bar.startAnimation(anim);
    }

    public void pauseLoadingBar(){
        countTimer.cancel();
    }

    public void freezeLoadingBar(Context context) {
        isFrozen = true;
        pauseLoadingBar();
        switchProgressDrawableTo(context, R.drawable.frozen_progress_bar);
    }

    public void unFreezeLoadingBar(Context context) {
        isFrozen = false;
        resumeLoadingBar();
        switchProgressDrawableTo(context, R.drawable.custom_progress_bar);
    }

    private void switchProgressDrawableTo(Context context, int drawableId) {
        progressBeforeFreezeStatusChange = bar.getProgress();
        bar.setProgress(0);
        Drawable progressDrawable = context.getResources().getDrawable(drawableId);
        progressDrawable.setBounds(bar.getProgressDrawable().getBounds());
        bar.setProgressDrawable(progressDrawable);
        bar.setProgress(progressBeforeFreezeStatusChange);
    }

    @Override
    public void onAnimationEnd() {
        resumeLoadingBar();
    }

    public long getRemainingTime() {
        return bar.getProgress();
    }
}
