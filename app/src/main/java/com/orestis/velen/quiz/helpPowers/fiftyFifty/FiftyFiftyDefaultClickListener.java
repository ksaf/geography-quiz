package com.orestis.velen.quiz.helpPowers.fiftyFifty;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.orestis.velen.quiz.answerButtons.AnswerChoice;
import com.orestis.velen.quiz.helpPowers.ChargeChangeListener;
import com.orestis.velen.quiz.helpPowers.buttonScaleDownAnimation.ScaleDownAnimationHandler;
import com.orestis.velen.quiz.helpPowers.buttonScaleDownAnimation.ViewScaledDownListener;
import com.orestis.velen.quiz.questions.QuestionHandler;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

import java.util.HashMap;
import java.util.Random;

import static com.orestis.velen.quiz.answerButtons.AnswerChoice.A;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.B;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.C;

public class FiftyFiftyDefaultClickListener implements View.OnClickListener, ViewScaledDownListener {

    private QuestionHandler questionHandler;
    private HashMap<AnswerChoice, Button> buttons;
    private ChargeChangeListener chargeChangeListener;
    private Context context;
    private SoundPoolHelper soundHelper;

    public FiftyFiftyDefaultClickListener(QuestionHandler questionHandler, HashMap<AnswerChoice, Button> buttons,
                                          ChargeChangeListener chargeChangeListener, Context context, SoundPoolHelper soundHelper) {
        this.questionHandler = questionHandler;
        this.buttons = buttons;
        this.chargeChangeListener = chargeChangeListener;
        this.context = context;
        this.soundHelper = soundHelper;
    }

    @Override
    public void onClick(View view) {
        view.setEnabled(false);
        soundHelper.playInGamePowerEnableSound();
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
        ScaleDownAnimationHandler animationHandler = new ScaleDownAnimationHandler(context);
        if(!questionHandler.isAnswerRemoved(wrongChoice1) && !questionHandler.isAnswerRemoved(wrongChoice2)) {
            Random rand = new Random();
            int x = rand.nextInt(1);
            if(x == 0) {
                animationHandler.hide(buttons.get(wrongChoice1), this);
                questionHandler.removeAnswer(wrongChoice1);
            } else {
                animationHandler.hide(buttons.get(wrongChoice2), this);
                questionHandler.removeAnswer(wrongChoice2);
            }
        } else if(questionHandler.isAnswerRemoved(wrongChoice1)) {
            animationHandler.hide(buttons.get(wrongChoice2), this);
            questionHandler.removeAnswer(wrongChoice2);
        } else {
            animationHandler.hide(buttons.get(wrongChoice1), this);
            questionHandler.removeAnswer(wrongChoice1);
        }
    }

    @Override
    public void onViewScaledDown() {
        chargeChangeListener.onChargeDecreased();
    }
}
