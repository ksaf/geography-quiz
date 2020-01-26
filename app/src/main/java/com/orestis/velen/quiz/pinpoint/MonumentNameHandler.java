package com.orestis.velen.quiz.pinpoint;

import android.content.Context;
import android.widget.TextView;

import com.orestis.velen.quiz.questions.Question;
import com.orestis.velen.quiz.questions.QuestionChangedListener;
import com.orestis.velen.quiz.questions.QuestionHandler;

public class MonumentNameHandler implements QuestionChangedListener{

    private TextView monumentName;
    private Context context;

    public MonumentNameHandler(final TextView monumentName, QuestionHandler questionHandler, Context context) {
        this.monumentName = monumentName;
        this.context = context;
        questionHandler.registerQuestionChangedListener(this);
    }

    @Override
    public void onQuestionChanged(Question newQuestion) {
        monumentName.setText(newQuestion.getCorrectAnswer().getStringResource(context));
    }
}
