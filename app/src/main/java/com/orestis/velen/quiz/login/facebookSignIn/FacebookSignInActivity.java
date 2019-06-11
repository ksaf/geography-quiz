package com.orestis.velen.quiz.login.facebookSignIn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.FirebaseUser;
import com.orestis.velen.quiz.login.firebase.FirebaseConnectedListener;
import com.orestis.velen.quiz.login.firebase.FirebaseConnectionHelper;

import java.util.Arrays;

public class FacebookSignInActivity extends AppCompatActivity implements FirebaseConnectedListener {

    private CallbackManager facebookCallbackManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                firebaseAuthWithFacebook(accessToken);
            }
            @Override
            public void onCancel() {
                resumeCallingActivityWithCanceled();
            }
            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
                resumeCallingActivityWithCanceled();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn) {
            firebaseAuthWithFacebook(accessToken);
        } else {
            signIn();
        }
    }

    private void signIn() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
    }

    private void resumeCallingActivityWithSuccess(Intent intent) {
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(0, 0);
    }

    private void resumeCallingActivityWithCanceled() {
        setResult(RESULT_CANCELED, new Intent());
        finish();
        overridePendingTransition(0, 0);
    }

    private void firebaseAuthWithFacebook(AccessToken token) {
        FirebaseConnectionHelper connectionHelper = new FirebaseConnectionHelper();
        connectionHelper.firebaseAuthWithFacebook(this, token, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFirebaseConnected(FirebaseUser firebaseUser) {
        Intent output = new Intent();
        output.putExtra("firebaseUser", firebaseUser);
        resumeCallingActivityWithSuccess(output);
    }

    @Override
    public void onFirebaseError() {
        resumeCallingActivityWithCanceled();
    }
}
