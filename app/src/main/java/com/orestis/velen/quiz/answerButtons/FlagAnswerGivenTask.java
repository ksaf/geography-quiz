package com.orestis.velen.quiz.answerButtons;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.design.card.MaterialCardView;
import android.widget.Button;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import static com.orestis.velen.quiz.answerButtons.AnswerChoice.A;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.B;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.C;

public abstract class FlagAnswerGivenTask extends AsyncTask {

    private WeakReference<HashMap<AnswerChoice, Button>> buttons;
    private WeakReference<HashMap<AnswerChoice, MaterialCardView>> buttonBackGrounds;
    private AnswerButtonStateListener buttonStateListener;
    private int displayAnswerDuration;

    public FlagAnswerGivenTask(HashMap<AnswerChoice, Button> buttons, int displayAnswerDuration,
                               AnswerButtonStateListener buttonStateListener, HashMap<AnswerChoice, MaterialCardView> buttonBackGrounds) {
        this.buttons = new WeakReference<>(buttons);
        this.buttonBackGrounds = new WeakReference<>(buttonBackGrounds);
        this.buttonStateListener = buttonStateListener;
        this.displayAnswerDuration = displayAnswerDuration;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        SystemClock.sleep(displayAnswerDuration);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        enableAllButtons();
        resetAllButtonsToDefault();
        buttonStateListener.onAnswerButtonsEnabled();
    }

    private void enableAllButtons() {
        buttons.get().get(A).setClickable(true);
        buttons.get().get(B).setClickable(true);
        buttons.get().get(C).setClickable(true);
    }

    private void resetAllButtonsToDefault() {
        buttonBackGrounds.get().get(A).setStrokeColor(Color.parseColor("#dff6efd7"));
        buttonBackGrounds.get().get(B).setStrokeColor(Color.parseColor("#dff6efd7"));
        buttonBackGrounds.get().get(C).setStrokeColor(Color.parseColor("#dff6efd7"));
    }
}
