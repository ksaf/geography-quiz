package com.orestis.velen.quiz.pinpoint;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.StreakBonus.BonusAwardedListener;
import com.orestis.velen.quiz.StreakBonus.StreakBonusDisplayHandler;
import com.orestis.velen.quiz.StreakBonus.StreakBonusManager;
import com.orestis.velen.quiz.bonusTimeDisplay.BonusTimeHandler;
import com.orestis.velen.quiz.helpPowers.freezeTime.FreezeTimeButton;
import com.orestis.velen.quiz.helpPowers.skip.SkipButton;
import com.orestis.velen.quiz.loadingBar.LoadingBarHandler;
import com.orestis.velen.quiz.loadingBar.LoadingBarStateListener;
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.player.PlayerHelper;
import com.orestis.velen.quiz.questionText.QuestionAnnouncement;
import com.orestis.velen.quiz.questionText.QuestionAnnouncementFactory;
import com.orestis.velen.quiz.questionText.QuestionTextHandler;
import com.orestis.velen.quiz.questions.QuestionHandler;
import com.orestis.velen.quiz.repositories.RepositoryFactory;
import com.orestis.velen.quiz.repositories.SampleSizeEndListener;
import com.orestis.velen.quiz.roundProgressDisplay.RoundProgressDisplayHandler;
import com.orestis.velen.quiz.victoryFragment.VictoryFragment;

import static com.orestis.velen.quiz.loadingBar.TimerDirection.DOWN;
import static com.orestis.velen.quiz.questions.Difficulty.EASY;
import static com.orestis.velen.quiz.questions.GameType.TYPE_B;
import static com.orestis.velen.quiz.questions.GameType.TYPE_D;
import static com.orestis.velen.quiz.repositories.GameTheme.GEOGRAPHY;

public class CapitalsPointActivity extends AppCompatActivity implements SampleSizeEndListener, DisplayDistanceDurationEndListener,
        BonusAwardedListener, PinpointAnswerGivenListener, LoadingBarStateListener {

    private Player player;
    private QuestionHandler questionHandler;
    private StreakBonusManager bonusManager;
    private StreakBonusDisplayHandler bonusDisplayHandler;
    private LoadingBarHandler loadingBarHandler;
    private BonusTimeHandler bonusTimeHandler;
    private static final int LEVEL_QUESTION_SAMPLE = 20;
    private static final int DISPLAY_DISTANCE_DURATION = 500;
    private static final int DELAYED_ANSWER_DURATION = 200;
    private static final int DISPLAY_BONUS_DURATION = 500;
    private static final long PROGRESS_BAR_DURATION = 60000;
    private static final int PROGRESS_BAR_ANIMATION_DURATION = 1800;
    private static final int EXTRA_TIME_CLOSE = 3000;
    private static final int EXTRA_TIME_PERFECT = 5000;
    private static final int LOST_TIME = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinpoint_activity);
        ImageView map = findViewById(R.id.mapId);

        startCountDownBar();

        player = PlayerHelper.getPlayerFromIntent(getIntent());

        Typeface face = Typeface.createFromAsset(getAssets(),"custom.ttf");

        DistanceTextHandler distanceTextHandler = new DistanceTextHandler(DISPLAY_DISTANCE_DURATION, (TextView) findViewById(R.id.distanceText), this);

        RepositoryFactory repositoryFactory = new RepositoryFactory(GEOGRAPHY);
        questionHandler = new QuestionHandler(repositoryFactory, this);

        QuestionAnnouncement questionAnnouncement = (new QuestionAnnouncementFactory()).getQuestionAnouncement(GEOGRAPHY);
        new QuestionTextHandler(face, (TextView) findViewById(R.id.questionText), questionHandler, questionAnnouncement, TYPE_D);

        MapTouchListener mapTouchListener = new MapTouchListener.Builder()
                .forMap(map)
                .withContext(this)
                .withDelayedAnswerDuration(DELAYED_ANSWER_DURATION)
                .withQuestionHandler(questionHandler)
                .withDistanceTextHandler(distanceTextHandler)
                .withAnswerGivenListener(this).build();
        map.setOnTouchListener(mapTouchListener);

        questionHandler.init(EASY, TYPE_B, LEVEL_QUESTION_SAMPLE);

        bonusTimeHandler = new BonusTimeHandler((TextView) findViewById(R.id.bonusTime));

        bonusManager = new StreakBonusManager(GEOGRAPHY, EASY, TYPE_D, this);
        bonusDisplayHandler = new StreakBonusDisplayHandler((TextView) findViewById(R.id.nowBonusTxt), (TextView) findViewById(R.id.accumBonusTxt), face, DISPLAY_BONUS_DURATION);

        new RoundProgressDisplayHandler((TextView) findViewById(R.id.questionProgressTxt),
                (TextView) findViewById(R.id.currentQuestionNumberTxt), LEVEL_QUESTION_SAMPLE, questionHandler, face);

//        new FiftyFiftyButton.Builder().forPlayer(player)
//                .useLayout((ConstraintLayout) findViewById(R.id.fiftyFiftyBtnLayout))
//                .withAnswerButtons(buttons)
//                .withQuestionHandler(questionHandler)
//                .withContext(this).enable();

        new SkipButton.Builder().forPlayer(player)
                .useLayout((ConstraintLayout) findViewById(R.id.skipBtnLayout))
                .withQuestionHandler(questionHandler).enable();

        new FreezeTimeButton.Builder().forPlayer(player)
                .useLayout((ConstraintLayout) findViewById(R.id.freezeTimeBtnLayout))
                .useContext(this)
                .withLoadingBarHandler(loadingBarHandler)
                .withPowerIcon((ImageView) findViewById(R.id.freezeImg))
                .withTimerText((TextView) findViewById(R.id.freezeTimer)).enable();

//        new ShieldButton.Builder().forPlayer(player)
//                .useLayout((ConstraintLayout) findViewById(R.id.shieldLayout))
//                .useShieldOnIcon((ImageView) findViewById(R.id.shieldImg))
//                .useShieldBreakingIcon((ImageView) findViewById(R.id.shieldBreakingImg))
//                .useshieldOverlay((FrameLayout) findViewById(R.id.shieldOverlay))
//                .withContext(this)
//                .withAnswerButtons(buttons)
//                .withAnswerButtonsHandler(answerButtonsHandler)
//                .withQuestionHandler(questionHandler).enable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingBarHandler.pauseLoadingBar();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadingBarHandler.resumeLoadingBar();
    }

    private void startCountDownBar() {
        ProgressBar bar = findViewById(R.id.progressBar);
        loadingBarHandler = new LoadingBarHandler(bar, DOWN, this);
        loadingBarHandler.startLoadingBarWithFillingAnimation(PROGRESS_BAR_DURATION, PROGRESS_BAR_ANIMATION_DURATION);
    }

    @Override
    public void onSampleSizeEnd() {
        loadingBarHandler.pauseLoadingBar();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        VictoryFragment victoryFragment = new VictoryFragment.Builder()
                .forPlayer(player)
                .withMaxTime(PROGRESS_BAR_DURATION)
                .forDifficulty(EASY)
                .withTimeLeft(loadingBarHandler.getRemainingTime())
                .withBonusAccumulated(bonusManager.getAccumulatedBonus()).build();
        ft.replace(R.id.endGameScreenPlaceholder, victoryFragment);
        ft.commit();
    }

    @Override
    public void onDisplayDistanceDurationEnd() {
        questionHandler.nextQuestion();
    }

    @Override
    public void onBonusAwarded(int bonusNow, int accumulatedBonus) {
        bonusDisplayHandler.displayBonus(bonusNow, accumulatedBonus);
    }

    @Override
    public void onCloseAnswerGiven() {
        bonusManager.correctAnswerGiven();
        bonusTimeHandler.displayGainedTime(EXTRA_TIME_CLOSE, DISPLAY_BONUS_DURATION);
        loadingBarHandler.incrementLoadingBar(EXTRA_TIME_CLOSE, DISPLAY_BONUS_DURATION);
    }

    @Override
    public void onPerfectAnswerGiven() {
        bonusManager.perfectAnswerGiven();
        bonusTimeHandler.displayGainedTime(EXTRA_TIME_PERFECT, DISPLAY_BONUS_DURATION);
        loadingBarHandler.incrementLoadingBar(EXTRA_TIME_PERFECT, DISPLAY_BONUS_DURATION);
    }

    @Override
    public void onFarAnswerGiven() {
        bonusManager.wrongAnswerGiven();
        bonusTimeHandler.displayLostTime(LOST_TIME, DISPLAY_BONUS_DURATION);
        loadingBarHandler.decrementLoadingBar(LOST_TIME, DISPLAY_BONUS_DURATION);
    }

    @Override
    public void onLoadingBarFinished() {

    }

    @Override
    public void onLoadingBarFillAnimationEnd() {

    }
}
