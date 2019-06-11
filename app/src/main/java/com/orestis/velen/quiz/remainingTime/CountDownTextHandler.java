package com.orestis.velen.quiz.remainingTime;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.text.DecimalFormat;

public class CountDownTextHandler extends CountDownTimer{

    private TextView textView;
    private DecimalFormat form;
    private long startingNumber;
    private boolean isMilliseconds;
    private int timesRun;

    public CountDownTextHandler(long millisInFuture, long countDownInterval, TextView textView, long startingNumber, boolean isMilliseconds) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
        this.startingNumber = startingNumber;
        this.isMilliseconds = isMilliseconds;
        form = new DecimalFormat("0.00");
    }

    @Override
    public void onTick(long millisUntilFinished) {
        timesRun++;
        long intervalPassed = (long) Math.ceil(startingNumber * 40 / 1000.0);
        startingNumber -= intervalPassed;
        if(isMilliseconds) {
            textView.setText(form.format(startingNumber / 1000.00));
        } else {
            textView.setText(String.valueOf(startingNumber));
        }
    }

    @Override
    public void onFinish() {
        if(isMilliseconds) {
            textView.setText(form.format(0));
        } else {
            textView.setText(String.valueOf(0));
        }
    }
}
