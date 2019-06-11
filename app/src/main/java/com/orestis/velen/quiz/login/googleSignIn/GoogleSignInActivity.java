package com.orestis.velen.quiz.login.googleSignIn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.login.firebase.FirebaseConnectedListener;
import com.orestis.velen.quiz.login.firebase.FirebaseConnectionHelper;

public class GoogleSignInActivity extends AppCompatActivity implements FirebaseConnectedListener {

    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestScopes(new Scope(Scopes.GAMES))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, signInOptions);
        GoogleSession.getInstance().setGoogleSignInClient(mGoogleSignInClient);
    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account == null) {
            signIn();
        } else {
            firebaseAuthWithGoogle(account);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        FirebaseConnectionHelper connectionHelper = new FirebaseConnectionHelper();
        connectionHelper.firebaseAuthWithGoogle(this, acct, this);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            Toast.makeText(this, "Could not sign in with Google...", Toast.LENGTH_SHORT).show();
            resumeCallingActivityWithCanceled();
        }
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
