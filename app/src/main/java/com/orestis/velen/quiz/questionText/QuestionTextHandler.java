package com.orestis.velen.quiz.questionText;

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

    public QuestionTextHandler(Typeface face, TextView questionText, QuestionHandler questionHandler, QuestionAnnouncement questionAnnouncement, GameType gameType) {
        this.face = face;
        this.questionText = questionText;
        this.questionAnnouncement = questionAnnouncement;
        this.gameType = gameType;
        questionHandler.registerQuestionChangedListener(this);
        questionText.setTypeface(face);
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    @Override
    public void onQuestionChanged(Question newQuestion) {
        questionText.setText(questionAnnouncement.getQuestionAnnouncementForGameType(gameType, newQuestion));
    }
}
