package com.orestis.velen.quiz.geography;

import com.orestis.velen.quiz.questions.Difficulty;
import com.orestis.velen.quiz.questions.GameType;
import com.orestis.velen.quiz.questions.QuestionPoolData;
import com.orestis.velen.quiz.repositories.DataRepository;

public class GeographyDataRepository implements DataRepository{

    private DataItemManager dataItemManager;

    public GeographyDataRepository() {
        this.dataItemManager = new DataItemManager();
    }

    public QuestionPoolData getDataFor(Difficulty difficulty, GameType gameType, int sampleSize) {
        return dataItemManager.getSample(difficulty, gameType, sampleSize);
    }

    public QuestionPoolData getDataFor(int continent, GameType gameType, int sampleSize) {
        return dataItemManager.getSample(continent, gameType, sampleSize);
    }
}
