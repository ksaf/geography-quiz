package com.orestis.velen.quiz.loadingBar;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class TimeLeftTextCountDownTimer extends CountDownTimer{

    private TextView countDownText;

    public TimeLeftTextCountDownTimer(TextView countDownText, long duration, long interval) {
        super(duration, interval);
        this.countDownText = countDownText;
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
    }
}
