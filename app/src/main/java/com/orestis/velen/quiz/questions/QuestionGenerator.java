package com.orestis.velen.quiz.questions;

import com.orestis.velen.quiz.geography.DataItem;
import com.orestis.velen.quiz.repositories.DataRepository;
import com.orestis.velen.quiz.repositories.SampleSizeEndListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class QuestionGenerator {
    private List<DataItem> questionPoolData;
    private List<DataItem> answersPoolData;
    private List<DataItem> allAnswersPoolData;
    private SampleSizeEndListener endListener;

    QuestionGenerator(Difficulty difficulty, GameType gameType, DataRepository repository, int sampleSize, SampleSizeEndListener endListener) {
        this.endListener = endListener;
        QuestionPoolData data = repository.getDataFor(difficulty, gameType, sampleSize);
        questionPoolData = data.getQuestionPool();

        answersPoolData = data.getAnswerPool();
        allAnswersPoolData = data.getAllAnswersPool();
    }

    QuestionGenerator(int continent, GameType gameType, DataRepository repository, int sampleSize, SampleSizeEndListener endListener) {
        this.endListener = endListener;
        QuestionPoolData data = repository.getDataFor(continent, gameType, sampleSize);
        questionPoolData = data.getQuestionPool();

        answersPoolData = data.getAnswerPool();
        allAnswersPoolData = data.getAllAnswersPool();
    }

    Question generateQuestion() {
        Question q;
        List<DataItem> answers = new ArrayList<>();
        answers.addAll(Arrays.asList(new DataItem[3]));
        Random r = new Random();
        if(questionPoolData.size() < 1) {
            endListener.onSampleSizeEnd();
            return null;
        }
        int randQuestionSelected = r.nextInt(questionPoolData.size());
        DataItem question = questionPoolData.get(randQuestionSelected);
        DataItem correctAnswer = answersPoolData.get(randQuestionSelected);

        if(questionPoolData.size()>0){
            questionPoolData.remove(question);
            answersPoolData.remove(correctAnswer);
        }

        DataItem answer2 = allAnswersPoolData.get(r.nextInt(allAnswersPoolData.size()));
        DataItem answer3 = allAnswersPoolData.get(r.nextInt(allAnswersPoolData.size()));

        while (answer2.equals(correctAnswer) || answer2.getContinent() != correctAnswer.getContinent()){
            answer2 = allAnswersPoolData.get(r.nextInt(allAnswersPoolData.size()));
        }
        while (answer3.equals(correctAnswer) || answer3.equals(answer2) || answer3.getContinent() != correctAnswer.getContinent()){
            answer3 = allAnswersPoolData.get(r.nextInt(allAnswersPoolData.size()));
        }

        int randomPos = r.nextInt(3);
        answers.add(randomPos, correctAnswer);


        if(randomPos==0){
            answers.set(1,answer2);
            answers.set(2,answer3);
        }
        if(randomPos==1){
            answers.set(0,answer2);
            answers.set(2,answer3);
        }
        if(randomPos==2){
            answers.set(0,answer2);
            answers.set(1,answer3);
        }

        q = new Question(question, correctAnswer, answers);

        return q;
    }

}
