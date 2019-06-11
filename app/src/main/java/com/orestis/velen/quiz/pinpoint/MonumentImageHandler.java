package com.orestis.velen.quiz.pinpoint;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.orestis.velen.quiz.questions.Question;
import com.orestis.velen.quiz.questions.QuestionChangedListener;
import com.orestis.velen.quiz.questions.QuestionHandler;

public class MonumentImageHandler implements QuestionChangedListener {

    private ImageView landmark;
    private Context context;
    private FrameResizeHandler frameResizeHandler;

    public MonumentImageHandler(ImageView landmark, QuestionHandler questionHandler, FrameResizeHandler frameResizeHandler, Context context) {
        this.landmark = landmark;
        this.context = context;
        this.frameResizeHandler = frameResizeHandler;
        questionHandler.registerQuestionChangedListener(this);
    }

    @Override
    public void onQuestionChanged(Question newQuestion) {
        Log.d("question: ", newQuestion.getCorrectAnswer());
        int resID = context.getResources().getIdentifier(newQuestion.getCorrectAnswer() , "drawable", context.getPackageName());
        landmark.setImageResource(resID);
        frameResizeHandler.onFrameResizeRequired();
    }
}
