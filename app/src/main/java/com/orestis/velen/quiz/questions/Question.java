package com.orestis.velen.quiz.questions;

import com.orestis.velen.quiz.geography.Continents;
import com.orestis.velen.quiz.pinpoint.Coordinates;

import java.util.List;

public class Question {
    private List<String> availableAnswers;
    private String correctAnswer;
    private String questionString;

    public Question(String questionString, String correctAnswer, List<String> availableAnswers) {
        this.availableAnswers = availableAnswers;
        this.correctAnswer = correctAnswer;
        this.questionString = questionString;
    }

    public List<String> getAvailableAnswers() {
        return availableAnswers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getQuestionString() {
        return questionString;
    }

    public Coordinates getCorrectCoordinates() {
        return new Coordinates(correctAnswer);
    }

    public int getCorrectAnswerContinentResource() {
        return Continents.getCorrectAnswerContinentResource(correctAnswer);
    }

}
