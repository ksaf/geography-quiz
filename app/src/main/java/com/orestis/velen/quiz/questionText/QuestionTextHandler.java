package com.orestis.velen.quiz.questionText;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.orestis.velen.quiz.questions.GameType;
import com.orestis.velen.quiz.questions.Question;
import com.orestis.velen.quiz.questions.QuestionChangedListener;
import com.orestis.velen.quiz.questions.QuestionHandler;

public class QuestionTextHandler implements QuestionChangedListener {

    private Typeface face;
    private TextView questionText;
    private QuestionAnnouncement questionAnnouncement;
    private GameType gameType;
    private Context context;

    public QuestionTextHandler(Typeface face, TextView questionText, QuestionHandler questionHandler,
                               QuestionAnnouncement questionAnnouncement, GameType gameType, Context context) {
        this.context = context;
        this.face = face;
        this.questionText = questionText;
        this.questionAnnouncement = questionAnnouncement;
        this.gameType = gameType;
        questionHandler.registerQuestionChangedListener(this);
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public void onQuestionChanged(Question newQuestion) {
        questionText.setText(questionAnnouncement.getQuestionAnnouncementForGameType(gameType, newQuestion, context));
    }
}
