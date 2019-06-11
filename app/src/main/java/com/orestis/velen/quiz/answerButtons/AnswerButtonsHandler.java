package com.orestis.velen.quiz.answerButtons;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

import com.orestis.velen.quiz.questions.Question;
import com.orestis.velen.quiz.questions.QuestionChangedListener;
import com.orestis.velen.quiz.questions.QuestionHandler;

import java.util.HashMap;
import java.util.Map;

import static com.orestis.velen.quiz.answerButtons.AnswerChoice.A;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.B;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.C;

public class AnswerButtonsHandler implements AnswerButtonPressedListener, QuestionChangedListener{

    private QuestionHandler questionHandler;
    private HashMap<AnswerChoice, Button> buttons;
    private AnswerGivenListener answerGivenListener;
    private AnswerButtonStateListener buttonStateListener;
    private AnswerTextSizeHandler answerTextSizeHandler;
    private int displayAnswerDuration;
    private Typeface face;

    private AnswerButtonsHandler() {}

    public void init() {
        AnswerButtonClickListener listener = new AnswerButtonClickListener(this);
        buttons.get(A).setOnClickListener(listener);
        buttons.get(B).setOnClickListener(listener);
        buttons.get(C).setOnClickListener(listener);
        buttons.get(A).setTypeface(face);
        buttons.get(B).setTypeface(face);
        buttons.get(C).setTypeface(face);
        answerTextSizeHandler = new AnswerTextSizeHandler(buttons);
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

    private void displayCorrectChoice(AnswerChoice answerChoice) {
        CorrectAnswerTask task = new CorrectAnswerTask(buttons, answerChoice, displayAnswerDuration, buttonStateListener);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void displayWrongChoice(AnswerChoice answerChoice, AnswerChoice correctChoice) {
        WrongAnswerTask task = new WrongAnswerTask(buttons, answerChoice, correctChoice, displayAnswerDuration, buttonStateListener);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private AnswerChoice getAnswerFromButton(View choice) {
        for(Map.Entry<AnswerChoice, Button> entry : buttons.entrySet()) {
            if (choice.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return A;
    }

    public void enableButtons(boolean enable) {
        buttons.get(A).setEnabled(enable);
        buttons.get(B).setEnabled(enable);
        buttons.get(C).setEnabled(enable);
    }

    @Override
    public void onQuestionChanged(Question question) {
        showAllButtons();
        setAnswers();
    }

    private void showAllButtons() {
        buttons.get(A).setVisibility(View.VISIBLE);
        buttons.get(B).setVisibility(View.VISIBLE);
        buttons.get(C).setVisibility(View.VISIBLE);
    }

    private void setAnswers() {
        HashMap<AnswerChoice, String> answers = questionHandler.getAvailableAnswers();
        buttons.get(A).setText(answers.get(A));
        buttons.get(B).setText(answers.get(B));
        buttons.get(C).setText(answers.get(C));
        answerTextSizeHandler.adjustTextSizeIfNeeded();
    }

    public static class Builder {

        private QuestionHandler questionHandler;
        private HashMap<AnswerChoice, Button> buttons;
        private AnswerButtonStateListener buttonStateListener;
        private AnswerGivenListener answerGivenListener;
        private int displayAnswerDuration;
        private Typeface face;

        public Builder forButtons(HashMap<AnswerChoice, Button> buttons) {
            this.buttons = buttons;
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

        public Builder withTypeFace(Typeface face) {
            this.face = face;
            return this;
        }

        public AnswerButtonsHandler buildAndInit() {
            AnswerButtonsHandler answerButtonsHandler = new AnswerButtonsHandler();
            answerButtonsHandler.questionHandler = this.questionHandler;
            answerButtonsHandler.buttons = this.buttons;
            answerButtonsHandler.buttonStateListener = this.buttonStateListener;
            answerButtonsHandler.displayAnswerDuration = this.displayAnswerDuration;
            answerButtonsHandler.answerGivenListener = this.answerGivenListener;
            answerButtonsHandler.face = this.face;

            questionHandler.registerQuestionChangedListener(answerButtonsHandler);
            answerButtonsHandler.init();
            return answerButtonsHandler;
        }
    }
}
