package com.orestis.velen.quiz.helpPowers.extraTime;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.helpPowers.ChargeChangeListener;
import com.orestis.velen.quiz.helpPowers.PowerType;
import com.orestis.velen.quiz.helpPowers.freezeTime.FreezeTimeClickListener;
import com.orestis.velen.quiz.loadingBar.LoadingBarHandler;
import com.orestis.velen.quiz.player.Player;

public class ExtraTimeButton implements ChargeChangeListener {

    private ConstraintLayout extraTimeBtnLayout;
    private Player player;
    private int charges;
    private TextView freezeTimer;
    private TextView freezeChargesTextView;
    private ImageView freezeImg;
    private LoadingBarHandler loadingBarHandler;
    private Context context;

    private ExtraTimeButton(){}

    private void enableButton() {
        charges = player.getPowers().get(PowerType.EXTRA_TIME).getCharges();
        freezeChargesTextView = extraTimeBtnLayout.findViewById(R.id.freezeCharges);
        freezeChargesTextView.setText(String.valueOf(charges));
        if(charges < 1) {
            extraTimeBtnLayout.setEnabled(false);
        }
        extraTimeBtnLayout.setOnClickListener(new FreezeTimeClickListener(freezeTimer, freezeImg, loadingBarHandler, context, 10000, this));
    }

    @Override
    public void onChargeDecreased() {
        charges--;
        freezeChargesTextView.setText(String.valueOf(charges));
    }

    @Override
    public void onChargeDurationEnd() {
        extraTimeBtnLayout.setEnabled(charges > 1);
    }

    public static class Builder {

        private ConstraintLayout extraTimeBtnLayout;
        private Player player;
        private TextView freezeTimer;
        private ImageView freezeImg;
        private LoadingBarHandler loadingBarHandler;
        private Context context;

        public Builder useLayout(ConstraintLayout extraTimeBtnLayout) {
            this.extraTimeBtnLayout = extraTimeBtnLayout;
            return this;
        }

        public Builder useContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder forPlayer(Player player) {
            this.player = player;
            return this;
        }

        public Builder withLoadingBarHandler(LoadingBarHandler loadingBarHandler) {
            this.loadingBarHandler = loadingBarHandler;
            return this;
        }

        public Builder withTimerText(TextView freezeTimer) {
            this.freezeTimer = freezeTimer;
            return this;
        }

        public Builder withPowerIcon(ImageView freezeImg) {
            this.freezeImg = freezeImg;
            return this;
        }

        public ExtraTimeButton enable() {
            ExtraTimeButton freezeTimeButton = new ExtraTimeButton();
            freezeTimeButton.freezeImg = this.freezeImg;
            freezeTimeButton.freezeTimer = this.freezeTimer;
            freezeTimeButton.extraTimeBtnLayout = this.extraTimeBtnLayout;
            freezeTimeButton.player = this.player;
            freezeTimeButton.loadingBarHandler = this.loadingBarHandler;
            freezeTimeButton.context = this.context;
            freezeTimeButton.enableButton();
            return freezeTimeButton;
        }
    }

}
