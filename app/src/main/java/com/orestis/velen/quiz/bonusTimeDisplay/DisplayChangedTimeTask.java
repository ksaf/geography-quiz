package com.orestis.velen.quiz.bonusTimeDisplay;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public abstract class DisplayChangedTimeTask extends AsyncTask {

    private WeakReference<TextView> bonusTimeTxt;
    private int displayBonusTimeDuration;

    public DisplayChangedTimeTask(TextView bonusTimeTxt, int displayBonusTimeDuration) {
        this.bonusTimeTxt = new WeakReference<>(bonusTimeTxt);
        this.displayBonusTimeDuration = displayBonusTimeDuration;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        SystemClock.sleep(displayBonusTimeDuration);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        bonusTimeTxt.get().setVisibility(TextView.INVISIBLE);
    }
}
