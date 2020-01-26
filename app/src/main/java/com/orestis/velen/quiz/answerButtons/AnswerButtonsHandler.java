package com.orestis.velen.quiz.answerButtons;

public interface AnswerButtonsHandler {
    void init();
    void displayCorrectAnswer();
    void enableButtons(boolean enable);
}
