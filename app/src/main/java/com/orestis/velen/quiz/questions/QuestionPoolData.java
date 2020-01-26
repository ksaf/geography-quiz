package com.orestis.velen.quiz.questions;

import com.orestis.velen.quiz.geography.DataItem;

import java.util.List;

public class QuestionPoolData {
    private List<DataItem> questionPool;
    private List<DataItem> answerPool;

    public QuestionPoolData(List<DataItem> questionPool, List<DataItem> answerPool) {
        this.questionPool = questionPool;
        this.answerPool = answerPool;
    }

    public QuestionPoolData(List<DataItem> itemPool) {
        this.questionPool = itemPool;
        this.answerPool = itemPool;
    }

    public List<DataItem> getQuestionPool() {
        return questionPool;
    }

    public List<DataItem> getAnswerPool() {
        return answerPool;
    }
}
