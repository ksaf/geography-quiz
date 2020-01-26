package com.orestis.velen.quiz.answerButtons;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.card.MaterialCardView;
import android.view.View;
import android.widget.Button;

import com.orestis.velen.quiz.questions.Question;
import com.orestis.velen.quiz.questions.QuestionChangedListener;
import com.orestis.velen.quiz.questions.QuestionHandler;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

import java.util.HashMap;
import java.util.Map;

import static com.orestis.velen.quiz.answerButtons.AnswerChoice.A;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.B;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.C;

public class FlagAnswerButtonsHandler implements AnswerButtonsHandler, AnswerButtonPressedListener, QuestionChangedListener {

    private QuestionHandler questionHandler;
    private HashMap<AnswerChoice, Button> buttons;
    private HashMap<AnswerChoice, MaterialCardView> buttonBackGrounds;
    private AnswerGivenListener answerGivenListener;
    private AnswerButtonStateListener buttonStateListener;
    private int displayAnswerDuration;
    private SoundPoolHelper soundHelper;
    private static final int DEFAULT = 0;
    private static final int MAP_TO_FLAGS = 1;
    private int mode;

    @Override
    public void init() {
        AnswerButtonClickListener listener = new AnswerButtonClickListener(this);
        buttons.get(A).setOnClickListener(listener);
        buttons.get(B).setOnClickListener(listener);
        buttons.get(C).setOnClickListener(listener);
    }

    @Override
    public void onQuestionChanged(Question newQuestion) {
        setAnswers();
        showAllButtons();
    }

    @Override
    public void displayCorrectAnswer() {

    }

    @Override
    public void enableButtons(boolean enable) {
        buttons.get(A).setEnabled(enable);
        buttons.get(B).setEnabled(enable);
        buttons.get(C).setEnabled(enable);
    }

    @Override
    public void onAnswerPressed(View choiceButton) {
        AnswerChoice correctChoice = questionHandler.getCorrectAnswerChoice();
        if(choiceButton.equals(buttons.get(correctChoice))) {
            answerGivenListener.onCorrectAnswerGiven();
            displayCorrectChoice(getAnswerFromButton(choiceButton));
        } else {
            answerGivenListener.onWrongAnswerGiven();
            displayWrongChoice(getAnswerFromButton(choiceButton), correctChoice);
        }
    }

    private void setAnswers() {
        HashMap<AnswerChoice, Integer> answers = questionHandler.getAvailableFlagAnswers();
        buttons.get(A).setBackgroundResource(answers.get(A));
        buttons.get(B).setBackgroundResource(answers.get(B));
        buttons.get(C).setBackgroundResource(answers.get(C));
    }

    private void showAllButtons() {
        switch (mode) {
            case DEFAULT:
                buttons.get(A).setVisibility(View.VISIBLE);
                buttons.get(B).setVisibility(View.VISIBLE);
                buttons.get(C).setVisibility(View.VISIBLE);
                break;
            case MAP_TO_FLAGS:
                buttonBackGrounds.get(A).setVisibility(View.VISIBLE);
                buttonBackGrounds.get(B).setVisibility(View.VISIBLE);
                buttonBackGrounds.get(C).setVisibility(View.VISIBLE);
                break;
        }

    }

    private AnswerChoice getAnswerFromButton(View choice) {
        for(Map.Entry<AnswerChoice, Button> entry : buttons.entrySet()) {
            if (choice.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return A;
    }

    private void displayCorrectChoice(AnswerChoice answerChoice) {
        soundHelper.playInGameCorrectChoiceSound();
        CorrectFlagAnswerTask task = new CorrectFlagAnswerTask(buttons, answerChoice,
                displayAnswerDuration, buttonStateListener, buttonBackGrounds);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void displayWrongChoice(AnswerChoice answerChoice, AnswerChoice correctChoice) {
        soundHelper.playInGameWrongChoiceSound();
        WrongFlagAnswerTask task = new WrongFlagAnswerTask(buttons, answerChoice,
                correctChoice, displayAnswerDuration, buttonStateListener, buttonBackGrounds);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static class Builder {
        private QuestionHandler questionHandler;
        private HashMap<AnswerChoice, Button> buttons;
        private HashMap<AnswerChoice, MaterialCardView> buttonBackGrounds;
        private AnswerButtonStateListener buttonStateListener;
        private AnswerGivenListener answerGivenListener;
        private int displayAnswerDuration;
        private Typeface face;
        private SoundPoolHelper soundHelper;

        public Builder forButtons(HashMap<AnswerChoice, Button> buttons) {
            this.buttons = buttons;
            return this;
        }

        public Builder forButtonBackgrounds(HashMap<AnswerChoice, MaterialCardView> buttonBackGrounds) {
            this.buttonBackGrounds = buttonBackGrounds;
            return this;
        }

        public Builder withQuestionHandler(QuestionHandler questionHandler) {
            this.questionHandler = questionHandler;
            return this;
        }

        public Builder withButtonStateListener(AnswerButtonStateListener buttonStateListener) {
            this.buttonStateListener = buttonStateListener;
            return this;
        }

        public Builder withAnswerGivenListener(AnswerGivenListener answerGivenListener) {
            this.answerGivenListener = answerGivenListener;
            return this;
        }

        public Builder displayAnswerDurationFor(int displayAnswerDuration) {
            this.displayAnswerDuration = displayAnswerDuration;
            return this;
        }

        public Builder withSoundPoolHelper(SoundPoolHelper soundHelper) {
            this.soundHelper = soundHelper;
            return this;
        }

        public FlagAnswerButtonsHandler buildAndInitDefault() {
            FlagAnswerButtonsHandler answerButtonsHandler = new FlagAnswerButtonsHandler();
            answerButtonsHandler.questionHandler = this.questionHandler;
            answerButtonsHandler.buttons = this.buttons;
            answerButtonsHandler.buttonBackGrounds = this.buttonBackGrounds;
            answerButtonsHandler.buttonStateListener = this.buttonStateListener;
            answerButtonsHandler.displayAnswerDuration = this.displayAnswerDuration;
            answerButtonsHandler.answerGivenListener = this.answerGivenListener;
            answerButtonsHandler.soundHelper = this.soundHelper;
            answerButtonsHandler.mode = DEFAULT;
            questionHandler.registerQuestionChangedListener(answerButtonsHandler);
            answerButtonsHandler.init();

            return answerButtonsHandler;
        }
        public FlagAnswerButtonsHandler buildAndInitForMapToFlags() {
            FlagAnswerButtonsHandler answerButtonsHandler = new FlagAnswerButtonsHandler();
            answerButtonsHandler.questionHandler = this.questionHandler;
            answerButtonsHandler.buttons = this.buttons;
            answerButtonsHandler.buttonBackGrounds = this.buttonBackGrounds;
            answerButtonsHandler.buttonStateListener = this.buttonStateListener;
            answerButtonsHandler.displayAnswerDuration = this.displayAnswerDuration;
            answerButtonsHandler.answerGivenListener = this.answerGivenListener;
            answerButtonsHandler.soundHelper = this.soundHelper;
            answerButtonsHandler.mode = MAP_TO_FLAGS;
            questionHandler.registerQuestionChangedListener(answerButtonsHandler);
            answerButtonsHandler.init();
            return answerButtonsHandler;
        }
    }
}
