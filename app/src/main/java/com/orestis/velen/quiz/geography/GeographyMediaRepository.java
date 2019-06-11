package com.orestis.velen.quiz.geography;

import com.orestis.velen.quiz.questions.GameType;
import com.orestis.velen.quiz.repositories.MediaRepository;

public class GeographyMediaRepository implements MediaRepository{

    @Override
    public String getImagePathFor(String questionDisplay, GameType gameType) {
        switch (gameType) {
            case TYPE_A:
                return questionDisplay;
            case TYPE_B:
                return questionDisplay;
            case TYPE_C:
                return questionDisplay + "_flag";
            case TYPE_D:
                return questionDisplay;
            default:
                return questionDisplay;
        }
    }
}
