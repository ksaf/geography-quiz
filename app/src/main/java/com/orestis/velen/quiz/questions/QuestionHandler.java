package com.orestis.velen.quiz.questions;

import android.content.Context;

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
    private Context context;
    private HashMap<AnswerChoice, Boolean> removedAnswers;

    public QuestionHandler(RepositoryFactory repositoryFactory, SampleSizeEndListener endListener, Context context) {
        this.repositoryFactory = repositoryFactory;
        this.endListener = endListener;
        this.context = context;
    }

    public void init(Difficulty difficulty, GameType gameType, int sampleSize) {
        questionGenerator = new QuestionGenerator(difficulty, gameType, repositoryFactory.getDataRepository(), sampleSize, endListener);
        nextQuestion();
    }

    public void init(int continent, GameType gameType, int sampleSize) {
        questionGenerator = new QuestionGenerator(continent, gameType, repositoryFactory.getDataRepository(), sampleSize, endListener);
        nextQuestion();
    }

    public void nextQuestion() {
        removedAnswers = new HashMap<>();
        removedAnswers.put(A, false);
        removedAnswers.put(B, false);
        removedAnswers.put(C, false);
        this.question = getNextQuestion();
        if(question == null) {
            return;
        }
        for(QuestionChangedListener listener : changedListeners) {
            listener.onQuestionChanged(question);
        }
    }

    public Question getQuestion() {
        return question;
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

    public void removeAnswer(AnswerChoice answerChoice) {
        removedAnswers.put(answerChoice, true);
    }

    public boolean isAnswerRemoved(AnswerChoice answerChoice) {
        return removedAnswers.get(answerChoice);
    }

    public HashMap<AnswerChoice, String> getAvailableAnswers() {
        HashMap<AnswerChoice, String> answers = new HashMap<>();
        answers.put(A, question.getAvailableAnswers().get(0).getStringResource(context));
        answers.put(B, question.getAvailableAnswers().get(1).getStringResource(context));
        answers.put(C, question.getAvailableAnswers().get(2).getStringResource(context));
        return answers;
    }

    public HashMap<AnswerChoice, Integer> getAvailableFlagAnswers() {
        HashMap<AnswerChoice, Integer> answers = new HashMap<>();
        answers.put(A, question.getAvailableAnswers().get(0).getItemDrawableId(context, true));
        answers.put(B, question.getAvailableAnswers().get(1).getItemDrawableId(context, true));
        answers.put(C, question.getAvailableAnswers().get(2).getItemDrawableId(context, true));
        return answers;
    }

    private Question getNextQuestion() {
        return questionGenerator.generateQuestion();
    }

}
