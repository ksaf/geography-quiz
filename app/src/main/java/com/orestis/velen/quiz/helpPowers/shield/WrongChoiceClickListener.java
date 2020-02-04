package com.orestis.velen.quiz.helpPowers.shield;

import android.content.Context;
import android.support.design.card.MaterialCardView;
import android.view.View;

import com.orestis.velen.quiz.answerButtons.AnswerChoice;
import com.orestis.velen.quiz.helpPowers.buttonScaleDownAnimation.ScaleDownAnimationHandler;
import com.orestis.velen.quiz.helpPowers.buttonScaleDownAnimation.ViewScaledDownListener;
import com.orestis.velen.quiz.questions.QuestionHandler;

public class WrongChoiceClickListener implements View.OnClickListener, ViewScaledDownListener {

    private ShieldEndListener shieldEndListener;
    private Context context;
    private ShieldBreakingAnimator shieldBreakingAnimator;
    private QuestionHandler questionHandler;
    private AnswerChoice wrongChoice;
    private MaterialCardView wrongChoiceBg;

    public WrongChoiceClickListener(ShieldEndListener shieldEndListener, Context context, ShieldBreakingAnimator shieldBreakingAnimator,
                                    QuestionHandler questionHandler, AnswerChoice wrongChoice) {
        this.shieldEndListener = shieldEndListener;
        this.context = context;
        this.shieldBreakingAnimator = shieldBreakingAnimator;
        this.questionHandler = questionHandler;
        this.wrongChoice = wrongChoice;
    }

    public WrongChoiceClickListener(ShieldEndListener shieldEndListener, Context context, ShieldBreakingAnimator shieldBreakingAnimator,
                                    QuestionHandler questionHandler, AnswerChoice wrongChoice, MaterialCardView wrongChoiceBg) {
        this.shieldEndListener = shieldEndListener;
        this.context = context;
        this.shieldBreakingAnimator = shieldBreakingAnimator;
        this.questionHandler = questionHandler;
        this.wrongChoice = wrongChoice;
        this.wrongChoiceBg = wrongChoiceBg;
    }

    @Override
    public void onClick(View view) {
        shieldBreakingAnimator.animateBreakingShield();
        view.setVisibility(View.INVISIBLE);
        ScaleDownAnimationHandler animationHandler = new ScaleDownAnimationHandler(context);
        animationHandler.hide(view, this);
        if(wrongChoiceBg != null) {
            animationHandler.hide(view, wrongChoiceBg, this);
            wrongChoiceBg.setVisibility(View.INVISIBLE);
        }
        questionHandler.removeAnswer(wrongChoice);
    }

    @Override
    public void onViewScaledDown() {
        shieldEndListener.onExtraTryEnd();
    }
}
