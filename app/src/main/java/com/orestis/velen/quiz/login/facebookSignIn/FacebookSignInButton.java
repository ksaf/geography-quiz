package com.orestis.velen.quiz.login.facebookSignIn;

import android.view.View;
import android.widget.ImageView;

public class FacebookSignInButton {

    private ImageView facebookSignInBtn;
    private View.OnClickListener onClickListener;

    public FacebookSignInButton(ImageView facebookSignInBtn, View.OnClickListener onClickListener) {
        this.facebookSignInBtn = facebookSignInBtn;
        this.onClickListener = onClickListener;
    }

    public void enable() {
        facebookSignInBtn.setVisibility(View.VISIBLE);
        facebookSignInBtn.setOnClickListener(onClickListener);
    }

    public void disable() {
        facebookSignInBtn.setVisibility(View.GONE);
        facebookSignInBtn.setOnClickListener(null);
    }
}
