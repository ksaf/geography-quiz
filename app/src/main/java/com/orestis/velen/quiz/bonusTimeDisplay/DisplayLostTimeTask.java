package com.orestis.velen.quiz.bonusTimeDisplay;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.orestis.velen.quiz.R;

import java.lang.ref.WeakReference;

public class DisplayLostTimeTask extends DisplayChangedTimeTask {

    private WeakReference<TextView> bonusTimeTxt;
    private int textToDisplay;
    private WeakReference<Context> context;

    public DisplayLostTimeTask(TextView bonusTimeTxt, int displayBonusTimeDuration, int textToDisplay, Context context) {
        super(bonusTimeTxt, displayBonusTimeDuration);
        this.bonusTimeTxt = new WeakReference<>(bonusTimeTxt);
        this.context = new WeakReference<>(context);
        this.textToDisplay = textToDisplay;
    }

    @Override
    protected void onPreExecute() {
        bonusTimeTxt.get().setText("-"+ textToDisplay/1000 + context.get().getString(R.string.sec));
        bonusTimeTxt.get().setTextColor(Color.parseColor("#FF0000"));
        bonusTimeTxt.get().setVisibility(TextView.VISIBLE);
    }
}
