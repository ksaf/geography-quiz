package com.orestis.velen.quiz.helpPowers.skip;

import android.view.View;

import com.orestis.velen.quiz.helpPowers.ChargeChangeListener;
import com.orestis.velen.quiz.questions.QuestionHandler;

public class SkipClickListener implements View.OnClickListener {

    private QuestionHandler questionHandler;
    private ChargeChangeListener chargeChangeListener;

    public SkipClickListener(QuestionHandler questionHandler, ChargeChangeListener chargeChangeListener) {
        this.questionHandler = questionHandler;
        this.chargeChangeListener = chargeChangeListener;
    }

    @Override
    public void onClick(View view) {
        questionHandler.nextQuestion();
        chargeChangeListener.onChargeDecreased();
    }
}
