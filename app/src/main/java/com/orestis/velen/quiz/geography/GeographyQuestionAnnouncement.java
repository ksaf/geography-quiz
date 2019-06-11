package com.orestis.velen.quiz.geography;

import com.orestis.velen.quiz.questionText.QuestionAnnouncement;
import com.orestis.velen.quiz.questions.GameType;
import com.orestis.velen.quiz.questions.Question;

public class GeographyQuestionAnnouncement implements QuestionAnnouncement{

    @Override
    public String getQuestionAnnouncementForGameType(GameType gameType, Question question) {
        switch (gameType) {
            case TYPE_A:
                return "Which Country Is This?";
            case TYPE_B:
                return "Which Country Has This Flag?";
            case TYPE_C:
                return "Which Country\\'s Capital Is This?";
            case TYPE_D:
                return " " + question.getCorrectAnswer() + " ";
            default:
                return "Which Country Is This?";
        }
    }
}
