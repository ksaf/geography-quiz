package com.orestis.velen.quiz.achievements;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.orestis.velen.quiz.login.SignInRequestHandler;
import com.orestis.velen.quiz.login.errors.ConnectionError;
import com.orestis.velen.quiz.login.googleSignIn.GoogleSession;

public class GoogleAchievements {

    public static final int RC_ACHIEVEMENTS = 5001;

    public void unlock(Context context, int achievementId) {
        //e.x. achievementId = R.string.achievement_right
        AchievementsClient mAchievementsClient = GoogleSession.getInstance().getAchievementsClient(context);
        if(mAchievementsClient != null) {
            mAchievementsClient.unlock(context.getResources().getString(achievementId));
        }
    }

    public void showAchievements(final AppCompatActivity activity, final SignInRequestHandler signInRequestHandler, final int errorPlaceholderId) {
        AchievementsClient mAchievementsClient = GoogleSession.getInstance().getAchievementsClient(activity);
        if(mAchievementsClient == null || !GoogleSession.getInstance().isSignedIn(activity)) {
            signInRequestHandler.onGoogleSignInRequested();
            return;
        }
        mAchievementsClient.getAchievementsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        activity.startActivityForResult(intent, RC_ACHIEVEMENTS);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ConnectionError connectionError = new ConnectionError(activity);
                        connectionError.show(3000, errorPlaceholderId, "Could not open achievements, check your connection and try again!");
                    }
                });
    }
}
