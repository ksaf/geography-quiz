package com.orestis.velen.quiz.questions;

import com.orestis.velen.quiz.answerButtons.AnswerChoice;
import com.orestis.velen.quiz.repositories.RepositoryFactory;
import com.orestis.velen.quiz.repositories.SampleSizeEndListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.orestis.velen.quiz.answerButtons.AnswerChoice.A;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.B;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.C;

public class QuestionHandler {

    private Question question;
    private QuestionGenerator questionGenerator;
    private List<QuestionChangedListener> changedListeners = new ArrayList<>();
    private RepositoryFactory repositoryFactory;
    private SampleSizeEndListener endListener;

    public QuestionHandler(RepositoryFactory repositoryFactory, SampleSizeEndListener endListener) {
        this.repositoryFactory = repositoryFactory;
        this.endListener = endListener;
    }

    public void init(Difficulty difficulty, GameType gameType, int sampleSize) {
        questionGenerator = new QuestionGenerator(difficulty, gameType, repositoryFactory.getDataRepository(), sampleSize, endListener);
        nextQuestion();
    }

    public void nextQuestion() {
        this.question = getNextQuestion();
        if(question == null) {
            return;
        }
        for(QuestionChangedListener listener : changedListeners) {
            listener.onQuestionChanged(question);
        }
    }

    public void registerQuestionChangedListener(QuestionChangedListener changedListener) {
        changedListeners.add(changedListener);
    }

    public AnswerChoice getCorrectAnswerChoice() {
        switch (question.getAvailableAnswers().indexOf(question.getCorrectAnswer())) {
            case 0:
                return A;
            case 1:
                return B;
            case 2:
                return C;
            default:
                return A;
        }
    }

    public String getQuestionString() {
        return question.getQuestionString();
    }

    public HashMap<AnswerChoice, String> getAvailableAnswers() {
        HashMap<AnswerChoice, String> answers = new HashMap<>();
        answers.put(A, question.getAvailableAnswers().get(0).replaceAll("_", " "));
        answers.put(B, question.getAvailableAnswers().get(1).replaceAll("_", " "));
        answers.put(C, question.getAvailableAnswers().get(2).replaceAll("_", " "));
        return answers;
    }

    private Question getNextQuestion() {
        return questionGenerator.generateQuestion();
    }



}
