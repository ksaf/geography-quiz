package com.orestis.velen.quiz.leaderboard;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.login.SignInRequestHandler;
import com.orestis.velen.quiz.login.errors.ConnectionError;
import com.orestis.velen.quiz.login.googleSignIn.GoogleSession;

public class GoogleLeaderboard {

    public static final int RC_LEADERBOARD = 5002;
    //private final AccomplishmentsOutbox mOutbox = new AccomplishmentsOutbox();

    public void updateLeaderboard(Context context, int score) {
        LeaderboardsClient mLeaderboardsClient = GoogleSession.getInstance().getLeaderboardsClient(context);
        if(mLeaderboardsClient != null) {
            mLeaderboardsClient.submitScore(context.getString(R.string.leaderboard_top_scorers), score);
        }
    }

    public void showLeaderboard(final AppCompatActivity activity, final SignInRequestHandler signInRequestHandler, final int errorPlaceholderId) {
        LeaderboardsClient mLeaderboardsClient = GoogleSession.getInstance().getLeaderboardsClient(activity);
        if(mLeaderboardsClient == null || !GoogleSession.getInstance().isSignedIn(activity)) {
            signInRequestHandler.onGoogleSignInRequested();
            return;
        }
        mLeaderboardsClient.getAllLeaderboardsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        activity.startActivityForResult(intent, RC_LEADERBOARD);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ConnectionError connectionError = new ConnectionError(activity);
                        connectionError.show(3000, errorPlaceholderId, "Could not open leaderboard, check your connection and try again!");
                    }
                });
    }
}
