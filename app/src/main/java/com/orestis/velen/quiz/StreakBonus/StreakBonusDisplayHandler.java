package com.orestis.velen.quiz.StreakBonus;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

public class StreakBonusDisplayHandler {

    private TextView accumBonusTxt;
    private TextView nowBonusTxt;
    private DisplayBonusTask displayBonusTask;
    private int displayTime;

    public StreakBonusDisplayHandler(TextView nowBonusTxt, TextView accumBonusTxt, Typeface face, int displayTime) {
        this.accumBonusTxt = accumBonusTxt;
        this.nowBonusTxt = nowBonusTxt;
        this.displayTime = displayTime;
        accumBonusTxt.setTypeface(face);
        accumBonusTxt.setText("Bonus: 0 ");
        nowBonusTxt.setTypeface(face);
        nowBonusTxt.setVisibility(View.INVISIBLE);

    }

    public void displayBonus(int bonusNow, int accumulatedBonus) {
        accumBonusTxt.setText("Bonus: " + String.valueOf(accumulatedBonus) + " ");
        nowBonusTxt.setText(" +" + String.valueOf(bonusNow) + " ");
        displayBonusTask = new DisplayBonusTask(displayTime, nowBonusTxt);
        displayBonusTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
