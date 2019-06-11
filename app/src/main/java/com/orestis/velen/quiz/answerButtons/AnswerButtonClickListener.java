package com.orestis.velen.quiz.answerButtons;

import android.view.View;

public class AnswerButtonClickListener implements View.OnClickListener{

    private AnswerButtonPressedListener pressedListener;

    public AnswerButtonClickListener(AnswerButtonPressedListener pressedListener) {
        this.pressedListener = pressedListener;
    }

    @Override
    public void onClick(View view) {
        pressedListener.onAnswerPressed(view);
    }
}
