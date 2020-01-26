package com.orestis.velen.quiz.mainMenu;

import android.view.View;
import android.widget.TextView;

import com.orestis.velen.quiz.player.PlayerSession;
import com.orestis.velen.quiz.login.googleSignIn.GoogleSignInButton;

public class SocialSignInUIHandler {

    private TextView profileName;
    private GoogleSignInButton googleSignInButton;

    public SocialSignInUIHandler(TextView profileName, GoogleSignInButton googleSignInButton) {
        this.profileName = profileName;
        this.googleSignInButton = googleSignInButton;
    }

    public void setSignedInUI() {
        profileName.setVisibility(View.VISIBLE);
        profileName.setText(PlayerSession.getInstance().getCurrentPlayerName());
        googleSignInButton.disable();
    }

    public void setSignedOutUI() {
        profileName.setVisibility(View.INVISIBLE);
        googleSignInButton.enable();
    }
}
