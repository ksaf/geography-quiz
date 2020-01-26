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

//    public QuestionPoolData getDataFor(Difficulty difficulty, GameType gameType, int sampleSize) {
//        List<DataItem> countries = dataItemManager.getAllItems(TYPE_COUNTRY, difficulty);
//        List<DataItem> capitals = dataItemManager.getAllItems(TYPE_CAPITAL, difficulty);
//        List<DataItem> monuments = dataItemManager.getAllItems(TYPE_MONUMENT, difficulty);
//        try {
//            GeographyItemsSampleSizer sampleSizer;
//            switch (gameType) {
//                case TYPE_OUTLINES:
//                    sampleSizer = new GeographyItemsSampleSizer(countries, capitals, sampleSize);
//                    return new QuestionPoolData(sampleSizer.getFirst(), sampleSizer.getFirst());
//                case TYPE_FLAGS:
//                    sampleSizer = new GeographyItemsSampleSizer(countries, capitals, sampleSize);
//                    return new QuestionPoolData(sampleSizer.getFirst(), sampleSizer.getFirst());
//                case TYPE_CAPITALS_MAP:
//                    sampleSizer = new GeographyItemsSampleSizer(countries, capitals, sampleSize);
//                    return new QuestionPoolData(sampleSizer.getFirst(), sampleSizer.getSecond());
//                case TYPE_OUTLINE_TO_FLAG:
//                    sampleSizer = new GeographyItemsSampleSizer(countries, capitals, sampleSize);
//                    return new QuestionPoolData(sampleSizer.getFirst(), sampleSizer.getFirst());
//                case TYPE_MONUMENTS:
//                    GeographyItemsSampleSizer landmarkSampleSizer = new GeographyItemsSampleSizer(monuments, sampleSize);
//                    return new QuestionPoolData(landmarkSampleSizer.getSingle());
//            }
//        } catch (ListSizeMismatchException e) {
//            throw new RuntimeException(e);
//        }
//        return null;
//    }

}
