package com.orestis.velen.quiz.geography;

import android.content.Context;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.questionText.QuestionAnnouncement;
import com.orestis.velen.quiz.questions.GameType;
import com.orestis.velen.quiz.questions.Question;

public class GeographyQuestionAnnouncement implements QuestionAnnouncement{

    @Override
    public String getQuestionAnnouncementForGameType(GameType gameType, Question question, Context context) {
        switch (gameType) {
            case TYPE_OUTLINES:
                return context.getString(R.string.geo_question_type_outlines);
            case TYPE_FLAGS:
                return context.getString(R.string.geo_question_type_flags);
            case TYPE_CAPITALS:
                return question.getCorrectAnswer().getStringResource(context);
            case TYPE_MONUMENTS:
                return "";
            case TYPE_OUTLINE_TO_FLAG:
                return context.getString(R.string.geo_question_type_outline_to_flag);
            default:
                return context.getString(R.string.geo_question_type_outlines);
        }
    }
}
