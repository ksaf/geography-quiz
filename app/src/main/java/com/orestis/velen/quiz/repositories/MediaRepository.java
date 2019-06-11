package com.orestis.velen.quiz.repositories;

import com.orestis.velen.quiz.questions.GameType;

public interface MediaRepository {

    public String getImagePathFor(String questionDisplay, GameType gameType);
}
