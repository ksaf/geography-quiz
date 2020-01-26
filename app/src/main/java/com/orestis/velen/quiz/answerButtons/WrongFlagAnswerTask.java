package com.orestis.velen.quiz.answerButtons;

import android.graphics.Color;
import android.support.design.card.MaterialCardView;
import android.widget.Button;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import static com.orestis.velen.quiz.answerButtons.AnswerChoice.A;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.B;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.C;

public class WrongFlagAnswerTask extends FlagAnswerGivenTask {

    private WeakReference<HashMap<AnswerChoice, Button>> buttons;
    private WeakReference<HashMap<AnswerChoice, MaterialCardView>> buttonBackGrounds;
    private AnswerChoice givenAnswerChoice;
    private AnswerChoice correctAnswerChoice;

    public WrongFlagAnswerTask(HashMap<AnswerChoice, Button> buttons, AnswerChoice givenAnswerChoice, AnswerChoice correctAnswerChoice,
                               int displayAnswerDuration, AnswerButtonStateListener buttonStateListener,
                               HashMap<AnswerChoice, MaterialCardView> buttonBackGrounds) {
        super(buttons, displayAnswerDuration, buttonStateListener, buttonBackGrounds);
        this.buttons = new WeakReference<>(buttons);
        this.buttonBackGrounds = new WeakReference<>(buttonBackGrounds);
        this.givenAnswerChoice = givenAnswerChoice;
        this.correctAnswerChoice = correctAnswerChoice;
    }

    @Override
    protected void onPreExecute() {
        disableAllButtons();
        buttonBackGrounds.get().get(givenAnswerChoice).setStrokeColor(Color.parseColor("#ff0000"));
        buttonBackGrounds.get().get(correctAnswerChoice).setStrokeColor(Color.parseColor("#00ff00"));
    }

    private void disableAllButtons() {
        buttons.get().get(A).setClickable(false);
        buttons.get().get(B).setClickable(false);
        buttons.get().get(C).setClickable(false);
    }
}
