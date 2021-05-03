package com.orestis.velen.quiz.pinpoint;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.StreakBonus.StreakBonusDisplayHandler;
import com.orestis.velen.quiz.StreakBonus.StreakBonusManager;
import com.orestis.velen.quiz.adverts.FullscreenAdManager;
import com.orestis.velen.quiz.bonusTimeDisplay.BonusTimeHandler;
import com.orestis.velen.quiz.gameEnd.GameEndLossFragment;
import com.orestis.velen.quiz.gameEnd.GameEndWinFragment;
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
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.player.PlayerHelper;
import com.orestis.velen.quiz.player.PlayerSession;
import com.orestis.velen.quiz.questions.Difficulty;
import com.orestis.velen.quiz.questions.DifficultyHelper;
import com.orestis.velen.quiz.questions.QuestionHandler;
import com.orestis.velen.quiz.repositories.RepositoryFactory;
import com.orestis.velen.quiz.repositories.SampleSizeEndListener;
import com.orestis.velen.quiz.roundProgressDisplay.RoundProgressDisplayHandler;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

import static com.orestis.velen.quiz.helpPowers.PowerType.EXTRA_TIME;
import static com.orestis.velen.quiz.loadingBar.TimerDirection.DOWN;
import static com.orestis.velen.quiz.mainMenu.MainMenuActivity.XP_BOOST_ENABLED;
import static com.orestis.velen.quiz.questions.GameType.TYPE_MONUMENTS;
import static com.orestis.velen.quiz.repositories.GameTheme.GEOGRAPHY;

public class MonumentsPointActivity extends AppCompatActivity implements SampleSizeEndListener, DisplayDistanceDurationEndListener,
         PinpointAnswerGivenListener, LoadingBarStateListener, FrameResizeHandler, GameStartingEndListener {

    private Player player;
    private QuestionHandler questionHandler;
    private StreakBonusManager bonusManager;
    private StreakBonusDisplayHandler bonusDisplayHandler;
    private LoadingBarHandler loadingBarHandler;
    private BonusTimeHandler bonusTimeHandler;
    private ImageView hideMapOverlay;
    private static final int LEVEL_QUESTION_SAMPLE = 20;
    public static final int DISPLAY_DISTANCE_DURATION = 500;
    private static final int DELAYED_ANSWER_DURATION = 200;
    private static final int DISPLAY_BONUS_DURATION = 500;
    private static final long PROGRESS_BAR_DURATION = 60000;
    private static final int PROGRESS_BAR_ANIMATION_DURATION = 1800;
    private static final int GAINED_TIME_CLOSE = 3000;
    private static final int GAINED_TIME_PERFECT = 5000;
    private static final int LOST_TIME = 5000;
    private boolean gameHasEnded = false;
    private Difficulty difficulty;
    private MapTouchListener mapTouchListener;
    private ImageView map;
    private SoundPoolHelper soundHelper;
    private boolean hasXpBoostEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monuments_point);

        soundHelper = new SoundPoolHelper(5, this);
        soundHelper.loadInGameSounds();

        FullscreenAdManager.getInstance().initialise(this);

        map = findViewById(R.id.mapId);
        hideMapOverlay = findViewById(R.id.hideMapId);

        player = PlayerHelper.getPlayerFromIntent(getIntent());
        hasXpBoostEnabled = getIntent().getBooleanExtra(XP_BOOST_ENABLED, false);

        difficulty = DifficultyHelper.getDifficultyFromIntent(getIntent());

        Typeface face = Typeface.createFromAsset(getAssets(),"custom.ttf");

        DistanceTextHandler distanceTextHandler = new DistanceTextHandler(DISPLAY_DISTANCE_DURATION, (TextView) findViewById(R.id.distanceText), this);

        RepositoryFactory repositoryFactory = new RepositoryFactory(GEOGRAPHY);
        questionHandler = new QuestionHandler(repositoryFactory, this, this);

        new MonumentNameHandler((TextView) findViewById(R.id.monumentName), questionHandler, this);

        new MonumentImageHandler((ImageView) findViewById(R.id.landmarkId), questionHandler, this, this);

        mapTouchListener = new MapTouchListener.Builder()
                .forMap(map)
                .withContext(this)
                .withDelayedAnswerDuration(DELAYED_ANSWER_DURATION)
                .withQuestionHandler(questionHandler)
                .withDistanceTextHandler(distanceTextHandler)
                .withSoundPoolHelper(soundHelper)
                .withAnswerGivenListener(this).build();

        questionHandler.init(difficulty, TYPE_MONUMENTS, LEVEL_QUESTION_SAMPLE);

        bonusTimeHandler = new BonusTimeHandler((TextView) findViewById(R.id.bonusTime), this);


        bonusDisplayHandler = new StreakBonusDisplayHandler((TextView) findViewById(R.id.nowBonusTxt),
                (TextView) findViewById(R.id.accumBonusTxt), face, DISPLAY_BONUS_DURATION, this);
        bonusManager = new StreakBonusManager(GEOGRAPHY, difficulty, TYPE_MONUMENTS, bonusDisplayHandler);

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
            map.setOnTouchListener(mapTouchListener);
            setupPowerButtons();
            soundHelper.playInGameBackgroundMusic();
        }
    }

    private void setupPowerButtons() {
        new SkipButton.Builder().forPlayer(player)
                .useLayout((ConstraintLayout) findViewById(R.id.skipBtnLayout))
                .withBonusManager(bonusManager)
                .withContext(this)
                .forMap(map)
                .withSoundPoolHelper(soundHelper)
                .useHelpPowerUsedImg((ImageView) findViewById(R.id.helpPowerUsedImg))
                .useHelpPowerUsedImgBg((ImageView) findViewById(R.id.helpPowerUsedImgBg))
                .withQuestionHandler(questionHandler).enable();

        new FiftyFiftyButton.Builder().forPlayer(player)
                .useLayout((ConstraintLayout) findViewById(R.id.fiftyFiftyBtnLayout))
                .withMap(map)
                .withHideMapOverlay(hideMapOverlay)
                .withQuestionHandler(questionHandler)
                .withBonusManager(bonusManager)
                .withSoundPoolHelper(soundHelper)
                .useHelpPowerUsedImg((ImageView) findViewById(R.id.helpPowerUsedImg))
                .useHelpPowerUsedImgBg((ImageView) findViewById(R.id.helpPowerUsedImgBg))
                .withContext(this).enableForMap();

        new ShieldButton.Builder().forPlayer(player)
                .useLayout((ConstraintLayout) findViewById(R.id.shieldLayout))
                .useShieldOnIcon((ImageView) findViewById(R.id.shieldImg))
                .useShieldBreakingIcon((ImageView) findViewById(R.id.shieldBreakingImg))
                .useShieldOverlay((FrameLayout) findViewById(R.id.shieldOverlay))
                .useHelpPowerUsedImg((ImageView) findViewById(R.id.helpPowerUsedImg))
                .useHelpPowerUsedImgBg((ImageView) findViewById(R.id.helpPowerUsedImgBg))
                .withContext(this)
                .withSoundPoolHelper(soundHelper)
                .withMapTouchListener(mapTouchListener)
                .withQuestionHandler(questionHandler).enable();

        new FreezeTimeButton.Builder().forPlayer(player)
                .useLayout((ConstraintLayout) findViewById(R.id.freezeTimeBtnLayout))
                .useContext(this)
                .withLoadingBarHandler(loadingBarHandler)
                .withSoundPoolHelper(soundHelper)
                .withPowerIcon((ImageView) findViewById(R.id.freezeImg))
                .useHelpPowerUsedImg((ImageView) findViewById(R.id.helpPowerUsedImg))
                .useHelpPowerUsedImgBg((ImageView) findViewById(R.id.helpPowerUsedImgBg))
                .withTimerText((TextView) findViewById(R.id.freezeTimer)).enable();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ImageView landmark = findViewById(R.id.landmarkId);
        ImageView frame = findViewById(R.id.frame);
        TextView landmarkName = findViewById(R.id.monumentName);
        setUpFrameUI(landmark, landmarkName, frame);
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
    public void onSampleSizeEnd() {
        if(gameHasEnded) {
            return;
        }
        soundHelper.pauseInGameBackgroundMusic();
        gameHasEnded = true;
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
                .withDarkBg((ImageView) findViewById(R.id.darkBg)).build();
        ft.replace(R.id.endGameScreenPlaceholder, gameEndWinFragment);
        ft.commit();
    }

    @Override
    public void onDisplayDistanceDurationEnd() {
        questionHandler.nextQuestion();
        hideMapOverlay.setVisibility(View.GONE);
    }

    @Override
    public void onCloseAnswerGiven() {
        bonusManager.correctAnswerGiven();
        bonusTimeHandler.displayGainedTime(GAINED_TIME_CLOSE, DISPLAY_BONUS_DURATION);
        loadingBarHandler.incrementLoadingBar(GAINED_TIME_CLOSE, DISPLAY_BONUS_DURATION);
    }

    @Override
    public void onPerfectAnswerGiven() {
        bonusManager.perfectAnswerGiven();
        bonusTimeHandler.displayGainedTime(GAINED_TIME_PERFECT, DISPLAY_BONUS_DURATION);
        loadingBarHandler.incrementLoadingBar(GAINED_TIME_PERFECT, DISPLAY_BONUS_DURATION);
    }

    @Override
    public void onFarAnswerGiven() {
        bonusManager.wrongAnswerGiven();
        bonusTimeHandler.displayLostTime(LOST_TIME, DISPLAY_BONUS_DURATION);
        loadingBarHandler.decrementLoadingBar(LOST_TIME, DISPLAY_BONUS_DURATION);
    }

    @Override
    public void onLoadingBarFinished() {
        if(gameHasEnded) {
            return;
        }
        soundHelper.pauseInGameBackgroundMusic();
        gameHasEnded = true;
        loadingBarHandler.stopLoadingBar();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        GameEndLossFragment gameEndLossFragment = new GameEndLossFragment.Builder()
                .forPlayer(player)
                .forDifficulty(difficulty)
                .withSoundPoolHelper(soundHelper)
                .restartActivity(MonumentsPointActivity.class)
                .withDarkBg((ImageView) findViewById(R.id.darkBg)).build();
        ft.replace(R.id.endGameScreenPlaceholder, gameEndLossFragment);
        ft.commit();
    }

    @Override
    public void onLoadingBarFillAnimationEnd() {

    }

    private void setUpFrameUI(ImageView landmark, TextView landmarkName, ImageView frame) {
        Drawable drawable = landmark.getDrawable();
        Rect imageBounds = drawable.getBounds();

        int scaledHeight = imageBounds.height();
        int scaledWidth = imageBounds.width();

        float aspectRatio = scaledWidth / (scaledHeight * 1.0f);

        int landmarkHeight = landmark.getHeight();
        frame.getLayoutParams().width = (int) (landmarkHeight * aspectRatio) + 4;
        frame.requestLayout();

        landmarkName.getLayoutParams().width = frame.getLayoutParams().width;
        landmarkName.requestLayout();
    }

    @Override
    public void onFrameResizeRequired() {
        ImageView landmark = findViewById(R.id.landmarkId);
        ImageView frame = findViewById(R.id.frame);
        TextView landmarkName = findViewById(R.id.monumentName);
        setUpFrameUI(landmark, landmarkName, frame);
    }

    @Override
    public void onBackPressed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                gameHasEnded = true;
                Intent intent = new Intent(MonumentsPointActivity.this, MainMenuActivity.class);
                PlayerSession.getInstance().setRecoveredPlayer(player);
                MonumentsPointActivity.this.startActivity(intent);
                MonumentsPointActivity.this.finish();
                MonumentsPointActivity.this.overridePendingTransition(0, 0);
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
}
