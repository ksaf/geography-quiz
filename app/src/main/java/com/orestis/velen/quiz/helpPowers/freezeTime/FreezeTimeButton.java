package com.orestis.velen.quiz.helpPowers.freezeTime;

import android.content.Context;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.helpPowers.ChargeChangeListener;
import com.orestis.velen.quiz.helpPowers.shield.PowerImageTask;
import com.orestis.velen.quiz.loadingBar.LoadingBarHandler;
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

import static com.orestis.velen.quiz.helpPowers.PowerType.FREEZE_TIME;

public class FreezeTimeButton implements ChargeChangeListener {

    private ConstraintLayout freezeTimeBtnLayout;
    private Player player;
    private int charges;
    private TextView freezeTimer;
    private TextView freezeChargesTextView;
    private ImageView freezeImg;
    private LoadingBarHandler loadingBarHandler;
    private Context context;
    private ImageView helpPowerUsedImg;
    private ImageView helpPowerUsedImgBg;
    private SoundPoolHelper soundHelper;

    private FreezeTimeButton(){}

    private void enableButton() {
        charges = player.getPowers().get(FREEZE_TIME).charges();
        freezeChargesTextView = freezeTimeBtnLayout.findViewById(R.id.freezeCharges);
        freezeChargesTextView.setText(String.valueOf(charges));
        if(charges < 1) {
            freezeTimeBtnLayout.setEnabled(false);
        }
        FreezeTimePowerConfigs freezeTimePowerConfigs = new FreezeTimePowerConfigs();
        int duration = freezeTimePowerConfigs.getTurnDurationForPowerLevel(player.getPowers().get(FREEZE_TIME).getPowerLevel());
        freezeTimeBtnLayout.setOnClickListener(new FreezeTimeClickListener(freezeTimer, freezeImg,
                loadingBarHandler, context, duration, this));
    }

    @Override
    public void onChargeDecreased() {
        soundHelper.playInGamePowerEnableSound();
        soundHelper.playInGameFreezePowerSound();
        charges--;
        freezeChargesTextView.setText(String.valueOf(charges));
        PowerImageTask powerImageTask = new PowerImageTask(R.drawable.help_stop_time_button_default,
                helpPowerUsedImg, helpPowerUsedImgBg, R.drawable.freeze_icon, 400);
        powerImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onChargeDurationEnd() {
        freezeTimeBtnLayout.setEnabled(charges >= 1);
    }

    public static class Builder {

        private ConstraintLayout freezeTimeBtnLayout;
        private Player player;
        private TextView freezeTimer;
        private ImageView freezeImg;
        private LoadingBarHandler loadingBarHandler;
        private Context context;
        private ImageView helpPowerUsedImg;
        private ImageView helpPowerUsedImgBg;
        private SoundPoolHelper soundHelper;

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

        public Builder useHelpPowerUsedImg(ImageView helpPowerUsedImg) {
            this.helpPowerUsedImg = helpPowerUsedImg;
            return this;
        }

        public Builder useHelpPowerUsedImgBg(ImageView helpPowerUsedImgBg) {
            this.helpPowerUsedImgBg = helpPowerUsedImgBg;
            return this;
        }

        public Builder withSoundPoolHelper(SoundPoolHelper soundHelper) {
            this.soundHelper = soundHelper;
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
            freezeTimeButton.helpPowerUsedImg = this.helpPowerUsedImg;
            freezeTimeButton.helpPowerUsedImgBg = this.helpPowerUsedImgBg;
            freezeTimeButton.soundHelper = this.soundHelper;
            freezeTimeButton.enableButton();
            return freezeTimeButton;
        }
    }

}
