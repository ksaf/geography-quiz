package com.orestis.velen.quiz.answerButtons;

import android.graphics.Color;
import android.support.design.card.MaterialCardView;
import android.widget.Button;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import static com.orestis.velen.quiz.answerButtons.AnswerChoice.A;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.B;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.C;

public class CorrectFlagAnswerTask extends FlagAnswerGivenTask {

    private WeakReference<HashMap<AnswerChoice, Button>> buttons;
    private WeakReference<HashMap<AnswerChoice, MaterialCardView>> buttonBackGrounds;
    private AnswerChoice answerChoice;

    public CorrectFlagAnswerTask(HashMap<AnswerChoice, Button> buttons, AnswerChoice answerChoice,
                                 int displayAnswerDuration, AnswerButtonStateListener buttonStateListener,
                                 HashMap<AnswerChoice, MaterialCardView> buttonBackGrounds) {
        super(buttons, displayAnswerDuration, buttonStateListener, buttonBackGrounds);
        this.buttons = new WeakReference<>(buttons);
        this.buttonBackGrounds = new WeakReference<>(buttonBackGrounds);
        this.answerChoice = answerChoice;
    }

    @Override
    protected void onPreExecute() {
        disableAllButtons();
        buttonBackGrounds.get().get(answerChoice).setStrokeColor(Color.parseColor("#00ff00"));
    }

    private void disableAllButtons() {
        buttons.get().get(A).setClickable(false);
        buttons.get().get(B).setClickable(false);
        buttons.get().get(C).setClickable(false);
    }
}
