package com.orestis.velen.quiz.answerButtons;

import android.widget.Button;

import com.orestis.velen.quiz.R;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import static com.orestis.velen.quiz.answerButtons.AnswerChoice.A;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.B;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.C;

public class CorrectAnswerTask extends AnswerGivenTask{

    private WeakReference<HashMap<AnswerChoice, Button>> buttons;
    private AnswerChoice answerChoice;
    private boolean wasClicked;

    public CorrectAnswerTask(HashMap<AnswerChoice, Button> buttons, AnswerChoice answerChoice,
                             int displayAnswerDuration, AnswerButtonStateListener buttonStateListener,
                             boolean wasClicked) {
        super(buttons, displayAnswerDuration, buttonStateListener);
        this.buttons = new WeakReference<>(buttons);
        this.answerChoice = answerChoice;
        this.wasClicked = wasClicked;
    }

    @Override
    protected void onPreExecute() {
        disableAllButtons();
        buttons.get().get(answerChoice).setBackgroundResource(wasClicked ?
                R.drawable.answer_button_correct_legacy
                : R.drawable.answer_button_correct_unclicked_legacy);
    }

    private void disableAllButtons() {
        buttons.get().get(A).setClickable(false);
        buttons.get().get(B).setClickable(false);
        buttons.get().get(C).setClickable(false);
    }
}
