package com.orestis.velen.quiz.mainMenu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.player.PlayerSession;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

public class SocialSignInUIHandler {

    private AppCompatActivity activity;
    private SignInRequestHandler signInRequestHandler;
    private SoundPoolHelper soundHelper;
    private static final String SIGNED_IN_FRAGMENT_TAG = "signedInFragment";
    private static final String SIGNED_OUT_FRAGMENT_TAG = "signedOutFragment";

    public SocialSignInUIHandler(AppCompatActivity activity, SignInRequestHandler signInRequestHandler, SoundPoolHelper soundHelper) {
        this.activity = activity;
        this.signInRequestHandler = signInRequestHandler;
        this.soundHelper = soundHelper;
    }

    public void setSignedInUI() {
        SignedInFragment signedInFragment = new SignedInFragment.Builder()
                .withPlayerName(PlayerSession.getInstance().getCurrentPlayerName())
                .build();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        Fragment signedOutFragment = activity.getSupportFragmentManager().findFragmentByTag(SIGNED_OUT_FRAGMENT_TAG);
        if(signedOutFragment != null) {
            ft.remove(signedOutFragment);
        }
        ft.replace(R.id.signedInFrameContainer, signedInFragment, SIGNED_IN_FRAGMENT_TAG);
        ft.commit();
    }

    public void setSignedOutUI() {
        SignedOutFragment signedOutFragment = new SignedOutFragment.Builder()
                .withSignInRequestHandler(signInRequestHandler)
                .withSoundPoolHelper(soundHelper)
                .build();
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        Fragment signedInFragment = activity.getSupportFragmentManager().findFragmentByTag(SIGNED_IN_FRAGMENT_TAG);
        if(signedInFragment != null) {
            ft.remove(signedInFragment);
        }
        ft.replace(R.id.signedOutFrameContainer, signedOutFragment, SIGNED_OUT_FRAGMENT_TAG);
        ft.commit();
    }
}
