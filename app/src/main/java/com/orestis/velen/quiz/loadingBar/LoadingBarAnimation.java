package com.orestis.velen.quiz.loadingBar;

import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

public class LoadingBarAnimation extends Animation {

    private ProgressBar progressBar;
    private float from;
    private float  to;

    public LoadingBarAnimation(ProgressBar progressBar, float from, float to, final LoadingBarAnimationListener loadingBarAnimationListener) {
        super();
        this.progressBar = progressBar;
        this.from = from;
        this.to = to;
        setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                loadingBarAnimationListener.onAnimationEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        setInterpolator(new DecelerateInterpolator());
    }

    public void changeInterpolator(Interpolator interpolator) {
        setInterpolator(interpolator);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int) value);
    }

}
