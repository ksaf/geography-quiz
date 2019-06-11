package com.orestis.velen.quiz.StreakBonus;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class DisplayBonusTask extends AsyncTask{

    private int displayTime;
    private WeakReference<TextView> nowBonusTxt;

    public DisplayBonusTask(int displayTime, TextView nowBonusTxt) {
        this.displayTime = displayTime;
        this.nowBonusTxt = new WeakReference<>(nowBonusTxt);
    }

    @Override
    protected void onPreExecute() {
        nowBonusTxt.get().setVisibility(View.VISIBLE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        SystemClock.sleep(displayTime);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        nowBonusTxt.get().setVisibility(View.INVISIBLE);
    }
}
