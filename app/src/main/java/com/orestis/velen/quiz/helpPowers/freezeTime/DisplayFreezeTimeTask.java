package com.orestis.velen.quiz.helpPowers.freezeTime;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayFreezeTimeTask extends CountDownTimer {

    private TextView freezeTimeTxt;
    private ImageView hourglassImg;
    private FreezeTimeEndListener endListener;

    public DisplayFreezeTimeTask(TextView freezeTimeTxt, ImageView hourglassImg, int freezeTimeDuration, FreezeTimeEndListener endListener) {
        super(freezeTimeDuration, 1000);
        this.freezeTimeTxt = freezeTimeTxt;
        this.hourglassImg = hourglassImg;
        this.endListener = endListener;
        freezeTimeTxt.setText(String.valueOf(freezeTimeDuration));
        freezeTimeTxt.setVisibility(TextView.VISIBLE);
        hourglassImg.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTick(long msUntilFinished) {
        freezeTimeTxt.setText(String.valueOf((int)Math.ceil(msUntilFinished / 1000.0)));
    }

    @Override
    public void onFinish() {
        freezeTimeTxt.setVisibility(TextView.INVISIBLE);
        hourglassImg.setVisibility(View.GONE);
        endListener.onFreezeTimeEnd();
    }
}
