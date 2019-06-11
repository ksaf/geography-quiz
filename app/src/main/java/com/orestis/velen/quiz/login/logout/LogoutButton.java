package com.orestis.velen.quiz.login.logout;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.orestis.velen.quiz.login.SignedOutListener;
import com.orestis.velen.quiz.login.UserSession;
import com.orestis.velen.quiz.login.facebookSignIn.FacebookSession;
import com.orestis.velen.quiz.login.googleSignIn.GoogleSession;

import static android.view.View.VISIBLE;

public class LogoutButton {

    private Button logoutBtn;

    public LogoutButton(Button logoutBtn) {
        this.logoutBtn = logoutBtn;
    }

    public void enableLogout(final SignedOutListener signedOutListener, final Context context) {
        logoutBtn.setVisibility(VISIBLE);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(context, signedOutListener);
            }
        });
    }

    public void disableLogout() {
        logoutBtn.setVisibility(View.GONE);
        logoutBtn.setOnClickListener(null);
    }

    private void logout(Context context, SignedOutListener signedOutListener) {
        GoogleSession.getInstance().signOut(null, context);
        FacebookSession.getInstance().signOut();
        UserSession.getInstance().disconnectFromFirebase();
        signedOutListener.onSignedOut();
    }
}
