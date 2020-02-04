package com.orestis.velen.quiz.helpPowers.shield;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.card.MaterialCardView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.orestis.velen.quiz.R;
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
    private HashMap<AnswerChoice, MaterialCardView> flagAnswerButtons;
    private AnswerButtonsHandler answerButtonsHandler;
    protected boolean isShieldEnabled = false;
    private ImageView shieldImg;
    private ImageView shieldBreakingImg;
    private FrameLayout shieldGradient;
    protected ChargeChangeListener chargeChangeListener;
    private Context context;
    private ShieldPowerConfig shieldPowerConfig;
    private int turnsShieldWasKeptOn;
    private ImageView helpPowerUsedImg;
    private ImageView helpPowerUsedImgBg;

    public ShieldClickListener(QuestionHandler questionHandler, ImageView shieldImg, ImageView shieldBreakingImg, FrameLayout shieldGradient,
                               ShieldPowerConfig shieldPowerConfig, ChargeChangeListener chargeChangeListener, Context context,
                               ImageView helpPowerUsedImg, ImageView helpPowerUsedImgBg) {
        this.questionHandler = questionHandler;
        this.shieldPowerConfig = shieldPowerConfig;
        this.shieldImg = shieldImg;
        this.shieldBreakingImg = shieldBreakingImg;
        this.shieldGradient = shieldGradient;
        this.chargeChangeListener = chargeChangeListener;
        this.context = context;
        this.helpPowerUsedImg = helpPowerUsedImg;
        this.helpPowerUsedImgBg = helpPowerUsedImgBg;
        questionHandler.registerQuestionChangedListener(this);
    }

    public ShieldClickListener(QuestionHandler questionHandler, HashMap<AnswerChoice, Button> buttons,
                               ImageView shieldImg, ImageView shieldBreakingImg, FrameLayout shieldGradient, AnswerButtonsHandler answerButtonsHandler,
                               ShieldPowerConfig shieldPowerConfig, ChargeChangeListener chargeChangeListener, Context context,
                               ImageView helpPowerUsedImg, ImageView helpPowerUsedImgBg) {
        this.questionHandler = questionHandler;
        this.buttons = buttons;
        this.answerButtonsHandler = answerButtonsHandler;
        this.shieldPowerConfig = shieldPowerConfig;
        this.shieldImg = shieldImg;
        this.shieldBreakingImg = shieldBreakingImg;
        this.shieldGradient = shieldGradient;
        this.chargeChangeListener = chargeChangeListener;
        this.context = context;
        this.helpPowerUsedImg = helpPowerUsedImg;
        this.helpPowerUsedImgBg = helpPowerUsedImgBg;
        questionHandler.registerQuestionChangedListener(this);
    }

    public void setFlagAnswerButtons(HashMap<AnswerChoice, MaterialCardView> flagAnswerButtons) {
        this.flagAnswerButtons = flagAnswerButtons;
    }

    @Override
    public void onClick(View view) {
        isShieldEnabled = true;
        turnsShieldWasKeptOn = 0;
        view.setEnabled(false);
        shieldImg.setVisibility(View.VISIBLE);
        shieldGradient.setVisibility(View.VISIBLE);
        stopCurrentTurnsMistake();
        chargeChangeListener.onChargeDecreased();
        PowerImageTask powerImageTask = new PowerImageTask(R.drawable.help_second_chance_button_default, helpPowerUsedImg,
                helpPowerUsedImgBg, R.drawable.shield, 400);
        powerImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    protected void stopCurrentTurnsMistake() {
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
        WrongChoiceClickListener wrongChoiceClickListener1;
        WrongChoiceClickListener wrongChoiceClickListener2;
        if(flagAnswerButtons != null) {
            wrongChoiceClickListener1 = new WrongChoiceClickListener(this, context, this, questionHandler,
                    wrongChoice1, flagAnswerButtons.get(wrongChoice1));
            wrongChoiceClickListener2 = new WrongChoiceClickListener(this, context, this, questionHandler,
                    wrongChoice2, flagAnswerButtons.get(wrongChoice2));
        } else {
            wrongChoiceClickListener1 = new WrongChoiceClickListener(this, context, this, questionHandler, wrongChoice1);
            wrongChoiceClickListener2 = new WrongChoiceClickListener(this, context, this, questionHandler, wrongChoice2);
        }

        buttons.get(wrongChoice1).setOnClickListener(wrongChoiceClickListener1);
        buttons.get(wrongChoice2).setOnClickListener(wrongChoiceClickListener2);
    }

    @Override
    public void onExtraTryEnd() {
        isShieldEnabled = false;
        animateBreakingShield();
        if(answerButtonsHandler != null) {
            answerButtonsHandler.init();
        }
        chargeChangeListener.onChargeDurationEnd();
    }

    @Override
    public void animateBreakingShield() {
        BreakingShieldTask task = new BreakingShieldTask(shieldImg, shieldBreakingImg, shieldGradient, 700);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        PowerImageTask powerImageTask = new PowerImageTask(R.drawable.help_second_chance_button_default, helpPowerUsedImg,
                helpPowerUsedImgBg, R.drawable.shield_broken, 700);
        powerImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onQuestionChanged(Question question) {
        turnsShieldWasKeptOn++;
        if(!isShieldEnabled) {
            return;
        }
        if(turnsShieldWasKeptOn < shieldPowerConfig.getTurnsDuration()) {
            if(answerButtonsHandler != null) {
                answerButtonsHandler.init();
            }
            stopCurrentTurnsMistake();
        } else {
            turnsShieldWasKeptOn = 0;
            onExtraTryEnd();
        }
    }
}
