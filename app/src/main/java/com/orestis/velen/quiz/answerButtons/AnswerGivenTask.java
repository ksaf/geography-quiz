package com.orestis.velen.quiz.answerButtons;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.Button;

import com.orestis.velen.quiz.R;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import static com.orestis.velen.quiz.answerButtons.AnswerChoice.A;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.B;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.C;

public abstract class AnswerGivenTask extends AsyncTask {

    private WeakReference<HashMap<AnswerChoice, Button>> buttons;
    private AnswerButtonStateListener buttonStateListener;
    private int displayAnswerDuration;

    public AnswerGivenTask(HashMap<AnswerChoice, Button> buttons, int displayAnswerDuration, AnswerButtonStateListener buttonStateListener) {
        this.buttons = new WeakReference<>(buttons);
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
        buttons.get().get(A).setBackgroundResource(R.drawable.answer_button_default);
        buttons.get().get(B).setBackgroundResource(R.drawable.answer_button_default);
        buttons.get().get(C).setBackgroundResource(R.drawable.answer_button_default);
    }
}
