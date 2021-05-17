package com.orestis.velen.quiz.adverts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class XpBoostTimeLeftTimer extends CountDownTimer {

    private TextView countDownText;
    private ViewGroup countDownTextContainer;
    private Button adBtn;
    private Context context;

    public XpBoostTimeLeftTimer(ViewGroup countDownTextContainer, TextView countDownText, Button adBtn, long duration, long interval, Context context) {
        super(duration, interval);
        this.countDownText = countDownText;
        this.countDownTextContainer = countDownTextContainer;
        this.adBtn = adBtn;
        this.context = context;
    }

    @Override
    public void onTick(long msUntilFinished) {
        @SuppressLint("DefaultLocale") String formatedTime = String.format("%d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(msUntilFinished),
                TimeUnit.MILLISECONDS.toSeconds(msUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(msUntilFinished))
        );
        countDownText.setText(formatedTime);
    }

    @Override
    public void onFinish() {
        countDownText.setText("-");
        adBtn.setVisibility(View.VISIBLE);
        countDownTextContainer.setVisibility(View.GONE);
    }
}
