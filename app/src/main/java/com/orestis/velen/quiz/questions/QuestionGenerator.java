package com.orestis.velen.quiz.questions;

import com.orestis.velen.quiz.repositories.DataRepository;
import com.orestis.velen.quiz.repositories.SampleSizeEndListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class QuestionGenerator {
    private List<String> questionPoolData;
    private List<String> answersPoolData;
    private List<String> originalAnswersPoolData;
    private SampleSizeEndListener endListener;

    QuestionGenerator(Difficulty difficulty, GameType gameType, DataRepository repository, int sampleSize, SampleSizeEndListener endListener) {
        this.endListener = endListener;
        QuestionPoolData data = repository.getDataFor(difficulty, gameType, sampleSize);
        questionPoolData = data.getQuestionPool();

        answersPoolData = data.getAnswerPool();
        originalAnswersPoolData = new ArrayList<>(answersPoolData);
    }

    Question generateQuestion() {
        Question q;
        List<String> answers = new ArrayList<>();
        answers.addAll(Arrays.asList(new String[3]));
        Random r = new Random();
        if(questionPoolData.size() < 1) {
            endListener.onSampleSizeEnd();
            return null;
        }
        int randQuestionSelected = r.nextInt(questionPoolData.size());
        String question = questionPoolData.get(randQuestionSelected);
        String correctAnswer = answersPoolData.get(randQuestionSelected);

        if(questionPoolData.size()>0){
            questionPoolData.remove(question);
            answersPoolData.remove(correctAnswer);
        }

        String answer2 = originalAnswersPoolData.get(r.nextInt(originalAnswersPoolData.size()));
        String answer3 = originalAnswersPoolData.get(r.nextInt(originalAnswersPoolData.size()));

        while (answer2.equals(correctAnswer)){
            answer2 = originalAnswersPoolData.get(r.nextInt(originalAnswersPoolData.size()));
        }
        while (answer3.equals(correctAnswer) || answer3.equals(answer2)){
            answer3 = originalAnswersPoolData.get(r.nextInt(originalAnswersPoolData.size()));
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
