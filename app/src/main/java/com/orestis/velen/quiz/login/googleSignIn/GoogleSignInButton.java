package com.orestis.velen.quiz.login.googleSignIn;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.orestis.velen.quiz.R;

public class GoogleSignInButton {

    private ImageView googleSignInBtn;
    private View.OnClickListener onClickListener;
    private Context context;

    public GoogleSignInButton(ImageView googleSignInBtn, View.OnClickListener onClickListener, Context context) {
        this.googleSignInBtn = googleSignInBtn;
        this.onClickListener = onClickListener;
        this.context = context;
    }

    public void enable() {
        Glide.with(context).load(R.drawable.google_games_icon).circleCrop().into(googleSignInBtn);
        googleSignInBtn.setVisibility(View.VISIBLE);
        googleSignInBtn.setOnClickListener(onClickListener);
    }

    public void disable() {
        googleSignInBtn.setVisibility(View.GONE);
        googleSignInBtn.setOnClickListener(null);
    }
}
