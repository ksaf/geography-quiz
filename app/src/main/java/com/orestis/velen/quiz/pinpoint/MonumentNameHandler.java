package com.orestis.velen.quiz.pinpoint;

import android.widget.TextView;

import com.orestis.velen.quiz.geography.MonumentNames;
import com.orestis.velen.quiz.questions.Question;
import com.orestis.velen.quiz.questions.QuestionChangedListener;
import com.orestis.velen.quiz.questions.QuestionHandler;

public class MonumentNameHandler implements QuestionChangedListener{

    private TextView monumentName;
    private MonumentNames monumentNames;

    public MonumentNameHandler(final TextView monumentName, QuestionHandler questionHandler) {
        this.monumentName = monumentName;
        monumentNames = new MonumentNames();
        questionHandler.registerQuestionChangedListener(this);
    }

    @Override
    public void onQuestionChanged(Question newQuestion) {
        String nextMonumentName = monumentNames.getMonumentNameFromCode(newQuestion.getCorrectAnswer());
        monumentName.setText(nextMonumentName);
    }
}
