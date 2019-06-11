package com.orestis.velen.quiz.helpPowers.freezeTime;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.helpPowers.ChargeChangeListener;
import com.orestis.velen.quiz.helpPowers.PowerType;
import com.orestis.velen.quiz.loadingBar.LoadingBarHandler;
import com.orestis.velen.quiz.player.Player;

public class FreezeTimeButton implements ChargeChangeListener {

    private ConstraintLayout freezeTimeBtnLayout;
    private Player player;
    private int charges;
    private TextView freezeTimer;
    private TextView freezeChargesTextView;
    private ImageView freezeImg;
    private LoadingBarHandler loadingBarHandler;
    private Context context;

    private FreezeTimeButton(){}

    private void enableButton() {
        charges = player.getPowers().get(PowerType.FREEZE_TIME).getCharges();
        freezeChargesTextView = freezeTimeBtnLayout.findViewById(R.id.freezeCharges);
        freezeChargesTextView.setText(String.valueOf(charges));
        if(charges < 1) {
            freezeTimeBtnLayout.setEnabled(false);
        }
        freezeTimeBtnLayout.setOnClickListener(new FreezeTimeClickListener(freezeTimer, freezeImg, loadingBarHandler, context, 10000, this));
    }

    @Override
    public void onChargeDecreased() {
        charges--;
        freezeChargesTextView.setText(String.valueOf(charges));
    }

    @Override
    public void onChargeDurationEnd() {
        freezeTimeBtnLayout.setEnabled(charges > 1);
    }

    public static class Builder {

        private ConstraintLayout freezeTimeBtnLayout;
        private Player player;
        private TextView freezeTimer;
        private ImageView freezeImg;
        private LoadingBarHandler loadingBarHandler;
        private Context context;

        public Builder useLayout(ConstraintLayout freezeTimeBtnLayout) {
            this.freezeTimeBtnLayout = freezeTimeBtnLayout;
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

        public FreezeTimeButton enable() {
            FreezeTimeButton freezeTimeButton = new FreezeTimeButton();
            freezeTimeButton.freezeImg = this.freezeImg;
            freezeTimeButton.freezeTimer = this.freezeTimer;
            freezeTimeButton.freezeTimeBtnLayout = this.freezeTimeBtnLayout;
            freezeTimeButton.player = this.player;
            freezeTimeButton.loadingBarHandler = this.loadingBarHandler;
            freezeTimeButton.context = this.context;
            freezeTimeButton.enableButton();
            return freezeTimeButton;
        }
    }

}
