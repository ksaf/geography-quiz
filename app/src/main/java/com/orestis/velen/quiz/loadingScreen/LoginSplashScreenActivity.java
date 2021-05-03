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
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.adverts.VideoAdManager;
import com.orestis.velen.quiz.login.UserSession;
import com.orestis.velen.quiz.login.firebase.FirebaseConnectedListener;
import com.orestis.velen.quiz.login.firebase.FirebaseConnectionHelper;
import com.orestis.velen.quiz.mainMenu.MainMenuActivity;
import com.orestis.velen.quiz.mainMenu.PlayerRecoveredListener;
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.player.PlayerSession;

public class LoginSplashScreenActivity extends AppCompatActivity implements FirebaseConnectedListener, PlayerRecoveredListener {

    private static final int RC_SIGN_IN = 0;
    private GoogleSignInAccount signedInAccount;

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
        signInSilently();
    }

    private void signInSilently() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestScopes(new Scope(Scopes.GAMES))
                .build();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (GoogleSignIn.hasPermissions(account, signInOptions.getScopeArray())) {
            // Already signed in.
            // The signed in account is stored in the 'account' variable.
            signedInAccount = account;
            getGoogleGamesPlayer(signedInAccount);
        } else {
            // Haven't been signed-in before. Try the silent sign-in first.
            GoogleSignInClient signInClient = GoogleSignIn.getClient(this, signInOptions);
            signInClient
            .silentSignIn()
            .addOnCompleteListener(
                    this,
                    new OnCompleteListener<GoogleSignInAccount>() {
                        @Override
                        public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                            if (task.isSuccessful()) {
                                // The signed in account is stored in the task's result.
                                signedInAccount = task.getResult();
                                getGoogleGamesPlayer(signedInAccount);
                            } else if (UserSession.getInstance().shouldAttemptAutomaticLogin(LoginSplashScreenActivity.this)){
                                startSignInIntent();
                            } else {
                                PlayerSession.getInstance().setConnected(false);
                                PlayerSession.getInstance().setWasConnectionError(false);
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        UserSession.getInstance().recoverPlayerFromLocalStorage(LoginSplashScreenActivity.this,
                                                getApplicationContext());
                                    }
                                }, 1000);
                            }
                        }
                    });
        }
    }

    private void startSignInIntent() {
        UserSession.getInstance().incrementAutoLoginAttempts(LoginSplashScreenActivity.this);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestScopes(new Scope(Scopes.GAMES))
        .build();
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this, signInOptions);
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    private void getGoogleGamesPlayer(GoogleSignInAccount googleAccount) {
        final FirebaseConnectionHelper connectionHelper = new FirebaseConnectionHelper();

        GamesClient gamesClient = Games.getGamesClient(this, googleAccount);
        gamesClient.setGravityForPopups(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        gamesClient.setViewForPopups(findViewById(R.id.gps_popup));

        PlayersClient playersClient = Games.getPlayersClient(LoginSplashScreenActivity.this, googleAccount);
        playersClient.getCurrentPlayer().addOnSuccessListener(new OnSuccessListener<com.google.android.gms.games.Player>() {
            @Override
            public void onSuccess(com.google.android.gms.games.Player player) {
                PlayerSession.getInstance().setCurrentPlayerName(player.getDisplayName());
                PlayerSession.getInstance().setConnected(true);
                PlayerSession.getInstance().setWasConnectionError(false);
                connectionHelper.firebaseAuthWithGoogle(LoginSplashScreenActivity.this,
                        signedInAccount, LoginSplashScreenActivity.this);
                UserSession.getInstance().resetAutoLoginAttempts(LoginSplashScreenActivity.this);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                PlayerSession.getInstance().setConnected(false);
                PlayerSession.getInstance().setWasConnectionError(true);
                UserSession.getInstance().recoverPlayerFromLocalStorage(LoginSplashScreenActivity.this,
                        getApplicationContext());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // The signed in account is stored in the result.
                signedInAccount = result.getSignInAccount();
                getGoogleGamesPlayer(signedInAccount);
            } else {
                PlayerSession.getInstance().setConnected(false);
                PlayerSession.getInstance().setWasConnectionError(false);
                UserSession.getInstance().recoverPlayerFromLocalStorage(LoginSplashScreenActivity.this,
                        getApplicationContext());
            }
        }
    }

    @Override
    public void onPlayerRecovered(final Player player, final boolean fromLocalStorage) {
        PlayerSession.getInstance().setRecoveredPlayer(player);
        nextActivity();
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
