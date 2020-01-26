package com.orestis.velen.quiz.helpPowers.skip;

import android.content.Context;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.StreakBonus.StreakBonusManager;
import com.orestis.velen.quiz.answerButtons.AnswerButtonsHandler;
import com.orestis.velen.quiz.helpPowers.ChargeChangeListener;
import com.orestis.velen.quiz.helpPowers.PowerType;
import com.orestis.velen.quiz.helpPowers.shield.PowerImageTask;
import com.orestis.velen.quiz.pinpoint.CorrectPointTask;
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.questions.QuestionHandler;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

import static com.orestis.velen.quiz.pinpoint.MonumentsPointActivity.DISPLAY_DISTANCE_DURATION;

public class SkipButton implements ChargeChangeListener {

    private ConstraintLayout skipBtnLayout;
    private QuestionHandler questionHandler;
    private Player player;
    private int charges;
    private TextView skipChargesTextView;
    private StreakBonusManager bonusManager;
    private AnswerButtonsHandler answerButtonsHandler;
    private ImageView map;
    private Context context;
    private ImageView helpPowerUsedImg;
    private ImageView helpPowerUsedImgBg;
    private SoundPoolHelper soundHelper;

    private SkipButton(){}

    private void enableButton() {
        charges = player.getPowers().get(PowerType.SKIP).charges();
        skipChargesTextView = skipBtnLayout.findViewById(R.id.freePassCharges);
        skipChargesTextView.setText(String.valueOf(charges));
        if(charges < 1) {
            skipBtnLayout.setEnabled(false);
        }
        skipBtnLayout.setOnClickListener(new SkipClickListener(this));
    }

    @Override
    public void onChargeDecreased() {
        soundHelper.playInGamePowerEnableSound();
        charges--;
        skipChargesTextView.setText(String.valueOf(charges));
        skipBtnLayout.setEnabled(charges >= 1);
        if(answerButtonsHandler != null) {
            answerButtonsHandler.displayCorrectAnswer();
        } else {
            CorrectPointTask task = new CorrectPointTask(DISPLAY_DISTANCE_DURATION, questionHandler, map, context);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        bonusManager.makeBonusChange(player.getPowers().get(PowerType.SKIP).bonusChanges().getNumber(),
                player.getPowers().get(PowerType.SKIP).bonusChanges().getSign());

        PowerImageTask powerImageTask = new PowerImageTask(R.drawable.help_free_pass_button_default,
                helpPowerUsedImg, helpPowerUsedImgBg, R.drawable.skip_icon, 400);
        powerImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onChargeDurationEnd() {

    }

    public static class Builder {

        private QuestionHandler questionHandler;
        private ConstraintLayout skipBtnLayout;
        private Player player;
        private StreakBonusManager bonusManager;
        private AnswerButtonsHandler answerButtonsHandler;
        private ImageView map;
        private Context context;
        private ImageView helpPowerUsedImg;
        private ImageView helpPowerUsedImgBg;
        private SoundPoolHelper soundHelper;

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

        public Builder withBonusManager(StreakBonusManager bonusManager) {
            this.bonusManager = bonusManager;
            return this;
        }

        public Builder withAnswerButtonsHandler(AnswerButtonsHandler answerButtonsHandler) {
            this.answerButtonsHandler = answerButtonsHandler;
            return this;
        }

        public Builder forMap(ImageView map) {
            this.map = map;
            return this;
        }

        public Builder withContext(Context context) {
            this.context = context;
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

        public SkipButton enable() {
            SkipButton skipButton = new SkipButton();
            skipButton.questionHandler = this.questionHandler;
            skipButton.skipBtnLayout = this.skipBtnLayout;
            skipButton.player = this.player;
            skipButton.bonusManager = this.bonusManager;
            skipButton.answerButtonsHandler = this.answerButtonsHandler;
            skipButton.map = this.map;
            skipButton.context = this.context;
            skipButton.helpPowerUsedImg = this.helpPowerUsedImg;
            skipButton.helpPowerUsedImgBg = this.helpPowerUsedImgBg;
            skipButton.soundHelper = this.soundHelper;
            skipButton.enableButton();
            return skipButton;
        }
    }

}
