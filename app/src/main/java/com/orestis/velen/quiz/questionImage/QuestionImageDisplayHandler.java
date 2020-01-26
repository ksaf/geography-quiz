package com.orestis.velen.quiz.questionImage;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.orestis.velen.quiz.questions.GameType;
import com.orestis.velen.quiz.questions.Question;
import com.orestis.velen.quiz.questions.QuestionChangedListener;
import com.orestis.velen.quiz.questions.QuestionHandler;

import static com.orestis.velen.quiz.questions.GameType.TYPE_FLAGS;

public class QuestionImageDisplayHandler implements QuestionChangedListener {

    private ImageView questionDisplay;
    private GameType gameType;
    private QuestionHandler questionHandler;
    private Context context;

    private QuestionImageDisplayHandler() {}

    private void init() {
        questionHandler.registerQuestionChangedListener(this);
    }

    @Override
    public void onQuestionChanged(Question question) {
        boolean isFlags = gameType == TYPE_FLAGS;
        Glide.with(context).load(question.getQuestionItem().getItemDrawableId(context, isFlags)).into(questionDisplay);
    }

    public static class Builder {
        private ImageView questionDisplay;
        private GameType gameType;
        private QuestionHandler questionHandler;
        private Context context;

        public Builder forImageView(ImageView questionDisplay) {
            this.questionDisplay = questionDisplay;
            return this;
        }

        public Builder gameOf(GameType gameType) {
            this.gameType = gameType;
            return this;
        }

        public Builder withQuestionHandler(QuestionHandler questionHandler) {
            this.questionHandler = questionHandler;
            return this;
        }

        public Builder withContext(Context context) {
            this.context = context;
            return this;
        }

        public QuestionImageDisplayHandler buildAndInit() {
            QuestionImageDisplayHandler questionDisplayHandler = new QuestionImageDisplayHandler();
            questionDisplayHandler.questionDisplay = this.questionDisplay;
            questionDisplayHandler.gameType = this.gameType;
            questionDisplayHandler.questionHandler = this.questionHandler;
            questionDisplayHandler.context = this.context;

            questionDisplayHandler.init();
            return questionDisplayHandler;
        }
    }
}
