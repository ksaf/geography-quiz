package com.orestis.velen.quiz.capitals;

import android.content.Context;
import android.widget.TextView;

import com.orestis.velen.quiz.questions.GameType;
import com.orestis.velen.quiz.questions.Question;
import com.orestis.velen.quiz.questions.QuestionChangedListener;
import com.orestis.velen.quiz.questions.QuestionHandler;

public class CapitalTextDisplayHandler implements QuestionChangedListener {

    private TextView questionDisplay;
    private GameType gameType;
    private QuestionHandler questionHandler;
    private Context context;

    private CapitalTextDisplayHandler() {}

    private void init() {
        questionHandler.registerQuestionChangedListener(this);
    }

    @Override
    public void onQuestionChanged(Question question) {
        questionDisplay.setText(question.getQuestionItem().getStringResource(context));
    }

    public static class Builder {
        private TextView questionDisplay;
        private GameType gameType;
        private QuestionHandler questionHandler;
        private Context context;

        public Builder forTextView(TextView questionDisplay) {
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

        public CapitalTextDisplayHandler buildAndInit() {
            CapitalTextDisplayHandler capitalTextDisplayHandler = new CapitalTextDisplayHandler();
            capitalTextDisplayHandler.questionDisplay = this.questionDisplay;
            capitalTextDisplayHandler.gameType = this.gameType;
            capitalTextDisplayHandler.questionHandler = this.questionHandler;
            capitalTextDisplayHandler.context = this.context;

            capitalTextDisplayHandler.init();
            return capitalTextDisplayHandler;
        }
    }
}
