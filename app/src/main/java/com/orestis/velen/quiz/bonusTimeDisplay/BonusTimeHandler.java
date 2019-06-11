package com.orestis.velen.quiz.bonusTimeDisplay;

import android.os.AsyncTask;
import android.widget.TextView;

public class BonusTimeHandler {

    private TextView bonusTimeTxt;

    public BonusTimeHandler(TextView bonusTimeTxt) {
        this.bonusTimeTxt = bonusTimeTxt;
    }

    public void displayGainedTime(int msExtraTime, int msDuration) {
        DisplayExtraTimeTask task = new DisplayExtraTimeTask(bonusTimeTxt, msDuration, msExtraTime);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void displayLostTime(int msLostTime, int msDuration) {
        DisplayLostTimeTask task = new DisplayLostTimeTask(bonusTimeTxt, msDuration, msLostTime);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
