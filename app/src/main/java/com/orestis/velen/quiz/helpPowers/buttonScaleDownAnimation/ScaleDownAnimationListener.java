package com.orestis.velen.quiz.helpPowers.buttonScaleDownAnimation;

import android.view.View;
import android.view.animation.Animation;

public class ScaleDownAnimationListener implements Animation.AnimationListener{

    private View animatedView;
    private ViewScaledDownListener viewScaledDownListener;

    public ScaleDownAnimationListener(View animatedView, ViewScaledDownListener viewScaledDownListener) {
        this.animatedView = animatedView;
        this.viewScaledDownListener = viewScaledDownListener;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        animatedView.setVisibility(View.INVISIBLE);
        animatedView.setScaleX(1);
        animatedView.setScaleY(1);
        viewScaledDownListener.onViewScaledDown();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
