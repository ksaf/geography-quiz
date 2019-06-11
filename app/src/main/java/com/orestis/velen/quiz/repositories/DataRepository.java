package com.orestis.velen.quiz.repositories;

import com.orestis.velen.quiz.questions.Difficulty;
import com.orestis.velen.quiz.questions.GameType;
import com.orestis.velen.quiz.questions.QuestionPoolData;

public interface DataRepository {
    QuestionPoolData getDataFor(Difficulty difficulty, GameType gameType, int sampleSize);
}
