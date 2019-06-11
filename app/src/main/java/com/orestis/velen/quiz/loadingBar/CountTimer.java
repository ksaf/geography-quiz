package com.orestis.velen.quiz.loadingBar;


import android.os.CountDownTimer;

import static com.orestis.velen.quiz.loadingBar.TimerDirection.UP;

public class CountTimer extends CountDownTimer{

    private long duration;
    private TimeIntervalListener timeIntervalListener;
    private TimerDirection direction;
    private long msUntilFinished;

    public CountTimer(TimeIntervalListener timeIntervalListener, TimerDirection direction, long duration, long interval) {
        super(duration, interval);
        this.timeIntervalListener = timeIntervalListener;
        this.timeIntervalListener = timeIntervalListener;
        this.duration = duration;
        this.direction = direction;
    }

    @Override
    public void onTick(long msUntilFinished) {
        long timeToFinish = direction == UP ? duration - msUntilFinished : msUntilFinished;
        this.msUntilFinished = msUntilFinished;
        timeIntervalListener.onTick(timeToFinish);
    }

    @Override
    public void onFinish() {
        timeIntervalListener.onFinish();
    }

}
