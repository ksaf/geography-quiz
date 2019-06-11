package com.orestis.velen.quiz.questionText;

import com.orestis.velen.quiz.geography.GeographyQuestionAnnouncement;
import com.orestis.velen.quiz.repositories.GameTheme;

public class QuestionAnnouncementFactory {

    public QuestionAnnouncement getQuestionAnouncement(GameTheme theme) {
        switch (theme) {
            case GEOGRAPHY:
                return new GeographyQuestionAnnouncement();
            default:
                return new GeographyQuestionAnnouncement();
        }
    }
}
