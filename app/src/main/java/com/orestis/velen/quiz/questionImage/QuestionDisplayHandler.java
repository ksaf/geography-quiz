package com.orestis.velen.quiz.questionImage;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.orestis.velen.quiz.questions.GameType;
import com.orestis.velen.quiz.questions.Question;
import com.orestis.velen.quiz.questions.QuestionChangedListener;
import com.orestis.velen.quiz.questions.QuestionHandler;
import com.orestis.velen.quiz.repositories.MediaRepository;

public class QuestionDisplayHandler implements QuestionChangedListener {

    private ImageView questionDisplay;
    private GameType gameType;
    private QuestionHandler questionHandler;
    private MediaRepository mediaRepository;
    private Context context;

    private QuestionDisplayHandler() {}

    private void init() {
        questionHandler.registerQuestionChangedListener(this);
    }

    @Override
    public void onQuestionChanged(Question question) {
        String displayUrl = mediaRepository.getImagePathFor(questionHandler.getQuestionString(), gameType);
        int drawableResourceId = context.getResources().getIdentifier(displayUrl, "drawable", context.getPackageName());
        Glide.with(context).load(drawableResourceId).into(questionDisplay);
    }

    public static class Builder {
        private ImageView questionDisplay;
        private GameType gameType;
        private QuestionHandler questionHandler;
        private MediaRepository mediaRepository;
        private Context context;

        public Builder forImageView(ImageView questionDisplay) {
            this.questionDisplay = questionDisplay;
            return this;
        }

        public Builder fromRepository(MediaRepository mediaRepository) {
            this.mediaRepository = mediaRepository;
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

        public QuestionDisplayHandler buildAndInit() {
            QuestionDisplayHandler questionDisplayHandler = new QuestionDisplayHandler();
            questionDisplayHandler.questionDisplay = this.questionDisplay;
            questionDisplayHandler.gameType = this.gameType;
            questionDisplayHandler.questionHandler = this.questionHandler;
            questionDisplayHandler.mediaRepository = this.mediaRepository;
            questionDisplayHandler.context = this.context;

            questionDisplayHandler.init();
            return questionDisplayHandler;
        }
    }
}
