package com.orestis.velen.quiz.helpPowers.fiftyFifty;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.orestis.velen.quiz.answerButtons.AnswerChoice;
import com.orestis.velen.quiz.helpPowers.ChargeChangeListener;
import com.orestis.velen.quiz.helpPowers.buttonScaleDownAnimation.ScaleDownAnimationHandler;
import com.orestis.velen.quiz.helpPowers.buttonScaleDownAnimation.ViewScaledDownListener;
import com.orestis.velen.quiz.questions.QuestionHandler;

import java.util.HashMap;
import java.util.Random;

import static com.orestis.velen.quiz.answerButtons.AnswerChoice.A;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.B;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.C;

public class FiftyFiftyClickListener implements View.OnClickListener, ViewScaledDownListener {

    private QuestionHandler questionHandler;
    private HashMap<AnswerChoice, Button> buttons;
    private ChargeChangeListener chargeChangeListener;
    private Context context;

    public FiftyFiftyClickListener(QuestionHandler questionHandler, HashMap<AnswerChoice, Button> buttons, ChargeChangeListener chargeChangeListener, Context context) {
        this.questionHandler = questionHandler;
        this.buttons = buttons;
        this.chargeChangeListener = chargeChangeListener;
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        AnswerChoice correctChoice = questionHandler.getCorrectAnswerChoice();
        switch (correctChoice) {
            case A:
                hideRandomButtonFrom(B, C);
                break;
            case B:
                hideRandomButtonFrom(A, C);
                break;
            case C:
                hideRandomButtonFrom(A, B);
                break;
        }
    }

    private void hideRandomButtonFrom(AnswerChoice wrongChoice1, AnswerChoice wrongChoice2) {
        Random rand = new Random();
        int x = rand.nextInt(1);
        ScaleDownAnimationHandler animationHandler = new ScaleDownAnimationHandler(context);
        if(x == 0) {
            animationHandler.hide(buttons.get(wrongChoice1), this);
        } else {
            animationHandler.hide(buttons.get(wrongChoice2), this);
        }
    }

    @Override
    public void onViewScaledDown() {
        chargeChangeListener.onChargeDecreased();
    }
}
