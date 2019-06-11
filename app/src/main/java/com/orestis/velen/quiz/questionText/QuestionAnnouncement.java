package com.orestis.velen.quiz.questionText;

import com.orestis.velen.quiz.questions.GameType;
import com.orestis.velen.quiz.questions.Question;

public interface QuestionAnnouncement {
    String getQuestionAnnouncementForGameType(GameType gameType, Question question);
}
