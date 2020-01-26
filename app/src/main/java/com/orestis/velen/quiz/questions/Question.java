package com.orestis.velen.quiz.questions;

import com.orestis.velen.quiz.geography.DataItem;

import java.util.List;

public class Question {
    private List<DataItem> availableAnswers;
    private DataItem correctAnswerItem;
    private DataItem questionItem;

    public Question(DataItem questionItem, DataItem correctAnswerItem, List<DataItem> availableAnswers) {
        this.availableAnswers = availableAnswers;
        this.correctAnswerItem = correctAnswerItem;
        this.questionItem = questionItem;
    }

    public List<DataItem> getAvailableAnswers() {
        return availableAnswers;
    }

    public DataItem getCorrectAnswer() {
        return correctAnswerItem;
    }

    public DataItem getQuestionItem() {
        return questionItem;
    }

}
