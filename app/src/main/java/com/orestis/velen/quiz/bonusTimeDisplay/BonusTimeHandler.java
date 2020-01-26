package com.orestis.velen.quiz.bonusTimeDisplay;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

public class BonusTimeHandler {

    private TextView bonusTimeTxt;
    private Context context;

    public BonusTimeHandler(TextView bonusTimeTxt, Context context) {
        this.bonusTimeTxt = bonusTimeTxt;
        this.context = context;
    }

    public void displayGainedTime(int msExtraTime, int msDuration) {
        DisplayExtraTimeTask task = new DisplayExtraTimeTask(bonusTimeTxt, msDuration, msExtraTime, context);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void displayLostTime(int msLostTime, int msDuration) {
        DisplayLostTimeTask task = new DisplayLostTimeTask(bonusTimeTxt, msDuration, msLostTime, context);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
