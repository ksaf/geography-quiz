package com.orestis.velen.quiz.gameStartingLoading;

import android.os.CountDownTimer;
import android.widget.TextView;

public class CountDownText extends CountDownTimer {

    private TextView textView;
    private GameStartingEndListener endListener;

    public CountDownText(TextView textView, long millisInFuture, long countDownInterval, GameStartingEndListener endListener) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
        this.endListener = endListener;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        textView.setText(" " + String.valueOf(millisUntilFinished / 1000 + 1) + " ");
    }

    @Override
    public void onFinish() {
        endListener.onCountDownEnd();
    }
}
