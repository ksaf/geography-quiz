package com.orestis.velen.quiz.login;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.orestis.velen.quiz.login.facebookSignIn.FacebookSession;
import com.orestis.velen.quiz.login.facebookSignIn.FacebookSignInButton;
import com.orestis.velen.quiz.login.googleSignIn.GoogleSession;
import com.orestis.velen.quiz.login.googleSignIn.GoogleSignInButton;
import com.orestis.velen.quiz.login.logout.LogoutButton;

public class SocialSignIn implements SignedOutListener, ImageManager.OnImageLoadedListener{

    private static final String GOOGLE = "google";
    private static final String FACEBOOK = "facebook";
    private LogoutButton logoutBtn;
    private GoogleSignInButton googleSignInButton;
    private FacebookSignInButton facebookSignInButton;
    private ImageView profileImg;
    private TextView profileName;
    private Context context;
    private PlayersClient mPlayersClient;

    private SocialSignIn() {}

    public void setGoogleSignedIn(GoogleSignInAccount googleAccount, FrameLayout ggsPlaceholder) {
        Games.getGamesClient(context, googleAccount).setViewForPopups(ggsPlaceholder);
        mPlayersClient = Games.getPlayersClient(context, googleAccount);
        mPlayersClient.getCurrentPlayer()
                .addOnCompleteListener(new OnCompleteListener<Player>() {
                    @Override
                    public void onComplete(@NonNull Task<Player> task) {
                        if (task.isSuccessful()) {
                            String personName = task.getResult().getDisplayName();
                            Uri personPhoto = task.getResult().getIconImageUri();
                            setSignedInUI(personName, personPhoto, GOOGLE);
                        } else {
                        }
                    }
                });
    }

    public void setFacebookSignedIn() {
        Profile profile = Profile.getCurrentProfile();
        String personName = profile.getFirstName();
        Uri personPhoto = profile.getProfilePictureUri(100, 100);
        setSignedInUI(personName, personPhoto, FACEBOOK);
    }

    public void setAlreadySignedIn(FrameLayout ggsPlaceholder) {
        GoogleSignInAccount lastGoogleAccount = GoogleSession.getInstance().getLastGoogleAccount(context);
        AccessToken lastAccessToken = FacebookSession.getInstance().getLastFacebookToken();
        if(lastGoogleAccount != null) {
            setGoogleSignedIn(lastGoogleAccount, ggsPlaceholder);
        } else if(lastAccessToken != null) {
            setFacebookSignedIn();
        }
    }

    private void setSignedInUI(String personName, Uri personPhoto, String provider) {
        profileName.setVisibility(View.VISIBLE);
        profileName.setText(personName);
        profileImg.setVisibility(View.VISIBLE);
        googleSignInButton.disable();
        facebookSignInButton.disable();
        logoutBtn.enableLogout(this, context);
        switch (provider) {
            case FACEBOOK:
                loadFacebookImage(personPhoto);
                break;
            case GOOGLE:
                loadGoogleImage(personPhoto);
                break;
        }
    }

    private void loadGoogleImage(Uri personPhoto){
        ImageManager mgr = ImageManager.create(context);
        mgr.loadImage(this, personPhoto);
    }

    private void loadFacebookImage(Uri personPhoto){
        Glide.with(context).load(personPhoto).circleCrop().into(profileImg);
    }

    public void setSignedOutUI() {
        profileName.setVisibility(View.GONE);
        profileName.setText("");
        profileImg.setVisibility(View.GONE);
        googleSignInButton.enable();
        facebookSignInButton.enable();
        logoutBtn.disableLogout();
    }

    @Override
    public void onSignedOut() {
        setSignedOutUI();
        mPlayersClient = null;
    }

    @Override
    public void onImageLoaded(Uri uri, Drawable drawable, boolean b) {
        Glide.with(context).load(drawable).circleCrop().into(profileImg);
    }


    public static class Builder {
        private FacebookSignInButton facebookSignInButton;
        private GoogleSignInButton googleSignInButton;
        private LogoutButton logoutBtn;
        private ImageView profileImg;
        private TextView profileName;
        private Context context;

        public Builder withProfileImg(ImageView profileImg) {
            this.profileImg = profileImg;
            return this;
        }

        public Builder withProfileName(TextView profileName) {
            this.profileName = profileName;
            return this;
        }

        public Builder withLogoutButton(LogoutButton logoutBtn) {
            this.logoutBtn = logoutBtn;
            return this;
        }

        public Builder withGoogleButton(GoogleSignInButton googleSignInButton) {
            this.googleSignInButton = googleSignInButton;
            return this;
        }

        public Builder withFacebookButton(FacebookSignInButton facebookSignInButton) {
            this.facebookSignInButton = facebookSignInButton;
            return this;
        }

        public Builder withContext(Context context) {
            this.context = context;
            return this;
        }

        public SocialSignIn build() {
            SocialSignIn socialSignIn = new SocialSignIn();
            socialSignIn.facebookSignInButton = this.facebookSignInButton;
            socialSignIn.googleSignInButton = this.googleSignInButton;
            socialSignIn.logoutBtn = this.logoutBtn;
            socialSignIn.profileImg = this.profileImg;
            socialSignIn.profileName = this.profileName;
            socialSignIn.context = this.context;

            return socialSignIn;
        }
    }
}
