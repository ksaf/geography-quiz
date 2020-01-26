package com.orestis.velen.quiz.loadingScreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.adverts.VideoAdManager;
import com.orestis.velen.quiz.login.UserSession;
import com.orestis.velen.quiz.login.firebase.FirebaseConnectedListener;
import com.orestis.velen.quiz.login.firebase.FirebaseConnectionHelper;
import com.orestis.velen.quiz.login.googleSignIn.GoogleSession;
import com.orestis.velen.quiz.mainMenu.MainMenuActivity;
import com.orestis.velen.quiz.mainMenu.PlayerRecoveredListener;
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.player.PlayerSession;

public class LoginSplashScreenActivity extends AppCompatActivity implements FirebaseConnectedListener, PlayerRecoveredListener {

    private GoogleSignInAccount lastGoogleAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_splash_screen_activity);
        enableBounceLoading();
        VideoAdManager.getInstance().initialise(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startConnecting();
    }

    private void startConnecting() {
        lastGoogleAccount = GoogleSession.getInstance().getLastGoogleAccount(this);
        FirebaseConnectionHelper connectionHelper = new FirebaseConnectionHelper();
        if(lastGoogleAccount != null) {
            connectionHelper.firebaseAuthWithGoogle(this, lastGoogleAccount, this);
        } else {
            PlayerSession.getInstance().setConnected(false);
            UserSession.getInstance().recoverPlayerFromLocalStorage(this, getApplicationContext());
        }
    }

    private void getGoogleGamesName(GoogleSignInAccount googleAccount) {
        Games.getGamesClient(this, googleAccount).setViewForPopups(findViewById(R.id.gps_popup));
        PlayersClient mPlayersClient = Games.getPlayersClient(this, googleAccount);
        mPlayersClient.getCurrentPlayer()
                .addOnCompleteListener(new OnCompleteListener<com.google.android.gms.games.Player>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.android.gms.games.Player> task) {
                        if (task.isSuccessful()) {
                            PlayerSession.getInstance().setCurrentPlayerName(task.getResult().getDisplayName());
                        }
                        PlayerSession.getInstance().setConnected(task.isSuccessful());
                        PlayerSession.getInstance().setWasConnectionError(!task.isSuccessful());
                        nextActivity();
                    }
                });
    }

    @Override
    public void onPlayerRecovered(final Player player, final boolean fromLocalStorage) {
        PlayerSession.getInstance().setRecoveredPlayer(player);
        if(lastGoogleAccount != null) {
            getGoogleGamesName(lastGoogleAccount);
        } else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    nextActivity();
                }
            }, 1000);
        }
    }

    @Override
    public void onPlayerRecoveryFromFirebaseFailed() {
        onFirebaseError();
    }

    private void nextActivity() {
        Intent intent = new Intent(LoginSplashScreenActivity.this, MainMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        LoginSplashScreenActivity.this.startActivity(intent);
        LoginSplashScreenActivity.this.finish();
    }

    @Override
    public void onFirebaseConnected(FirebaseUser firebaseUser) {
        UserSession.getInstance().recoverPlayerFromFirebase(firebaseUser, this, getApplicationContext());
    }

    @Override
    public void onFirebaseError() {
        UserSession.getInstance().recoverPlayerFromLocalStorage(this, getApplicationContext());
        PlayerSession.getInstance().setWasConnectionError(true);
    }

    @Override
    public void onWasAlreadyConnectedToFirebase(FirebaseUser firebaseUser) {
        onFirebaseConnected(firebaseUser);
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
}
