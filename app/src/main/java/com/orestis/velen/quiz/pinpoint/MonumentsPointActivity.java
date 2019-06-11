package com.orestis.velen.quiz.pinpoint;

import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.orestis.velen.quiz.loadingBar.LoadingBarHandler;
import com.orestis.velen.quiz.loadingBar.LoadingBarStateListener;
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.player.PlayerHelper;
import com.orestis.velen.quiz.questions.QuestionHandler;
import com.orestis.velen.quiz.repositories.RepositoryFactory;
import com.orestis.velen.quiz.repositories.SampleSizeEndListener;
import com.orestis.velen.quiz.roundProgressDisplay.RoundProgressDisplayHandler;
import com.orestis.velen.quiz.victoryFragment.VictoryFragment;

import static com.orestis.velen.quiz.loadingBar.TimerDirection.DOWN;
import static com.orestis.velen.quiz.questions.Difficulty.EASY;
import static com.orestis.velen.quiz.questions.GameType.TYPE_D;
import static com.orestis.velen.quiz.questions.GameType.TYPE_E;
import static com.orestis.velen.quiz.repositories.GameTheme.GEOGRAPHY;

public class MonumentsPointActivity extends AppCompatActivity implements SampleSizeEndListener, DisplayDistanceDurationEndListener,
        BonusAwardedListener, PinpointAnswerGivenListener, LoadingBarStateListener, FrameResizeHandler {

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
        setContentView(R.layout.activity_monuments_point);

        ImageView map = findViewById(R.id.mapId);

        startCountDownBar();

        player = PlayerHelper.getPlayerFromIntent(getIntent());

        Typeface face = Typeface.createFromAsset(getAssets(),"custom.ttf");

        DistanceTextHandler distanceTextHandler = new DistanceTextHandler(DISPLAY_DISTANCE_DURATION, (TextView) findViewById(R.id.distanceText), this);

        RepositoryFactory repositoryFactory = new RepositoryFactory(GEOGRAPHY);
        questionHandler = new QuestionHandler(repositoryFactory, this);

        new MonumentNameHandler((TextView) findViewById(R.id.monumentName), questionHandler);

        new MonumentImageHandler((ImageView) findViewById(R.id.landmarkId), questionHandler, this, this);

        MapTouchListener mapTouchListener = new MapTouchListener.Builder()
                .forMap(map)
                .withContext(this)
                .withDelayedAnswerDuration(DELAYED_ANSWER_DURATION)
                .withQuestionHandler(questionHandler)
                .withDistanceTextHandler(distanceTextHandler)
                .withAnswerGivenListener(this).build();
        map.setOnTouchListener(mapTouchListener);

        questionHandler.init(EASY, TYPE_E, LEVEL_QUESTION_SAMPLE);

        bonusTimeHandler = new BonusTimeHandler((TextView) findViewById(R.id.bonusTime));

        bonusManager = new StreakBonusManager(GEOGRAPHY, EASY, TYPE_D, this);
        bonusDisplayHandler = new StreakBonusDisplayHandler((TextView) findViewById(R.id.nowBonusTxt), (TextView) findViewById(R.id.accumBonusTxt), face, DISPLAY_BONUS_DURATION);

        new RoundProgressDisplayHandler((TextView) findViewById(R.id.questionProgressTxt),
                (TextView) findViewById(R.id.currentQuestionNumberTxt), LEVEL_QUESTION_SAMPLE, questionHandler, face);
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
}
