package com.orestis.velen.quiz.loadingBar;

public interface TimeIntervalListener {

    void onTick(long msUntilFinished);
    void onFinish();

}
