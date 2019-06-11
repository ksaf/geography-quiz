package com.orestis.velen.quiz.helpPowers.fiftyFifty;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.widget.Button;
import android.widget.TextView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.answerButtons.AnswerChoice;
import com.orestis.velen.quiz.helpPowers.ChargeChangeListener;
import com.orestis.velen.quiz.helpPowers.PowerType;
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.questions.QuestionHandler;

import java.util.HashMap;

public class FiftyFiftyButton implements ChargeChangeListener {

    private ConstraintLayout fiftyFiftyBtnLayout;
    private HashMap<AnswerChoice, Button> answerButtons;
    private QuestionHandler questionHandler;
    private Player player;
    private int charges;
    private TextView fiftyFiftyChargesTextView;
    private Context context;

    private FiftyFiftyButton(){}

    private void enableButton() {
        charges = player.getPowers().get(PowerType.FIFTY_FIFTY).getCharges();
        fiftyFiftyChargesTextView = fiftyFiftyBtnLayout.findViewById(R.id.fiftyFiftyCharges);
        fiftyFiftyChargesTextView.setText(String.valueOf(charges));
        if(charges < 1) {
            fiftyFiftyBtnLayout.setEnabled(false);
        }
        fiftyFiftyBtnLayout.setOnClickListener(new FiftyFiftyClickListener(questionHandler, answerButtons, this, context));
    }

    @Override
    public void onChargeDecreased() {
        charges--;
        fiftyFiftyChargesTextView.setText(String.valueOf(charges));
        fiftyFiftyBtnLayout.setEnabled(charges > 1);
    }

    @Override
    public void onChargeDurationEnd() {

    }

    public static class Builder {

        private QuestionHandler questionHandler;
        private HashMap<AnswerChoice, Button> answerButtons;
        private ConstraintLayout fiftyFiftyBtnLayout;
        private Player player;
        private Context context;

        public Builder useLayout(ConstraintLayout fiftyFiftyBtnLayout) {
            this.fiftyFiftyBtnLayout = fiftyFiftyBtnLayout;
            return this;
        }

        public Builder forPlayer(Player player) {
            this.player = player;
            return this;
        }

        public Builder withAnswerButtons(HashMap<AnswerChoice, Button> answerButtons) {
            this.answerButtons = answerButtons;
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

        public FiftyFiftyButton enable() {
            FiftyFiftyButton fiftyFiftyButton = new FiftyFiftyButton();
            fiftyFiftyButton.questionHandler = this.questionHandler;
            fiftyFiftyButton.answerButtons = this.answerButtons;
            fiftyFiftyButton.fiftyFiftyBtnLayout = this.fiftyFiftyBtnLayout;
            fiftyFiftyButton.player = this.player;
            fiftyFiftyButton.context = this.context;
            fiftyFiftyButton.enableButton();
            return fiftyFiftyButton;
        }
    }

}
