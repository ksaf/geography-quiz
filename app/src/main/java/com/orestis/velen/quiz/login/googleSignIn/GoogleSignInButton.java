package com.orestis.velen.quiz.login.googleSignIn;

import android.view.View;
import android.widget.ImageView;

public class GoogleSignInButton {

    private ImageView googleSignInBtn;
    private View.OnClickListener onClickListener;

    public GoogleSignInButton(ImageView googleSignInBtn, View.OnClickListener onClickListener) {
        this.googleSignInBtn = googleSignInBtn;
        this.onClickListener = onClickListener;
    }

    public void enable() {
        googleSignInBtn.setVisibility(View.VISIBLE);
        googleSignInBtn.setOnClickListener(onClickListener);
    }

    public void disable() {
        googleSignInBtn.setVisibility(View.GONE);
        googleSignInBtn.setOnClickListener(null);
    }
}
