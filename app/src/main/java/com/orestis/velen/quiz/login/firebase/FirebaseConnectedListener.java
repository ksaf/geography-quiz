package com.orestis.velen.quiz.login.firebase;

import com.google.firebase.auth.FirebaseUser;

public interface FirebaseConnectedListener {
    void onFirebaseConnected(FirebaseUser firebaseUser);
    void onFirebaseError();
}
