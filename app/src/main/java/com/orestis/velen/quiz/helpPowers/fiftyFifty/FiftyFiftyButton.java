package com.orestis.velen.quiz.helpPowers.fiftyFifty;

import android.content.Context;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.design.card.MaterialCardView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.StreakBonus.StreakBonusManager;
import com.orestis.velen.quiz.answerButtons.AnswerChoice;
import com.orestis.velen.quiz.helpPowers.ChargeChangeListener;
import com.orestis.velen.quiz.helpPowers.PowerType;
import com.orestis.velen.quiz.helpPowers.shield.PowerImageTask;
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.questions.Question;
import com.orestis.velen.quiz.questions.QuestionChangedListener;
import com.orestis.velen.quiz.questions.QuestionHandler;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

import java.util.HashMap;

public class FiftyFiftyButton implements ChargeChangeListener, QuestionChangedListener {

    private ConstraintLayout fiftyFiftyBtnLayout;
    private HashMap<AnswerChoice, Button> answerButtons;
    private HashMap<AnswerChoice, MaterialCardView> flagAnswerButtons;
    private QuestionHandler questionHandler;
    private Player player;
    private int charges;
    private TextView fiftyFiftyChargesTextView;
    private StreakBonusManager bonusManager;
    private Context context;
    private ImageView hideMapOverlay;
    private ImageView map;
    private ImageView helpPowerUsedImg;
    private ImageView helpPowerUsedImgBg;
    private SoundPoolHelper soundHelper;
    private static final int DEFAULT = 0;
    private static final int MAP = 1;
    private static final int MAP_TO_FLAGS = 2;

    private FiftyFiftyButton(){}

    private void enableButton(int mode) {
        questionHandler.registerQuestionChangedListener(this);
        charges = player.getPowers().get(PowerType.FIFTY_FIFTY).charges();
        fiftyFiftyChargesTextView = fiftyFiftyBtnLayout.findViewById(R.id.fiftyFiftyCharges);
        fiftyFiftyChargesTextView.setText(String.valueOf(charges));
        if(charges < 1) {
            fiftyFiftyBtnLayout.setEnabled(false);
        }
        switch (mode) {
            case DEFAULT:
                fiftyFiftyBtnLayout.setOnClickListener(new FiftyFiftyDefaultClickListener(questionHandler, answerButtons,
                        this, context, soundHelper));
                break;
            case MAP:
                fiftyFiftyBtnLayout.setOnClickListener(new FiftyFiftyMapClickListener(map, hideMapOverlay,
                        this, questionHandler, soundHelper));
                break;
            case MAP_TO_FLAGS:
                fiftyFiftyBtnLayout.setOnClickListener(new FiftyFiftyMapToFlagsClickListener(questionHandler, flagAnswerButtons,
                        this, context, soundHelper));
                break;
        }
    }

    @Override
    public void onChargeDecreased() {
        charges--;
        fiftyFiftyChargesTextView.setText(String.valueOf(charges));
        bonusManager.makeBonusChange(player.getPowers().get(PowerType.FIFTY_FIFTY).bonusChanges().getNumber(),
                player.getPowers().get(PowerType.FIFTY_FIFTY).bonusChanges().getSign());
        PowerImageTask powerImageTask = new PowerImageTask(R.drawable.help_fiftyfifty_button_default,
                helpPowerUsedImg, helpPowerUsedImgBg, R.drawable.fiftyfifty_icon, 400);
        powerImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onQuestionChanged(Question question) {
        fiftyFiftyBtnLayout.setEnabled(charges >= 1);
    }

    @Override
    public void onChargeDurationEnd() {

    }

    public static class Builder {

        private QuestionHandler questionHandler;
        private HashMap<AnswerChoice, Button> answerButtons;
        private HashMap<AnswerChoice, MaterialCardView> flagAnswerButtons;
        private ConstraintLayout fiftyFiftyBtnLayout;
        private StreakBonusManager bonusManager;
        private Player player;
        private Context context;
        private ImageView hideMapOverlay;
        private ImageView map;
        private ImageView helpPowerUsedImg;
        private ImageView helpPowerUsedImgBg;
        private SoundPoolHelper soundHelper;

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

        public Builder withFlagAnswerButtons(HashMap<AnswerChoice, MaterialCardView> flagAnswerButtons) {
            this.flagAnswerButtons = flagAnswerButtons;
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

        public Builder withBonusManager(StreakBonusManager bonusManager) {
            this.bonusManager = bonusManager;
            return this;
        }

        public Builder withHideMapOverlay(ImageView hideMapOverlay) {
            this.hideMapOverlay = hideMapOverlay;
            return this;
        }

        public Builder withMap(ImageView map) {
            this.map = map;
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

        public FiftyFiftyButton enableDefault() {
            FiftyFiftyButton fiftyFiftyButton = new FiftyFiftyButton();
            fiftyFiftyButton.questionHandler = this.questionHandler;
            fiftyFiftyButton.answerButtons = this.answerButtons;
            fiftyFiftyButton.fiftyFiftyBtnLayout = this.fiftyFiftyBtnLayout;
            fiftyFiftyButton.player = this.player;
            fiftyFiftyButton.context = this.context;
            fiftyFiftyButton.bonusManager = this.bonusManager;
            fiftyFiftyButton.helpPowerUsedImg = this.helpPowerUsedImg;
            fiftyFiftyButton.helpPowerUsedImgBg = this.helpPowerUsedImgBg;
            fiftyFiftyButton.soundHelper = this.soundHelper;
            fiftyFiftyButton.enableButton(DEFAULT);
            return fiftyFiftyButton;
        }

        public FiftyFiftyButton enableForMap() {
            FiftyFiftyButton fiftyFiftyButton = new FiftyFiftyButton();
            fiftyFiftyButton.questionHandler = this.questionHandler;
            fiftyFiftyButton.answerButtons = this.answerButtons;
            fiftyFiftyButton.fiftyFiftyBtnLayout = this.fiftyFiftyBtnLayout;
            fiftyFiftyButton.player = this.player;
            fiftyFiftyButton.context = this.context;
            fiftyFiftyButton.bonusManager = this.bonusManager;
            fiftyFiftyButton.map = this.map;
            fiftyFiftyButton.hideMapOverlay = this.hideMapOverlay;
            fiftyFiftyButton.helpPowerUsedImg = this.helpPowerUsedImg;
            fiftyFiftyButton.helpPowerUsedImgBg = this.helpPowerUsedImgBg;
            fiftyFiftyButton.soundHelper = this.soundHelper;
            fiftyFiftyButton.enableButton(MAP);
            return fiftyFiftyButton;
        }

        public FiftyFiftyButton enableForMapToFlag() {
            FiftyFiftyButton fiftyFiftyButton = new FiftyFiftyButton();
            fiftyFiftyButton.questionHandler = this.questionHandler;
            fiftyFiftyButton.flagAnswerButtons = this.flagAnswerButtons;
            fiftyFiftyButton.fiftyFiftyBtnLayout = this.fiftyFiftyBtnLayout;
            fiftyFiftyButton.player = this.player;
            fiftyFiftyButton.context = this.context;
            fiftyFiftyButton.bonusManager = this.bonusManager;
            fiftyFiftyButton.helpPowerUsedImg = this.helpPowerUsedImg;
            fiftyFiftyButton.helpPowerUsedImgBg = this.helpPowerUsedImgBg;
            fiftyFiftyButton.soundHelper = this.soundHelper;
            fiftyFiftyButton.enableButton(MAP_TO_FLAGS);
            return fiftyFiftyButton;
        }
    }


}
