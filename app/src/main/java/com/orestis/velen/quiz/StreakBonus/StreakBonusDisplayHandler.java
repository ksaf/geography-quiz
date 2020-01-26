package com.orestis.velen.quiz.StreakBonus;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.orestis.velen.quiz.R;

public class StreakBonusDisplayHandler {

    private TextView accumBonusTxt;
    private TextView nowBonusTxt;
    private DisplayBonusTask displayBonusTask;
    private int displayTime;
    private Context context;

    public StreakBonusDisplayHandler(TextView nowBonusTxt, TextView accumBonusTxt, Typeface face, int displayTime, Context context) {
        this.accumBonusTxt = accumBonusTxt;
        this.nowBonusTxt = nowBonusTxt;
        this.displayTime = displayTime;
        this.context = context;
//        accumBonusTxt.setTypeface(face);
        accumBonusTxt.setText(context.getString(R.string.bonus) + " 0 ");
        nowBonusTxt.setTypeface(face);
        nowBonusTxt.setVisibility(View.INVISIBLE);

    }

    public void displayBonus(int bonusNow, int accumulatedBonus, String sign) {
        accumBonusTxt.setText(context.getString(R.string.bonus) + " " + String.valueOf(accumulatedBonus) + " ");
        nowBonusTxt.setText(" " + sign + String.valueOf(bonusNow) + " ");
        String colorHex = sign.equals("+") ? "#ee9f3c" : "#ed4337";
        nowBonusTxt.setTextColor(Color.parseColor(colorHex));
        displayBonusTask = new DisplayBonusTask(displayTime, nowBonusTxt);
        displayBonusTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
