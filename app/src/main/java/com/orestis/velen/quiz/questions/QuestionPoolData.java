package com.orestis.velen.quiz.questions;

import java.util.List;

public class QuestionPoolData {
    private List<String> questionPool;
    private List<String> answerPool;

    public QuestionPoolData(List<String> questionPool, List<String> answerPool) {
        this.questionPool = questionPool;
        this.answerPool = answerPool;
    }

    public List<String> getQuestionPool() {
        return questionPool;
    }

    public List<String> getAnswerPool() {
        return answerPool;
    }
}
