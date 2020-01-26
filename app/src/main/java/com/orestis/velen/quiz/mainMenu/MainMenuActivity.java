package com.orestis.velen.quiz.mainMenu;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.achievements.GoogleAchievements;
import com.orestis.velen.quiz.adverts.VideoAdManager;
import com.orestis.velen.quiz.adverts.XpBoostEnabledListener;
import com.orestis.velen.quiz.adverts.XpBoostFragment;
import com.orestis.velen.quiz.adverts.XpBoostFragmentActive;
import com.orestis.velen.quiz.adverts.XpBoostTimeLeftTimer;
import com.orestis.velen.quiz.flags.FlagsActivity;
import com.orestis.velen.quiz.language.LocaleHelper;
import com.orestis.velen.quiz.leaderboard.GoogleLeaderboard;
import com.orestis.velen.quiz.login.UserSession;
import com.orestis.velen.quiz.login.firebase.FirebaseConnectedListener;
import com.orestis.velen.quiz.login.googleSignIn.GoogleSession;
import com.orestis.velen.quiz.login.googleSignIn.GoogleSignInActivity;
import com.orestis.velen.quiz.login.googleSignIn.GoogleSignInButton;
import com.orestis.velen.quiz.mainMenu.errors.ConnectionError;
import com.orestis.velen.quiz.menuGameTypeFragments.FlagsMenuFragment;
import com.orestis.velen.quiz.menuGameTypeFragments.MapsMenuFragment;
import com.orestis.velen.quiz.menuGameTypeFragments.MonumentsMenuFragment;
import com.orestis.velen.quiz.menuGameTypeFragments.OutlineToFlagsMenuFragment;
import com.orestis.velen.quiz.menuGameTypeFragments.ScreenSlidePagerAdapter;
import com.orestis.velen.quiz.outlines.MainActivity;
import com.orestis.velen.quiz.outlinesToFlags.OutlinesToFlagsActivity;
import com.orestis.velen.quiz.pinpoint.CapitalsPointActivity;
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.player.PlayerHelper;
import com.orestis.velen.quiz.player.PlayerSession;
import com.orestis.velen.quiz.questions.Difficulty;
import com.orestis.velen.quiz.questions.DifficultyHelper;
import com.orestis.velen.quiz.settings.SettingsFragment;
import com.orestis.velen.quiz.skillUpgrades.SkillSumChangeListener;
import com.orestis.velen.quiz.skillUpgrades.SkillUpgradesFragment;
import com.orestis.velen.quiz.sound.BackgroundMusicLoadedListener;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

import static com.orestis.velen.quiz.achievements.GoogleAchievements.RC_ACHIEVEMENTS;
import static com.orestis.velen.quiz.leaderboard.GoogleLeaderboard.RC_LEADERBOARD;

public class MainMenuActivity extends AppCompatActivity implements PlayerRecoveredListener,
        FirebaseConnectedListener, SignInRequestHandler, ViewPager.OnPageChangeListener,
        GameStartRequestListener, SkillSumChangeListener, BackgroundMusicLoadedListener,
        XpBoostEnabledListener {

    private static final int GOOGLE_SIGN_IN = 2;
    private SocialSignInUIHandler socialSignInUIHandler;
    private ConnectionError connectionError = new ConnectionError(this);
    private ViewPager viewPager;
    private Player recoveredPlayer;
    private SkillUpgradesFragment skillUpgradesFragment;
    private ImageView darkBg;
    private SoundPoolHelper soundHelper;
    public static final int GAME_TYPE_FLAGS = 0;
    public static final int GAME_TYPE_OUTLINE_TO_FLAGS = 1;
    public static final int GAME_TYPE_OUTLINES = 2;
    public static final int GAME_TYPE_MONUMENTS = 3;
    public static final String SHOULD_ANIMATE_ENTRANCE = "shouldAnimateEntrance";
    public static final int XP_BOOST_DURATION = 300000;
    public static final String XP_BOOST_ENABLED = "xpBoostEnabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        if(getIntent().getBooleanExtra(SHOULD_ANIMATE_ENTRANCE, true)) {
            initViewScales((ViewGroup) findViewById(R.id.playerInfo));
            initViewScales((ViewGroup) findViewById(R.id.startOptions));
        }

        darkBg = findViewById(R.id.darkBg);

        soundHelper = new SoundPoolHelper(5, this);
        soundHelper.loadMainMenuSounds();
        soundHelper.setBackgroundMusicLoadedListener(this);

        Button achievementsBtn = findViewById(R.id.achievementsBtn);
        achievementsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundHelper.playMenuBtnOpenSound();
                GoogleAchievements achievements = new GoogleAchievements();
                achievements.showAchievements(MainMenuActivity.this, MainMenuActivity.this, R.id.errorMessagePlaceholder);
            }
        });

        Button leaderboardBtn = findViewById(R.id.leaderboardBtn);
        leaderboardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundHelper.playMenuBtnOpenSound();
                GoogleLeaderboard leaderboard = new GoogleLeaderboard();
                leaderboard.showLeaderboard(MainMenuActivity.this, MainMenuActivity.this, R.id.errorMessagePlaceholder);
            }
        });

        setViewPager();

        GoogleSignInButton googleSignInButton = new GoogleSignInButton((ImageView) findViewById(R.id.googleSignInBtn), new View.OnClickListener() {
            @Override
            public void onClick(View view) { googleSignIn();}
        }, this);
        socialSignInUIHandler = new SocialSignInUIHandler((TextView) findViewById(R.id.profileName), googleSignInButton);

        Button settingsBtn = findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainMenuActivity.this, FindPointsActivity2.class);
//                MainMenuActivity.this.startActivity(intent);

                soundHelper.playMenuBtnOpenSound();
                SettingsFragment settingsFragment = new SettingsFragment.Builder()
                        .withDarkBg(darkBg)
                        .withSocialSignInUIHandler(socialSignInUIHandler)
                        .withPlayerRecoveredListener(MainMenuActivity.this)
                        .withSoundPoolHelper(soundHelper)
                        .build();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.optionScreenPlaceholder, settingsFragment, "SettingsTag");
                ft.addToBackStack("settings");
                ft.commit();
            }
        });

        recoveredPlayer = PlayerSession.getInstance().getCurrentPlayer();
        if(PlayerSession.getInstance().isConnected()) {
            socialSignInUIHandler.setSignedInUI();
        } else {
            socialSignInUIHandler.setSignedOutUI();
        }
        onPlayerRecoveredAction(recoveredPlayer);

        if(PlayerSession.getInstance().isWasConnectionError()) {
            showError(getString(R.string.connectionError), 3000);
        }

        ImageButton adBtn = findViewById(R.id.adBtn);
        adBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundHelper.playMenuBtnOpenSound();
                if(recoveredPlayer.hasXpBoostEnabled(XP_BOOST_DURATION)) {
                    XpBoostFragmentActive xpBoostFragmentActive = new XpBoostFragmentActive.Builder()
                            .withDarkBg(darkBg)
                            .withSoundPoolHelper(soundHelper)
                            .build();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.optionScreenPlaceholder, xpBoostFragmentActive).commit();
                } else {
                    XpBoostFragment xpBoostFragment = new XpBoostFragment.Builder()
                            .withDarkBg(darkBg)
                            .withSoundPoolHelper(soundHelper)
                            .withVideoAdManager(VideoAdManager.getInstance())
                            .withXpBoostEnabledListener(MainMenuActivity.this)
                            .build();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.optionScreenPlaceholder, xpBoostFragment);
                    ft.commit();
                }
            }
        });
        if(PlayerSession.getInstance().getCurrentPlayer().hasXpBoostEnabled(XP_BOOST_DURATION)) {
            onXpBoostEnabled();
        }

    }

    @Override
    public void onXpBoostEnabled() {
        TextView xpBoostTimeLeftTxt = findViewById(R.id.xpBoostTimeLeftTxt);
        ViewGroup xpBoostContainer = findViewById(R.id.xpBoostContainer);
        xpBoostContainer.setVisibility(View.VISIBLE);
        XpBoostTimeLeftTimer xpBoostTimeLeftTimer =
                new XpBoostTimeLeftTimer(xpBoostContainer, xpBoostTimeLeftTxt,
                        PlayerSession.getInstance().getCurrentPlayer().getXpBoostEnabledTimeLeft(XP_BOOST_DURATION),
                        1000, this);
        xpBoostTimeLeftTimer.start();
    }

    @Override
    public void onBackgroundMusicLoaded() {
        soundHelper.playMenuBackgroundMusic();
    }

    private void googleSignIn() {
        if(!isNetworkAvailable()) {
            showError(getString(R.string.networkError), 3000);
        } else {
            startActivityForResult(new Intent(this, GoogleSignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION), GOOGLE_SIGN_IN);
        }
    }

    private void showError(String message, int duration) {
        connectionError.show(duration, R.id.errorMessagePlaceholder, message);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void enableStartButton(int selected) {
        Button startBtn = findViewById(R.id.startBtn);
        final Class activityToStart;
        final String gameTypeString;
        switch (selected) {
            case GAME_TYPE_FLAGS:
                activityToStart = FlagsActivity.class;
                gameTypeString = getString(R.string.flags);
                break;
            case GAME_TYPE_OUTLINE_TO_FLAGS:
                activityToStart = OutlinesToFlagsActivity.class;
                gameTypeString = getString(R.string.mapToFlag);
                break;
            case GAME_TYPE_OUTLINES:
                activityToStart = MainActivity.class;
                gameTypeString = getString(R.string.maps);
                break;
            case GAME_TYPE_MONUMENTS:
                activityToStart = CapitalsPointActivity.class;
                gameTypeString = getString(R.string.landmarks);
                break;
            default:
                activityToStart = FlagsActivity.class;
                gameTypeString = getString(R.string.flags);
                break;
        }
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGameStartRequest(activityToStart, gameTypeString);
            }
        });
    }

    @Override
    public void onGameStartRequest(Class activityToStart, String gameType) {
        soundHelper.playMenuBtnOpenSound();
        GameStartConfirmationFragment gameStartConfirmationFragment = new GameStartConfirmationFragment.Builder()
                .withDarkBg(darkBg)
                .forGameType(gameType)
                .forActivityToStart(activityToStart)
                .useGameStartRequestListener(this)
                .withSoundPoolHelper(soundHelper)
                .build();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.gameStartConfirmationPlaceholder, gameStartConfirmationFragment);
        ft.commit();
    }

    @Override
    public void onGameStartConfirm(Class activityToStart, Difficulty difficulty, boolean xpBoostEnabled) {
        Intent intent = new Intent(MainMenuActivity.this, activityToStart);
        intent = PlayerHelper.addPlayerToIntent(intent, recoveredPlayer);
        intent = DifficultyHelper.addDifficultyToIntent(intent, difficulty);
        intent.putExtra(XP_BOOST_ENABLED, xpBoostEnabled);
        MainMenuActivity.this.startActivity(intent);
        MainMenuActivity.this.finish();
    }

    private void showRemainingSkillPointsButton(final Player player) {
        Button skillPlusBtn = findViewById(R.id.skillPlusBtn);
        skillPlusBtn.setText(getString(R.string.skills) + " (" + player.getRemainingSkillPoints() + ")");
        skillPlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSkillPointAllocation(player);
            }
        });
    }

    private void openSkillPointAllocation(Player player) {
        soundHelper.playMenuBtnOpenSound();
        skillUpgradesFragment = new SkillUpgradesFragment.Builder()
                .forPlayer(player)
                .withDarkBg(darkBg)
                .withSoundPoolHelper(soundHelper)
                .withSkillSumChangeListener(this)
                .build();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.optionScreenPlaceholder, skillUpgradesFragment);
        ft.commit();
    }

    @Override
    public void onSkillSumChange(int remainingSkillPoints) {
        Button skillPlusBtn = findViewById(R.id.skillPlusBtn);
        skillPlusBtn.setText(getString(R.string.skills) + " (" + remainingSkillPoints + ")");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                FirebaseUser user = data.getParcelableExtra("firebaseUser");
                if(user != null) {
                    onFirebaseConnected(user);
                } else {
                    onFirebaseError();
                }
            } else if(resultCode == RESULT_CANCELED){
                onFirebaseError();
            }
        }
        if(resultCode == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED && requestCode == RC_ACHIEVEMENTS) {
            socialSignInUIHandler.setSignedOutUI();
        }
        if(resultCode == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED && requestCode == RC_LEADERBOARD) {
            socialSignInUIHandler.setSignedOutUI();
        }
    }

    @Override
    public void onPlayerRecovered(final Player player, final boolean fromLocalStorage) {
        PlayerSession.getInstance().setConnected(true);
        onPlayerRecoveredAction(player);
    }

    @Override
    public void onPlayerRecoveryFromFirebaseFailed() {

    }

    private void onPlayerRecoveredAction(Player player) {
        this.recoveredPlayer = player;
        PlayerSession.getInstance().setRecoveredPlayer(player);
        enableStartButton(0);
        showRemainingSkillPointsButton(player);
        TextView mainMenuProfileLevel = ((ViewGroup) findViewById(R.id.playerInfo)).findViewById(R.id.mainMenuProfileLevel);
        mainMenuProfileLevel.setText(getString(R.string.mainMenuLevel) + Integer.toString(recoveredPlayer.getCurrentLevel()));
        animateUIAppearance((ViewGroup) findViewById(R.id.playerInfo));
        animateUIAppearance((ViewGroup) findViewById(R.id.startOptions));
        animateViewPagerAppearance();

        int xpPercentage = (int)(player.getCurrentXP() * 100.0f) / player.getXpToLevel();

        RoundCornerProgressBar mainMenuLoadingBar = findViewById(R.id.mainMenuLoadingBar);
        TextView mainMenuXPText = findViewById(R.id.mainMenuXPText);
        mainMenuLoadingBar.setProgress(xpPercentage);
        mainMenuXPText.setText(String.valueOf(xpPercentage) + "%");
    }

    @Override
    public void onFirebaseConnected(FirebaseUser firebaseUser) {
        UserSession.getInstance().recoverPlayerFromFirebase(firebaseUser, this, getApplicationContext());
        GoogleSignInAccount lastGoogleAccount = GoogleSession.getInstance().getLastGoogleAccount(this);
        if(lastGoogleAccount != null) {
            Games.getGamesClient(this, lastGoogleAccount).setViewForPopups(findViewById(R.id.gps_popup));
            PlayersClient mPlayersClient = Games.getPlayersClient(this, lastGoogleAccount);
            mPlayersClient.getCurrentPlayer()
                    .addOnCompleteListener(new OnCompleteListener<com.google.android.gms.games.Player>() {
                        @Override
                        public void onComplete(@NonNull Task<com.google.android.gms.games.Player> task) {
                            if (task.isSuccessful()) {
                                PlayerSession.getInstance().setCurrentPlayerName(task.getResult().getDisplayName());
                                socialSignInUIHandler.setSignedInUI();
                            } else {
                                socialSignInUIHandler.setSignedOutUI();
                            }
                        }
                    });
        }
    }

    @Override
    public void onWasAlreadyConnectedToFirebase(FirebaseUser firebaseUser) {
        onFirebaseConnected(firebaseUser);
    }

    @Override
    public void onFirebaseError() {
        showError(getString(R.string.connectionError2), 3000);
        UserSession.getInstance().recoverPlayerFromLocalStorage(this, getApplicationContext());
        socialSignInUIHandler.setSignedOutUI();
    }

    @Override
    public void onGoogleSignInRequested() {
        googleSignIn();
    }

    private ViewPager setViewPager() {
        viewPager = findViewById(R.id.gameTypesViewPager);
        FlagsMenuFragment flagsMenuFragment = new FlagsMenuFragment();
        flagsMenuFragment.useInViewPager(viewPager);
        flagsMenuFragment.setGameStartRequestListener(this);
        OutlineToFlagsMenuFragment outlineToFlagsMenuFragment = new OutlineToFlagsMenuFragment();
        outlineToFlagsMenuFragment.useInViewPager(viewPager);
        outlineToFlagsMenuFragment.setGameStartRequestListener(this);
        MapsMenuFragment mapsMenuFragment = new MapsMenuFragment();
        mapsMenuFragment.useInViewPager(viewPager);
        mapsMenuFragment.setGameStartRequestListener(this);
        MonumentsMenuFragment monumentsMenuFragment = new MonumentsMenuFragment();
        monumentsMenuFragment.useInViewPager(viewPager);
        monumentsMenuFragment.setGameStartRequestListener(this);
        PagerAdapter adapter = new ScreenSlidePagerAdapter.Builder()
                .useFragmentManager(getSupportFragmentManager())
                .addFragment(flagsMenuFragment)
                .addFragment(mapsMenuFragment)
                .addFragment(outlineToFlagsMenuFragment)
                .addFragment(monumentsMenuFragment)
                .build();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        TabLayout tabLayout = findViewById(R.id.viewPagerIndicator);
        tabLayout.setupWithViewPager(viewPager, true);
        return viewPager;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
//        pageIndicatorView.setSelection(position);
        enableStartButton(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void animateUIAppearance(ViewGroup container) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View view = container.getChildAt(i);
            ViewPropertyAnimatorCompat viewAnimator;

            viewAnimator = ViewCompat.animate(view)
                    .scaleY(1).scaleX(1)
                    .setStartDelay((50 * i))
                    .setDuration(500);

            viewAnimator.setInterpolator(new DecelerateInterpolator()).start();
        }
    }

    private void animateViewPagerAppearance() {
        ViewPager viewPager = findViewById(R.id.gameTypesViewPager);
        ViewPropertyAnimatorCompat viewAnimator;
        viewAnimator = ViewCompat.animate(viewPager)
                .alpha(1)
                .setDuration(1000);
        viewAnimator.setInterpolator(new DecelerateInterpolator()).start();
    }

    private void initViewScales(ViewGroup container) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View view = container.getChildAt(i);
            view.setScaleX(0f);
            view.setScaleY(0f);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundHelper.pauseMenuBackgroundMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        soundHelper.resumeMenuBackgroundMusic();
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