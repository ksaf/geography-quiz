package com.orestis.velen.quiz.login.firebase;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class FirebaseConnectionHelper {

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public void firebaseAuthWithFacebook(Activity activity, AccessToken token, final FirebaseConnectedListener connectedListener) {
        Log.d("FirebaseConnection", "firebaseAuthWithFacebookToken: " + token.getToken());
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuthWithCredential(activity, credential, connectedListener);
    }

    public void firebaseAuthWithGoogle(Activity activity, GoogleSignInAccount acct, final FirebaseConnectedListener connectedListener) {
        Log.d("FirebaseConnection", "firebaseAuthWithGoogle: " + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuthWithCredential(activity, credential, connectedListener);
    }

    private void firebaseAuthWithCredential(Activity activity, AuthCredential credential, final FirebaseConnectedListener connectedListener) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in userDBRef's information
                            Log.d("FirebaseConnection", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            connectedListener.onFirebaseConnected(user);
                        } else {
                            // If sign in fails, display a message to the userDBRef.
                            Log.w("FirebaseConnection", "signInWithCredential:failure", task.getException());
                            connectedListener.onFirebaseError();
                        }
                    }
                });
    }
}
