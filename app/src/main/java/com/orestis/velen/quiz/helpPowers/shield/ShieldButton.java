package com.orestis.velen.quiz.helpPowers.shield;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.answerButtons.AnswerButtonsHandler;
import com.orestis.velen.quiz.answerButtons.AnswerChoice;
import com.orestis.velen.quiz.helpPowers.ChargeChangeListener;
import com.orestis.velen.quiz.helpPowers.PowerType;
import com.orestis.velen.quiz.pinpoint.MapTouchListener;
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.questions.QuestionHandler;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

import java.util.HashMap;

public class ShieldButton implements ChargeChangeListener {

    private ConstraintLayout shieldBtnLayout;
    private HashMap<AnswerChoice, Button> answerButtons;
    private AnswerButtonsHandler answerButtonsHandler;
    private QuestionHandler questionHandler;
    private Player player;
    private ImageView shieldOnIcon;
    private ImageView shieldBreakingIcon;
    private FrameLayout shieldOverlay;
    private int charges;
    private TextView shieldTextView;
    private Context context;
    private MapTouchListener mapTouchListener;
    private ImageView helpPowerUsedImg;
    private ImageView helpPowerUsedImgBg;
    private SoundPoolHelper soundHelper;

    private ShieldButton(){}

    private void enableButton() {
        charges = player.getPowers().get(PowerType.SHIELD).charges();
        shieldTextView = shieldBtnLayout.findViewById(R.id.shieldCharges);
        shieldTextView.setText(String.valueOf(charges));
        ShieldPowerConfig shieldPowerConfig = new ShieldPowerConfig(player);
        if(charges < 1) {
            shieldBtnLayout.setEnabled(false);
        }
        if(answerButtons != null) {
            shieldBtnLayout.setOnClickListener(new ShieldClickListener(questionHandler, answerButtons,
                    shieldOnIcon, shieldBreakingIcon, shieldOverlay, answerButtonsHandler, shieldPowerConfig,
                    this, context, helpPowerUsedImg, helpPowerUsedImgBg));
        } else if (mapTouchListener != null){
            shieldBtnLayout.setOnClickListener(new ShieldOnMapClickListener(questionHandler,
                    shieldOnIcon, shieldBreakingIcon, shieldOverlay, shieldPowerConfig,
                    this, context, mapTouchListener, helpPowerUsedImg, helpPowerUsedImgBg));
        }

    }

    @Override
    public void onChargeDecreased() {
        soundHelper.playInGamePowerEnableSound();
        charges--;
        shieldTextView.setText(String.valueOf(charges));
    }

    @Override
    public void onChargeDurationEnd() {
        shieldBtnLayout.setEnabled(charges >= 1);
    }

    public static class Builder {

        private ConstraintLayout shieldBtnLayout;
        private HashMap<AnswerChoice, Button> answerButtons;
        private AnswerButtonsHandler answerButtonsHandler;
        private QuestionHandler questionHandler;
        private Player player;
        private ImageView shieldOnIcon;
        private ImageView shieldBreakingIcon;
        private FrameLayout shieldOverlay;
        private Context context;
        private MapTouchListener mapTouchListener;
        private ImageView helpPowerUsedImg;
        private ImageView helpPowerUsedImgBg;
        private SoundPoolHelper soundHelper;

        public Builder useLayout(ConstraintLayout shieldBtnLayout) {
            this.shieldBtnLayout = shieldBtnLayout;
            return this;
        }

        public Builder useShieldOnIcon(ImageView shieldOnIcon) {
            this.shieldOnIcon = shieldOnIcon;
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

        public Builder useShieldBreakingIcon(ImageView shieldBreakingIcon) {
            this.shieldBreakingIcon = shieldBreakingIcon;
            return this;
        }

        public Builder useShieldOverlay(FrameLayout shieldOverlay) {
            this.shieldOverlay = shieldOverlay;
            return this;
        }

        public Builder forPlayer(Player player) {
            this.player = player;
            return this;
        }

        public Builder withContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder withAnswerButtons(HashMap<AnswerChoice, Button> answerButtons) {
            this.answerButtons = answerButtons;
            return this;
        }

        public Builder withAnswerButtonsHandler(AnswerButtonsHandler answerButtonsHandler) {
            this.answerButtonsHandler = answerButtonsHandler;
            return this;
        }

        public Builder withQuestionHandler(QuestionHandler questionHandler) {
            this.questionHandler = questionHandler;
            return this;
        }

        public Builder withMapTouchListener(MapTouchListener mapTouchListener) {
            this.mapTouchListener = mapTouchListener;
            return this;
        }

        public Builder withSoundPoolHelper(SoundPoolHelper soundHelper) {
            this.soundHelper = soundHelper;
            return this;
        }

        public ShieldButton enable() {
            ShieldButton shieldButton = new ShieldButton();
            shieldButton.questionHandler = this.questionHandler;
            shieldButton.answerButtons = this.answerButtons;
            shieldButton.shieldBtnLayout = this.shieldBtnLayout;
            shieldButton.player = this.player;
            shieldButton.answerButtonsHandler = this.answerButtonsHandler;
            shieldButton.shieldOnIcon = this.shieldOnIcon;
            shieldButton.shieldBreakingIcon = this.shieldBreakingIcon;
            shieldButton.shieldOverlay = this.shieldOverlay;
            shieldButton.context = this.context;
            shieldButton.mapTouchListener = this.mapTouchListener;
            shieldButton.helpPowerUsedImg = this.helpPowerUsedImg;
            shieldButton.helpPowerUsedImgBg = this.helpPowerUsedImgBg;
            shieldButton.soundHelper = this.soundHelper;
            shieldButton.enableButton();
            return shieldButton;
        }
    }

}
