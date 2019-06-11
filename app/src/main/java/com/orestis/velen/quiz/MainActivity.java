package com.orestis.velen.quiz;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orestis.velen.quiz.StreakBonus.BonusAwardedListener;
import com.orestis.velen.quiz.StreakBonus.StreakBonusDisplayHandler;
import com.orestis.velen.quiz.StreakBonus.StreakBonusManager;
import com.orestis.velen.quiz.answerButtons.AnswerButtonStateListener;
import com.orestis.velen.quiz.answerButtons.AnswerButtonsHandler;
import com.orestis.velen.quiz.answerButtons.AnswerChoice;
import com.orestis.velen.quiz.answerButtons.AnswerGivenListener;
import com.orestis.velen.quiz.bonusTimeDisplay.BonusTimeHandler;
import com.orestis.velen.quiz.gameStartingLoading.GameStartingScreen;
import com.orestis.velen.quiz.helpPowers.fiftyFifty.FiftyFiftyButton;
import com.orestis.velen.quiz.helpPowers.freezeTime.FreezeTimeButton;
import com.orestis.velen.quiz.helpPowers.shield.ShieldButton;
import com.orestis.velen.quiz.helpPowers.skip.SkipButton;
import com.orestis.velen.quiz.loadingBar.LoadingBarHandler;
import com.orestis.velen.quiz.loadingBar.LoadingBarStateListener;
import com.orestis.velen.quiz.login.BounceLoadingView;
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.player.PlayerHelper;
import com.orestis.velen.quiz.questionImage.QuestionDisplayHandler;
import com.orestis.velen.quiz.questionText.QuestionAnnouncement;
import com.orestis.velen.quiz.questionText.QuestionAnnouncementFactory;
import com.orestis.velen.quiz.questionText.QuestionTextHandler;
import com.orestis.velen.quiz.questions.QuestionHandler;
import com.orestis.velen.quiz.repositories.RepositoryFactory;
import com.orestis.velen.quiz.repositories.SampleSizeEndListener;
import com.orestis.velen.quiz.roundProgressDisplay.RoundProgressDisplayHandler;
import com.orestis.velen.quiz.victoryFragment.VictoryFragment;

import java.util.HashMap;

import static com.orestis.velen.quiz.answerButtons.AnswerChoice.A;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.B;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.C;
import static com.orestis.velen.quiz.loadingBar.TimerDirection.DOWN;
import static com.orestis.velen.quiz.questions.Difficulty.EASY;
import static com.orestis.velen.quiz.questions.GameType.TYPE_A;
import static com.orestis.velen.quiz.repositories.GameTheme.GEOGRAPHY;

public class MainActivity extends AppCompatActivity implements LoadingBarStateListener, AnswerGivenListener,
        AnswerButtonStateListener, SampleSizeEndListener, BonusAwardedListener {

    private static final long PROGRESS_BAR_DURATION = 60000;
    private static final int PROGRESS_BAR_ANIMATION_DURATION = 1800;
    private static final int EXTRA_TIME = 3000;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startCountDownBar();

        player = PlayerHelper.getPlayerFromIntent(getIntent());

        HashMap<AnswerChoice, Button> buttons = new HashMap<>();
        buttons.put(A, (Button) findViewById(R.id.answerA));
        buttons.put(B, (Button) findViewById(R.id.answerB));
        buttons.put(C, (Button) findViewById(R.id.answerC));

        ImageView questionDisplay = findViewById(R.id.questionDisplay);

        face = Typeface.createFromAsset(getAssets(),"custom.ttf");

        RepositoryFactory repositoryFactory = new RepositoryFactory(GEOGRAPHY);
        questionHandler = new QuestionHandler(repositoryFactory, this);

        new QuestionDisplayHandler.Builder()
                .forImageView(questionDisplay)
                .fromRepository(repositoryFactory.getMediaRepository())
                .gameOf(TYPE_A)
                .withContext(this)
                .withQuestionHandler(questionHandler).buildAndInit();


        answerButtonsHandler = new AnswerButtonsHandler.Builder()
                .forButtons(buttons)
                .withTypeFace(face)
                .displayAnswerDurationFor(DISPLAY_BONUS_DURATION)
                .withQuestionHandler(questionHandler)
                .withButtonStateListener(this)
                .withAnswerGivenListener(this).buildAndInit();

        QuestionAnnouncement questionAnnouncement = (new QuestionAnnouncementFactory()).getQuestionAnouncement(GEOGRAPHY);
        questionTextHandler = new QuestionTextHandler(face, (TextView) findViewById(R.id.questionText), questionHandler, questionAnnouncement, TYPE_A);

        questionHandler.init(EASY, TYPE_A, LEVEL_QUESTION_SAMPLE);

        bonusTimeHandler = new BonusTimeHandler((TextView) findViewById(R.id.bonusTime));

        bonusManager = new StreakBonusManager(GEOGRAPHY, EASY, TYPE_A, this);
        bonusDisplayHandler = new StreakBonusDisplayHandler((TextView) findViewById(R.id.nowBonusTxt), (TextView) findViewById(R.id.accumBonusTxt), face, DISPLAY_BONUS_DURATION);

        new RoundProgressDisplayHandler((TextView) findViewById(R.id.questionProgressTxt),
                (TextView) findViewById(R.id.currentQuestionNumberTxt), LEVEL_QUESTION_SAMPLE, questionHandler, face);

        new FiftyFiftyButton.Builder().forPlayer(player)
                .useLayout((ConstraintLayout) findViewById(R.id.fiftyFiftyBtnLayout))
                .withAnswerButtons(buttons)
                .withQuestionHandler(questionHandler)
                .withContext(this).enable();

        new SkipButton.Builder().forPlayer(player)
                .useLayout((ConstraintLayout) findViewById(R.id.skipBtnLayout))
                .withQuestionHandler(questionHandler).enable();

        new FreezeTimeButton.Builder().forPlayer(player)
                .useLayout((ConstraintLayout) findViewById(R.id.freezeTimeBtnLayout))
                .useContext(this)
                .withLoadingBarHandler(loadingBarHandler)
                .withPowerIcon((ImageView) findViewById(R.id.freezeImg))
                .withTimerText((TextView) findViewById(R.id.freezeTimer)).enable();

        new ShieldButton.Builder().forPlayer(player)
                .useLayout((ConstraintLayout) findViewById(R.id.shieldLayout))
                .useShieldOnIcon((ImageView) findViewById(R.id.shieldImg))
                .useShieldBreakingIcon((ImageView) findViewById(R.id.shieldBreakingImg))
                .useshieldOverlay((FrameLayout) findViewById(R.id.shieldOverlay))
                .withContext(this)
                .withAnswerButtons(buttons)
                .withAnswerButtonsHandler(answerButtonsHandler)
                .withQuestionHandler(questionHandler).enable();

        new GameStartingScreen.Builder()
                .useBounceLoadingView((BounceLoadingView) findViewById(R.id.bounceLoading))
                .useCountText((TextView) findViewById(R.id.countDown))
                .forContainer((ConstraintLayout) findViewById(R.id.container))
                .useTypeface(face)
                .withContext(this).init();
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
    public void onLoadingBarFinished() {

    }

    @Override
    public void onLoadingBarFillAnimationEnd() {
        answerButtonsHandler.enableButtons(true);
    }

    @Override
    public void onCorrectAnswerGiven() {
        bonusManager.correctAnswerGiven();
        bonusTimeHandler.displayGainedTime(EXTRA_TIME, DISPLAY_BONUS_DURATION);
        loadingBarHandler.incrementLoadingBar(EXTRA_TIME, DISPLAY_BONUS_DURATION);
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
        answerButtonsHandler.enableButtons(false);
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
    public void onBonusAwarded(int bonusNow, int accumulatedBonus) {
        bonusDisplayHandler.displayBonus(bonusNow, accumulatedBonus);
    }
}
