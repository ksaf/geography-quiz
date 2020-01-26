package com.orestis.velen.quiz.geography;

import com.orestis.velen.quiz.questions.GameType;
import com.orestis.velen.quiz.repositories.MediaRepository;

public class GeographyMediaRepository implements MediaRepository{

    @Override
    public String getImagePathFor(String questionDisplay, GameType gameType) {
        switch (gameType) {
            case TYPE_OUTLINES:
                return questionDisplay;
            case TYPE_FLAGS:
                return questionDisplay + "_flag";
            case TYPE_CAPITALS_TEXT:
            case TYPE_CAPITALS_MAP:
                return questionDisplay;
            case TYPE_OUTLINE_TO_FLAG:
                return questionDisplay;
            default:
                return questionDisplay;
        }
    }
}
