package com.orestis.velen.quiz.questions;

import com.orestis.velen.quiz.geography.DataItem;

import java.util.List;

public class QuestionPoolData {
    private List<DataItem> questionPool;
    private List<DataItem> answerPool;
    private List<DataItem> allAnswersPool;

    public QuestionPoolData(List<DataItem> questionPool, List<DataItem> answerPool, List<DataItem> allAnswersPool) {
        this.questionPool = questionPool;
        this.answerPool = answerPool;
        this.allAnswersPool = allAnswersPool;
    }

    public List<DataItem> getQuestionPool() {
        return questionPool;
    }

    public List<DataItem> getAnswerPool() {
        return answerPool;
    }

    public List<DataItem> getAllAnswersPool() {
        return allAnswersPool;
    }
}
