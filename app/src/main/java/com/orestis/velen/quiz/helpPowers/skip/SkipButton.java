package com.orestis.velen.quiz.helpPowers.skip;

import android.support.constraint.ConstraintLayout;
import android.widget.TextView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.helpPowers.ChargeChangeListener;
import com.orestis.velen.quiz.helpPowers.PowerType;
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.questions.QuestionHandler;

public class SkipButton implements ChargeChangeListener {

    private ConstraintLayout skipBtnLayout;
    private QuestionHandler questionHandler;
    private Player player;
    private int charges;
    private TextView skipChargesTextView;

    private SkipButton(){}

    private void enableButton() {
        charges = player.getPowers().get(PowerType.SKIP).getCharges();
        skipChargesTextView = skipBtnLayout.findViewById(R.id.freePassCharges);
        skipChargesTextView.setText(String.valueOf(charges));
        if(charges < 1) {
            skipBtnLayout.setEnabled(false);
        }
        skipBtnLayout.setOnClickListener(new SkipClickListener(questionHandler, this));
    }

    @Override
    public void onChargeDecreased() {
        charges--;
        skipChargesTextView.setText(String.valueOf(charges));
        skipBtnLayout.setEnabled(charges > 1);
    }

    @Override
    public void onChargeDurationEnd() {

    }

    public static class Builder {

        private QuestionHandler questionHandler;
        private ConstraintLayout skipBtnLayout;
        private Player player;

        public Builder useLayout(ConstraintLayout skipBtnLayout) {
            this.skipBtnLayout = skipBtnLayout;
            return this;
        }

        public Builder forPlayer(Player player) {
            this.player = player;
            return this;
        }

        public Builder withQuestionHandler(QuestionHandler questionHandler) {
            this.questionHandler = questionHandler;
            return this;
        }

        public SkipButton enable() {
            SkipButton skipButton = new SkipButton();
            skipButton.questionHandler = this.questionHandler;
            skipButton.skipBtnLayout = this.skipBtnLayout;
            skipButton.player = this.player;
            skipButton.enableButton();
            return skipButton;
        }
    }

}
