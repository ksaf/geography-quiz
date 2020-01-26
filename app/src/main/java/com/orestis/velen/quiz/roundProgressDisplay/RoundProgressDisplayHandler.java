package com.orestis.velen.quiz.roundProgressDisplay;

import android.graphics.Typeface;
import android.widget.TextView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.questions.Question;
import com.orestis.velen.quiz.questions.QuestionChangedListener;
import com.orestis.velen.quiz.questions.QuestionHandler;

public class RoundProgressDisplayHandler implements QuestionChangedListener{

    private TextView currentQuestionTxt;
    private int sampleSize;
    private int currentQuestionNumber = 1;

    public RoundProgressDisplayHandler(TextView questionProgressTxt, TextView currentQuestionTxt, int sampleSize, QuestionHandler questionHandler, Typeface face) {
        this.currentQuestionTxt = currentQuestionTxt;
        this.sampleSize = sampleSize;
//        questionProgressTxt.setTypeface(face);
//        currentQuestionTxt.setTypeface(face);
        questionProgressTxt.setText(R.string.question);
        questionHandler.registerQuestionChangedListener(this);
        currentQuestionTxt.setText(" " + currentQuestionNumber + " / " + sampleSize + " ");
    }


    @Override
    public void onQuestionChanged(Question question) {
        currentQuestionNumber++;
        currentQuestionTxt.setText(" " + currentQuestionNumber + " / " + sampleSize + " ");
    }
}
