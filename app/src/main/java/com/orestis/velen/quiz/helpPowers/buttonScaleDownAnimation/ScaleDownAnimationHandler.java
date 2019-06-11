package com.orestis.velen.quiz.helpPowers.buttonScaleDownAnimation;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.orestis.velen.quiz.R;

public class ScaleDownAnimationHandler {

    private Animation animation;

    public ScaleDownAnimationHandler(Context context) {
        animation = AnimationUtils.loadAnimation(context, R.anim.scale_down_animation);
    }

    public void hide(View animatedView, ViewScaledDownListener viewScaledDownListener) {
        animation.setAnimationListener(new ScaleDownAnimationListener(animatedView, viewScaledDownListener));
        animatedView.startAnimation(animation);
    }

}
