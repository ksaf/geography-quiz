package com.orestis.velen.quiz.login.googleSignIn;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.EventsClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.orestis.velen.quiz.R;

public class GoogleSession {

    private static GoogleSession instance;
    private GoogleSignInClient mGoogleSignInClient;
    private AchievementsClient mAchievementsClient;
    private LeaderboardsClient mLeaderboardsClient;
    private EventsClient mEventsClient;

    private GoogleSession(){}

    public static GoogleSession getInstance() {
        if(instance == null) {
            instance =  new GoogleSession();
        }
        return instance;
    }

    public void setGoogleSignInClient(GoogleSignInClient mGoogleSignInClient) {
        this.mGoogleSignInClient = mGoogleSignInClient;
    }

    public GoogleSignInAccount getLastGoogleAccount(Context context) {
        return GoogleSignIn.getLastSignedInAccount(context);
    }

    public AchievementsClient getAchievementsClient(Context context) {
        if(isSignedIn(context)) {
            if(mAchievementsClient == null) {
                mAchievementsClient = Games.getAchievementsClient(context, getLastGoogleAccount(context));
            }
            return mAchievementsClient;
        }
        return null;
    }

    public LeaderboardsClient getLeaderboardsClient(Context context) {
        if(isSignedIn(context)) {
            if (mLeaderboardsClient == null) {
                mLeaderboardsClient = Games.getLeaderboardsClient(context, getLastGoogleAccount(context));
            }
            return mLeaderboardsClient;
        }
        return null;
    }

    public EventsClient getEventsClient(Context context) {
        if(isSignedIn(context)) {
            if(mEventsClient == null) {
                mEventsClient = Games.getEventsClient(context, getLastGoogleAccount(context));
            }
            return mEventsClient;
        }
        return null;
    }

    public boolean isSignedIn(Context context) {
        return getLastGoogleAccount(context) != null;
    }

    private GoogleSignInClient getGoogleSignInClient(Context context) {
        if(mGoogleSignInClient == null) {
            GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                    .requestIdToken(context.getString(R.string.default_web_client_id))
                    .requestScopes(new Scope(Scopes.GAMES))
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(context, signInOptions);
        }
        return mGoogleSignInClient;
    }

    public void signOut(@Nullable OnCompleteListener<Void> onCompleteListener, Context context) {
        mGoogleSignInClient = getGoogleSignInClient(context);
        if(mGoogleSignInClient != null) {
            Task<Void> task = mGoogleSignInClient.signOut();
            mGoogleSignInClient.revokeAccess();
            if(onCompleteListener != null) {
                task.addOnCompleteListener(onCompleteListener);
            }
        }
        mAchievementsClient = null;
        mLeaderboardsClient = null;
        mEventsClient = null;
    }

}
