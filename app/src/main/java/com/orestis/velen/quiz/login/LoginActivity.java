package com.orestis.velen.quiz.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.firebase.auth.FirebaseUser;
import com.ldoublem.ringPregressLibrary.Ring;
import com.ldoublem.ringPregressLibrary.RingProgress;
import com.orestis.velen.quiz.MainActivity;
import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.achievements.GoogleAchievements;
import com.orestis.velen.quiz.findPoints.FindPointsActivity;
import com.orestis.velen.quiz.gameTypes.CapitalsMenuFragment;
import com.orestis.velen.quiz.gameTypes.FlagsMenuFragment;
import com.orestis.velen.quiz.gameTypes.MapsMenuFragment;
import com.orestis.velen.quiz.gameTypes.ScreenSlidePagerAdapter;
import com.orestis.velen.quiz.leaderboard.GoogleLeaderboard;
import com.orestis.velen.quiz.login.errors.ConnectionError;
import com.orestis.velen.quiz.login.facebookSignIn.FacebookSession;
import com.orestis.velen.quiz.login.facebookSignIn.FacebookSignInActivity;
import com.orestis.velen.quiz.login.facebookSignIn.FacebookSignInButton;
import com.orestis.velen.quiz.login.firebase.FirebaseConnectedListener;
import com.orestis.velen.quiz.login.firebase.FirebaseConnectionHelper;
import com.orestis.velen.quiz.login.googleSignIn.GoogleSession;
import com.orestis.velen.quiz.login.googleSignIn.GoogleSignInActivity;
import com.orestis.velen.quiz.login.googleSignIn.GoogleSignInButton;
import com.orestis.velen.quiz.login.logout.LogoutButton;
import com.orestis.velen.quiz.pinpoint.CapitalsPointActivity;
import com.orestis.velen.quiz.pinpoint.MonumentsPointActivity;
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.player.PlayerHelper;

import java.util.ArrayList;
import java.util.List;

import devlight.io.library.ntb.NavigationTabBar;

import static com.orestis.velen.quiz.achievements.GoogleAchievements.RC_ACHIEVEMENTS;
import static com.orestis.velen.quiz.leaderboard.GoogleLeaderboard.RC_LEADERBOARD;

public class LoginActivity extends AppCompatActivity implements PlayerRecoveredListener,
        FirebaseConnectedListener, SignInRequestHandler, ViewPager.OnPageChangeListener {

    private static final int GOOGLE_SIGN_IN = 2;
    private static final int FACEBOOK_SIGN_IN = 3;
    private SocialSignIn socialSignIn;
    private ConnectionError connectionError = new ConnectionError(this);
    private ViewPager viewPager;
    private RingProgress mRingProgress;
    private Player recoveredPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_legacy);

        initViewScales((ViewGroup) findViewById(R.id.playerInfo));
        initViewScales((ViewGroup) findViewById(R.id.startOptions));

        LogoutButton logoutButton = new LogoutButton((Button) findViewById(R.id.LogoutBtn));
        GoogleSignInButton googleSignInButton = new GoogleSignInButton((ImageView) findViewById(R.id.googleSignInBtn), new View.OnClickListener() {
            @Override
            public void onClick(View view) { googleSignIn();}
        });
        FacebookSignInButton facebookSignInButton = new FacebookSignInButton((ImageView) findViewById(R.id.facebookSignInBtn), new View.OnClickListener() {
            @Override
            public void onClick(View view) { facebookSignIn();}
        });
        socialSignIn = new SocialSignIn.Builder().withContext(this)
                .withFacebookButton(facebookSignInButton)
                .withGoogleButton(googleSignInButton)
                .withLogoutButton(logoutButton)
                .withProfileImg((ImageView) findViewById(R.id.profileImg))
                .withProfileName((TextView) findViewById(R.id.profileName)).build();

        GoogleSignInAccount lastGoogleAccount = GoogleSession.getInstance().getLastGoogleAccount(this);
        AccessToken lastAccessToken = FacebookSession.getInstance().getLastFacebookToken();
        FirebaseConnectionHelper connectionHelper = new FirebaseConnectionHelper();
        if(lastGoogleAccount != null) {
            connectionHelper.firebaseAuthWithGoogle(this, lastGoogleAccount, this);
            socialSignIn.setGoogleSignedIn(lastGoogleAccount, (FrameLayout) findViewById(R.id.gps_popup));
        } else if(lastAccessToken != null && !lastAccessToken.isExpired()) {
            connectionHelper.firebaseAuthWithFacebook(this, lastAccessToken, this);
            socialSignIn.setFacebookSignedIn();
        } else {
            socialSignIn.setSignedOutUI();
        }

        Button achievementsBtn = findViewById(R.id.achievementsBtn);
        achievementsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleAchievements achievements = new GoogleAchievements();
                achievements.showAchievements(LoginActivity.this, LoginActivity.this, R.id.errorMessagePlaceholder);
            }
        });

        Button leaderboardBtn = findViewById(R.id.leaderboardBtn);
        leaderboardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleLeaderboard leaderboard = new GoogleLeaderboard();
                leaderboard.showLeaderboard(LoginActivity.this, LoginActivity.this, R.id.errorMessagePlaceholder);
            }
        });

        enableNavBar();

        mRingProgress = findViewById(R.id.ring_progress);
        Ring r = new Ring(0, "", "XP", Color.rgb(249, 215, 28), Color.rgb(249, 215, 28));
        List<Ring> mlistRing = new ArrayList<>();
        mlistRing.add(r);
        mRingProgress.setData(mlistRing, 100);

        Button settingsBtn = findViewById(R.id.settingsBtn);
        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, FindPointsActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });

        enableBounceLoading();
    }

    private void googleSignIn() {
        if(!isNetworkAvailable()) {
            showError("Please connect to a network, then try again!", 3000);
        } else {
            startActivityForResult(new Intent(this, GoogleSignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION), GOOGLE_SIGN_IN);
        }
    }

    private void facebookSignIn() {
        if(!isNetworkAvailable()) {
            showError("Please connect to a network, then try again!", 3000);
        } else {
            startActivityForResult(new Intent(this, FacebookSignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION), FACEBOOK_SIGN_IN);
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
        switch (selected) {
            case 0:
                activityToStart = CapitalsPointActivity.class;
                break;
            case 1:
                activityToStart = MainActivity.class;
                break;
            case 2:
                activityToStart = MonumentsPointActivity.class;
                break;
            default:
                activityToStart = MainActivity.class;
                break;
        }
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, activityToStart);
                intent = PlayerHelper.addPlayerToIntent(intent, recoveredPlayer);
                LoginActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN || requestCode == FACEBOOK_SIGN_IN) {
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
            socialSignIn.setSignedOutUI();
        }
        if(resultCode == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED && requestCode == RC_LEADERBOARD) {
            socialSignIn.setSignedOutUI();
        }
    }

    @Override
    public void onPlayerRecovered(Player player) {
        this.recoveredPlayer = player;
        enableStartButton(1);
        stopBounceLoading();
        animateUIAppearance((ViewGroup) findViewById(R.id.playerInfo));
        animateUIAppearance((ViewGroup) findViewById(R.id.startOptions));
        animateViewPagerAppearance();

        int xpPercentage = (int)(player.getCurrentXP() * 100.0f) / player.getXpToLevel();
        Ring r = new Ring(xpPercentage, String.valueOf(xpPercentage) + "%", "XP", Color.rgb(249, 215, 28), Color.rgb(249, 215, 28));
        List<Ring> mlistRing = new ArrayList<>();
        mlistRing.add(r);
        mRingProgress.setData(mlistRing, 100);
    }

    @Override
    public void onFirebaseConnected(FirebaseUser firebaseUser) {
        UserSession.getInstance().recoverPlayer(firebaseUser, this, getApplicationContext());
        socialSignIn.setAlreadySignedIn((FrameLayout) findViewById(R.id.gps_popup));
    }

    @Override
    public void onFirebaseError() {
        showError("Connection error, please try again!", 3000);
        UserSession.getInstance().recoverPlayerFromLocalStorage(this, getApplicationContext());
        socialSignIn.setSignedOutUI();
    }

    @Override
    public void onGoogleSignInRequested() {
        googleSignIn();
    }

    private void enableNavBar() {
        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.flags_icon),
                        Color.parseColor("#111e6c")
                ).build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.map_icon),
                        Color.parseColor("#111e6c")
                ).build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.capitals_icon),
                        Color.parseColor("#111e6c")
                ).build()
        );
        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(getViewPager(), 1);
    }

    private ViewPager getViewPager() {
        viewPager = findViewById(R.id.gameTypesViewPager);
        PagerAdapter adapter = new ScreenSlidePagerAdapter.Builder()
                .useFragmentManager(getSupportFragmentManager())
                .addFragment(new FlagsMenuFragment())
                .addFragment(new MapsMenuFragment())
                .addFragment(new CapitalsMenuFragment())
                .build();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        return viewPager;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
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

    private void enableBounceLoading() {
        TextView loadingText = findViewById(R.id.loadingText);
        Typeface face = Typeface.createFromAsset(getAssets(),"custom.ttf");
        loadingText.setTypeface(face);
        loadingText.setVisibility(View.VISIBLE);
        BounceLoadingView bounceLoading = findViewById(R.id.bounceLoading);
        bounceLoading.setVisibility(View.VISIBLE);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, metrics);
        for(int i = 1; i <= 11; i++) {
            int id = getResources().getIdentifier("landmark" + i, "drawable", getPackageName());
            Bitmap bm = BitmapFactory.decodeResource(getResources(), id);
            bm = Bitmap.createScaledBitmap(bm, size, size, false);
            bounceLoading.addBitmap(bm);
        }
        bounceLoading.shuffleBitmaps();
        bounceLoading.setShadowColor(Color.LTGRAY);
        bounceLoading.setDuration(800);
        bounceLoading.start();
    }

    private void stopBounceLoading() {
        final TextView loadingText = findViewById(R.id.loadingText);
        ViewPropertyAnimatorCompat viewAnimator;
        viewAnimator = ViewCompat.animate(loadingText)
                .translationX(1000)
                .setDuration(300);
        viewAnimator.setInterpolator(new DecelerateInterpolator()).withEndAction(new Runnable() {
            @Override
            public void run() {
                loadingText.setVisibility(View.INVISIBLE);
                int[] location = new int[2];
                loadingText.getLocationOnScreen(location);
                loadingText.setX(location[0] - 1000);
            }
        }).start();

        BounceLoadingView bounceLoading = findViewById(R.id.bounceLoading);
        bounceLoading.stop();
        bounceLoading.setVisibility(View.INVISIBLE);
    }
}
