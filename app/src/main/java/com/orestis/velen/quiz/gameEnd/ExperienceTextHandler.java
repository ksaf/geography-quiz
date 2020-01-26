package com.orestis.velen.quiz.gameEnd;

import android.app.Activity;
import android.widget.TextView;

import com.orestis.velen.quiz.victoryFragment.PreciseCountdown;

import java.util.List;

public class ExperienceTextHandler extends PreciseCountdown {

    private TextView textView;
    private int startingXP;
    private int endingXP;
    private List<Integer> xpRangesPassed;
    private int currentMaxXP;
    private int intervals;
    private int valToAdd;
    private Activity activity;
    private int currentLevelIteration = 0;

    public ExperienceTextHandler(long totalTime, long interval, TextView textView, Activity activity) {
        super(totalTime, interval);
        this.textView = textView;
        this.activity = activity;
        this.intervals = (int)((double)totalTime / (double) interval);
    }

    public void initWithXP(int startingXP, int endingXP, List<Integer> xpRangesPassed, int score) {
        this.startingXP = startingXP;
        this.endingXP = endingXP;
        this.xpRangesPassed = xpRangesPassed;
        this.currentMaxXP = xpRangesPassed.get(0);
        this.valToAdd = (int)((double) score / (double) intervals);
        start();
    }

    @Override
    public void onTick(long timeLeft) {
        startingXP += valToAdd;
        if(startingXP >= xpRangesPassed.get(currentLevelIteration)) {
            if(currentLevelIteration < xpRangesPassed.size()-1) {
                currentLevelIteration++;
            }
            currentMaxXP = xpRangesPassed.get(currentLevelIteration);
            startingXP = 0;
        }
        setText(String.valueOf(startingXP) + "/" + String.valueOf(currentMaxXP));
    }

    @Override
    public void onFinish() {
        setText(String.valueOf(endingXP) + "/" + String.valueOf(currentMaxXP));
        dispose();
    }

    private void setText(final String text) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(text);
            }
        });
    }
}
