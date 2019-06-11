package com.orestis.velen.quiz.helpPowers.shield;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.orestis.velen.quiz.answerButtons.AnswerButtonsHandler;
import com.orestis.velen.quiz.answerButtons.AnswerChoice;
import com.orestis.velen.quiz.helpPowers.ChargeChangeListener;
import com.orestis.velen.quiz.questions.Question;
import com.orestis.velen.quiz.questions.QuestionChangedListener;
import com.orestis.velen.quiz.questions.QuestionHandler;

import java.util.HashMap;

import static com.orestis.velen.quiz.answerButtons.AnswerChoice.A;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.B;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.C;

public class ShieldClickListener implements View.OnClickListener, ShieldEndListener, QuestionChangedListener, ShieldBreakingAnimator {

    private QuestionHandler questionHandler;
    private HashMap<AnswerChoice, Button> buttons;
    private AnswerButtonsHandler answerButtonsHandler;
    private boolean keepShield;
    private boolean isShieldEnabled = false;
    private ImageView shieldImg;
    private ImageView shieldBreakingImg;
    private FrameLayout shieldGradient;
    private ChargeChangeListener chargeChangeListener;
    private Context context;

    public ShieldClickListener(QuestionHandler questionHandler, HashMap<AnswerChoice, Button> buttons,
                               ImageView shieldImg, ImageView shieldBreakingImg, FrameLayout shieldGradient, AnswerButtonsHandler answerButtonsHandler,
                               boolean keepShield, ChargeChangeListener chargeChangeListener, Context context) {
        this.questionHandler = questionHandler;
        this.buttons = buttons;
        this.answerButtonsHandler = answerButtonsHandler;
        this.keepShield = keepShield;
        this.shieldImg = shieldImg;
        this.shieldBreakingImg = shieldBreakingImg;
        this.shieldGradient = shieldGradient;
        this.chargeChangeListener = chargeChangeListener;
        this.context = context;
        questionHandler.registerQuestionChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        isShieldEnabled = true;
        view.setEnabled(false);
        shieldImg.setVisibility(View.VISIBLE);
        shieldGradient.setVisibility(View.VISIBLE);
        stopCurrentQuestionMistake();
        chargeChangeListener.onChargeDecreased();
    }

    private void stopCurrentQuestionMistake() {
        AnswerChoice correctChoice = questionHandler.getCorrectAnswerChoice();
        switch (correctChoice) {
            case A:
                overrideWrongAnswersListeners(B, C);
                break;
            case B:
                overrideWrongAnswersListeners(A, C);
                break;
            case C:
                overrideWrongAnswersListeners(A, B);
                break;
        }
    }

    private void overrideWrongAnswersListeners(AnswerChoice wrongChoice1, AnswerChoice wrongChoice2) {
        buttons.get(wrongChoice1).setOnClickListener(new WrongChoiceClickListener(this, context, this));
        buttons.get(wrongChoice2).setOnClickListener(new WrongChoiceClickListener(this, context, this));
    }

    @Override
    public void onExtraTryEnd() {
        isShieldEnabled = false;
        animateBreakingShield();
        answerButtonsHandler.init();
        chargeChangeListener.onChargeDurationEnd();
    }

    @Override
    public void animateBreakingShield() {
        BreakingShieldTask task = new BreakingShieldTask(shieldImg, shieldBreakingImg, shieldGradient, 700);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onQuestionChanged(Question question) {
        if(!isShieldEnabled) {
            return;
        }
        if(keepShield) {
            answerButtonsHandler.init();
            stopCurrentQuestionMistake();
        } else {
            onExtraTryEnd();
        }
    }
}
