package com.orestis.velen.quiz.outlinesToFlags;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.card.MaterialCardView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.StreakBonus.StreakBonusDisplayHandler;
import com.orestis.velen.quiz.StreakBonus.StreakBonusManager;
import com.orestis.velen.quiz.adverts.FullscreenAdManager;
import com.orestis.velen.quiz.answerButtons.AnswerButtonStateListener;
import com.orestis.velen.quiz.answerButtons.AnswerButtonsHandler;
import com.orestis.velen.quiz.answerButtons.AnswerChoice;
import com.orestis.velen.quiz.answerButtons.AnswerGivenListener;
import com.orestis.velen.quiz.answerButtons.FlagAnswerButtonsHandler;
import com.orestis.velen.quiz.bonusTimeDisplay.BonusTimeHandler;
import com.orestis.velen.quiz.gameEnd.GameEndLossFragment;
import com.orestis.velen.quiz.gameEnd.GameEndWinFragment;
import com.orestis.velen.quiz.gameEnd.LevelUpScreenHandler;
import com.orestis.velen.quiz.gameStartingLoading.GameStartingEndListener;
import com.orestis.velen.quiz.gameStartingLoading.GameStartingFragment;
import com.orestis.velen.quiz.helpPowers.extraTime.ExtraTimePowerConfigs;
import com.orestis.velen.quiz.helpPowers.fiftyFifty.FiftyFiftyButton;
import com.orestis.velen.quiz.helpPowers.freezeTime.FreezeTimeButton;
import com.orestis.velen.quiz.helpPowers.shield.ShieldButton;
import com.orestis.velen.quiz.helpPowers.skip.SkipButton;
import com.orestis.velen.quiz.language.LocaleHelper;
import com.orestis.velen.quiz.loadingBar.LoadingBarHandler;
import com.orestis.velen.quiz.loadingBar.LoadingBarStateListener;
import com.orestis.velen.quiz.mainMenu.MainMenuActivity;
import com.orestis.velen.quiz.outlines.MainActivity;
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.player.PlayerHelper;
import com.orestis.velen.quiz.player.PlayerSession;
import com.orestis.velen.quiz.questionImage.QuestionImageDisplayHandler;
import com.orestis.velen.quiz.questionText.QuestionAnnouncement;
import com.orestis.velen.quiz.questionText.QuestionAnnouncementFactory;
import com.orestis.velen.quiz.questionText.QuestionTextHandler;
import com.orestis.velen.quiz.questions.Difficulty;
import com.orestis.velen.quiz.questions.DifficultyHelper;
import com.orestis.velen.quiz.questions.QuestionHandler;
import com.orestis.velen.quiz.repositories.RepositoryFactory;
import com.orestis.velen.quiz.repositories.SampleSizeEndListener;
import com.orestis.velen.quiz.roundProgressDisplay.RoundProgressDisplayHandler;
import com.orestis.velen.quiz.skillUpgrades.LevelUpScreenClosedListener;
import com.orestis.velen.quiz.skillUpgrades.SkillUpgradesFragment;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

import java.util.HashMap;

import static com.orestis.velen.quiz.answerButtons.AnswerChoice.A;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.B;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.C;
import static com.orestis.velen.quiz.helpPowers.PowerType.EXTRA_TIME;
import static com.orestis.velen.quiz.loadingBar.TimerDirection.DOWN;
import static com.orestis.velen.quiz.mainMenu.MainMenuActivity.XP_BOOST_ENABLED;
import static com.orestis.velen.quiz.questions.GameType.TYPE_OUTLINE_TO_FLAG;
import static com.orestis.velen.quiz.repositories.GameTheme.GEOGRAPHY;

public class OutlinesToFlagsActivity extends AppCompatActivity implements LoadingBarStateListener, AnswerGivenListener,
        AnswerButtonStateListener, SampleSizeEndListener, GameStartingEndListener, LevelUpScreenHandler {

    private static final long PROGRESS_BAR_DURATION = 60000;
    private static final int GAINED_TIME = 3000;
    private static final int LOST_TIME = 5000;
    private static final int DISPLAY_BONUS_DURATION = 500;
    private static final int LEVEL_QUESTION_SAMPLE = 20;
    private LoadingBarHandler loadingBarHandler;
    private BonusTimeHandler bonusTimeHandler;
    private QuestionHandler questionHandler;
    private QuestionTextHandler questionTextHandler;
    private AnswerButtonsHandler answerButtonsHandler;
    private StreakBonusManager bonusManager;
    private StreakBonusDisplayHandler bonusDisplayHandler;
    private Typeface face;
    private Player player;
    private Difficulty difficulty;
    private boolean gameHasEnded = false;
    private HashMap<AnswerChoice, Button> buttons = new HashMap<>();
    private HashMap<AnswerChoice, MaterialCardView> buttonBackGrounds = new HashMap<>();
    private SoundPoolHelper soundHelper;
    private boolean hasXpBoostEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outlines_to_flags_activity);

        soundHelper = new SoundPoolHelper(5, this);
        soundHelper.loadInGameSounds();

        FullscreenAdManager.getInstance().initialise(this);

        player = PlayerHelper.getPlayerFromIntent(getIntent());
        hasXpBoostEnabled = getIntent().getBooleanExtra(XP_BOOST_ENABLED, false);

        buttons.put(A, (Button) findViewById(R.id.answerA));
        buttons.put(B, (Button) findViewById(R.id.answerB));
        buttons.put(C, (Button) findViewById(R.id.answerC));

        buttonBackGrounds.put(A, (MaterialCardView) findViewById(R.id.answerABackGround));
        buttonBackGrounds.put(B, (MaterialCardView) findViewById(R.id.answerBBackGround));
        buttonBackGrounds.put(C, (MaterialCardView) findViewById(R.id.answerCBackGround));

        ImageView questionDisplay = findViewById(R.id.questionDisplay);

        face = Typeface.createFromAsset(getAssets(),"custom.ttf");

        RepositoryFactory repositoryFactory = new RepositoryFactory(GEOGRAPHY);
        questionHandler = new QuestionHandler(repositoryFactory, this, this);

        difficulty = DifficultyHelper.getDifficultyFromIntent(getIntent());

        new QuestionImageDisplayHandler.Builder()
                .forImageView(questionDisplay)
                .gameOf(TYPE_OUTLINE_TO_FLAG)
                .withContext(this)
                .withQuestionHandler(questionHandler).buildAndInit();


        answerButtonsHandler = new FlagAnswerButtonsHandler.Builder()
                .forButtons(buttons)
                .forButtonBackgrounds(buttonBackGrounds)
                .displayAnswerDurationFor(DISPLAY_BONUS_DURATION)
                .withQuestionHandler(questionHandler)
                .withButtonStateListener(this)
                .withSoundPoolHelper(soundHelper)
                .withAnswerGivenListener(this).buildAndInitForMapToFlags();

        QuestionAnnouncement questionAnnouncement = (new QuestionAnnouncementFactory()).getQuestionAnouncement(GEOGRAPHY);
        questionTextHandler = new QuestionTextHandler(face, (TextView) findViewById(R.id.questionText),
                questionHandler, questionAnnouncement, TYPE_OUTLINE_TO_FLAG, this);

        questionHandler.init(difficulty, TYPE_OUTLINE_TO_FLAG, LEVEL_QUESTION_SAMPLE);

        bonusTimeHandler = new BonusTimeHandler((TextView) findViewById(R.id.bonusTime), this);

        bonusDisplayHandler = new StreakBonusDisplayHandler((TextView) findViewById(R.id.nowBonusTxt),
                (TextView) findViewById(R.id.accumBonusTxt), face, DISPLAY_BONUS_DURATION, this);
        bonusManager = new StreakBonusManager(GEOGRAPHY, difficulty, TYPE_OUTLINE_TO_FLAG, bonusDisplayHandler);

        new RoundProgressDisplayHandler((TextView) findViewById(R.id.questionProgressTxt),
                (TextView) findViewById(R.id.currentQuestionNumberTxt), LEVEL_QUESTION_SAMPLE, questionHandler, face);

        GameStartingFragment gameStartingFragment = new GameStartingFragment.Builder()
                .useTypeface(face)
                .withGameStartingEndListener(this)
                .build();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.gameStartingPlaceholder, gameStartingFragment);
        ft.commit();

    }

    @Override
    public void onGameStartingScreenEnd() {
        if(!gameHasEnded) {
            startCountDownBar();
            setupPowerButtons();
            soundHelper.playInGameBackgroundMusic();
        }
    }

    private void setupPowerButtons() {
        new FiftyFiftyButton.Builder().forPlayer(player)
                .useLayout((ConstraintLayout) findViewById(R.id.fiftyFiftyBtnLayout))
                .withFlagAnswerButtons(buttonBackGrounds)
                .withQuestionHandler(questionHandler)
                .withBonusManager(bonusManager)
                .withSoundPoolHelper(soundHelper)
                .useHelpPowerUsedImg((ImageView) findViewById(R.id.helpPowerUsedImg))
                .useHelpPowerUsedImgBg((ImageView) findViewById(R.id.helpPowerUsedImgBg))
                .withContext(this).enableForMapToFlag();

        new SkipButton.Builder().forPlayer(player)
                .useLayout((ConstraintLayout) findViewById(R.id.skipBtnLayout))
                .withBonusManager(bonusManager)
                .withAnswerButtonsHandler(answerButtonsHandler)
                .withSoundPoolHelper(soundHelper)
                .useHelpPowerUsedImg((ImageView) findViewById(R.id.helpPowerUsedImg))
                .useHelpPowerUsedImgBg((ImageView) findViewById(R.id.helpPowerUsedImgBg))
                .withQuestionHandler(questionHandler).enable();

        new FreezeTimeButton.Builder().forPlayer(player)
                .useLayout((ConstraintLayout) findViewById(R.id.freezeTimeBtnLayout))
                .useContext(this)
                .withLoadingBarHandler(loadingBarHandler)
                .withPowerIcon((ImageView) findViewById(R.id.freezeImg))
                .withSoundPoolHelper(soundHelper)
                .useHelpPowerUsedImg((ImageView) findViewById(R.id.helpPowerUsedImg))
                .useHelpPowerUsedImgBg((ImageView) findViewById(R.id.helpPowerUsedImgBg))
                .withTimerText((TextView) findViewById(R.id.freezeTimer)).enable();

        new ShieldButton.Builder().forPlayer(player)
                .useLayout((ConstraintLayout) findViewById(R.id.shieldLayout))
                .useShieldOnIcon((ImageView) findViewById(R.id.shieldImg))
                .useShieldBreakingIcon((ImageView) findViewById(R.id.shieldBreakingImg))
                .useShieldOverlay((FrameLayout) findViewById(R.id.shieldOverlay))
                .useHelpPowerUsedImg((ImageView) findViewById(R.id.helpPowerUsedImg))
                .useHelpPowerUsedImgBg((ImageView) findViewById(R.id.helpPowerUsedImgBg))
                .withContext(this)
                .withSoundPoolHelper(soundHelper)
                .withAnswerButtons(buttons)
                .withFlagAnswerButtons(buttonBackGrounds)
                .withAnswerButtonsHandler(answerButtonsHandler)
                .withQuestionHandler(questionHandler).enable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundHelper.pauseInGameBackgroundMusic();
        if(loadingBarHandler != null) {
            loadingBarHandler.pauseLoadingBar();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        soundHelper.resumeInGameBackgroundMusic();
        if(loadingBarHandler != null) {
            loadingBarHandler.resumeLoadingBar();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(loadingBarHandler != null) {
            loadingBarHandler.pauseLoadingBar();
        }
    }

    private void startCountDownBar() {
        ProgressBar bar = findViewById(R.id.progressBar);
        TextView countDownTxt = findViewById(R.id.countDownTxt);
        loadingBarHandler = new LoadingBarHandler(bar, DOWN, this);
        loadingBarHandler.setCountDownText(countDownTxt);
        ExtraTimePowerConfigs extraTimePowerConfigs = new ExtraTimePowerConfigs();
        int extraTime = extraTimePowerConfigs.getExtraTimeForPowerLevel(player.getPowers().get(EXTRA_TIME).getPowerLevel());
        loadingBarHandler.startLoadingBar(PROGRESS_BAR_DURATION + extraTime);
    }

    @Override
    public void onLoadingBarFinished() {
        if (gameHasEnded) {
            return;
        }
        soundHelper.pauseInGameBackgroundMusic();
        gameHasEnded = true;
        answerButtonsHandler.enableButtons(false);
        loadingBarHandler.stopLoadingBar();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        GameEndLossFragment gameEndLossFragment = new GameEndLossFragment.Builder()
                .forPlayer(player)
                .forDifficulty(difficulty)
                .restartActivity(MainActivity.class)
                .withSoundPoolHelper(soundHelper)
                .withDarkBg((ImageView) findViewById(R.id.darkBg)).build();
        ft.replace(R.id.endGameScreenPlaceholder, gameEndLossFragment);
        ft.commit();
    }

    @Override
    public void onLoadingBarFillAnimationEnd() {
        answerButtonsHandler.enableButtons(true);
    }

    @Override
    public void onCorrectAnswerGiven() {
        bonusManager.correctAnswerGiven();
        bonusTimeHandler.displayGainedTime(GAINED_TIME, DISPLAY_BONUS_DURATION);
        loadingBarHandler.incrementLoadingBar(GAINED_TIME, DISPLAY_BONUS_DURATION);
    }

    @Override
    public void onWrongAnswerGiven() {
        bonusManager.wrongAnswerGiven();
        bonusTimeHandler.displayLostTime(LOST_TIME, DISPLAY_BONUS_DURATION);
        loadingBarHandler.decrementLoadingBar(LOST_TIME, DISPLAY_BONUS_DURATION);
    }

    @Override
    public void onAnswerButtonsEnabled() {
        questionHandler.nextQuestion();
    }

    @Override
    public void onSampleSizeEnd() {
        if(gameHasEnded) {
            return;
        }
        soundHelper.pauseInGameBackgroundMusic();
        gameHasEnded = true;
        answerButtonsHandler.enableButtons(false);
        loadingBarHandler.stopLoadingBar();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        GameEndWinFragment gameEndWinFragment = new GameEndWinFragment.Builder()
                .forPlayer(player)
                .withMaxTime(PROGRESS_BAR_DURATION)
                .forDifficulty(difficulty)
                .withTimeLeft(loadingBarHandler.getRemainingTime())
                .withStreakBonusManager(bonusManager)
                .withSoundPoolHelper(soundHelper)
                .hasXpBoostEnabled(hasXpBoostEnabled)
                .forTotalQuestionAmount(LEVEL_QUESTION_SAMPLE)
                .withLevelUpScreenHandler(this)
                .withDarkBg((ImageView) findViewById(R.id.darkBg)).build();
        ft.replace(R.id.endGameScreenPlaceholder, gameEndWinFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameHasEnded = true;
                Intent intent = new Intent(OutlinesToFlagsActivity.this, MainMenuActivity.class);
                PlayerSession.getInstance().setRecoveredPlayer(player);
                OutlinesToFlagsActivity.this.startActivity(intent);
                OutlinesToFlagsActivity.this.finish();
                OutlinesToFlagsActivity.this.overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundHelper.release();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    public void showLevelUpScreen(@Nullable LevelUpScreenClosedListener levelUpScreenClosedListener) {
        soundHelper.playMenuBtnOpenSound();
        SkillUpgradesFragment skillUpgradesFragment = new SkillUpgradesFragment.Builder()
                .forPlayer(player)
                .withDarkBg((ImageView) findViewById(R.id.darkBg))
                .doNotReleaseDarkBgOnClose()
                .withSoundPoolHelper(soundHelper)
                .withCloseListener(levelUpScreenClosedListener)
                .build();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.bounce_down_animation, R.anim.slide_up_animation);
        ft.replace(R.id.optionScreenPlaceholder, skillUpgradesFragment);
        ft.commit();
    }
}
