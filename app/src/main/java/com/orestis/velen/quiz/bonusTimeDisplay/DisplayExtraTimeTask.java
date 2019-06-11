package com.orestis.velen.quiz.bonusTimeDisplay;

import android.graphics.Color;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class DisplayExtraTimeTask extends DisplayChangedTimeTask {

    private WeakReference<TextView> bonusTimeTxt;
    private int textToDisplay;

    public DisplayExtraTimeTask(TextView bonusTimeTxt, int displayBonusTimeDuration, int textToDisplay) {
        super(bonusTimeTxt, displayBonusTimeDuration);
        this.bonusTimeTxt = new WeakReference<>(bonusTimeTxt);
        this.textToDisplay = textToDisplay;
    }

    @Override
    protected void onPreExecute() {
        bonusTimeTxt.get().setText("+"+ textToDisplay/1000 + "sec");
        bonusTimeTxt.get().setTextColor(Color.parseColor("#000000"));
        bonusTimeTxt.get().setVisibility(TextView.VISIBLE);
    }
}
