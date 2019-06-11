package com.orestis.velen.quiz.answerButtons;

import android.widget.Button;

import com.orestis.velen.quiz.R;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import static com.orestis.velen.quiz.answerButtons.AnswerChoice.A;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.B;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.C;

public class WrongAnswerTask extends AnswerGivenTask{

    private WeakReference<HashMap<AnswerChoice, Button>> buttons;
    private AnswerChoice givenAnswerChoice;
    private AnswerChoice correctAnswerChoice;

    public WrongAnswerTask(HashMap<AnswerChoice, Button> buttons, AnswerChoice givenAnswerChoice, AnswerChoice correctAnswerChoice,
                           int displayAnswerDuration, AnswerButtonStateListener buttonStateListener) {
        super(buttons, displayAnswerDuration, buttonStateListener);
        this.buttons = new WeakReference<>(buttons);
        this.givenAnswerChoice = givenAnswerChoice;
        this.correctAnswerChoice = correctAnswerChoice;
    }

    @Override
    protected void onPreExecute() {
        disableAllButtons();
        buttons.get().get(givenAnswerChoice).setBackgroundResource(R.drawable.answer_button_wrong);
        buttons.get().get(correctAnswerChoice).setBackgroundResource(R.drawable.answer_button_correct);
    }

    private void disableAllButtons() {
        buttons.get().get(A).setClickable(false);
        buttons.get().get(B).setClickable(false);
        buttons.get().get(C).setClickable(false);
    }
}
