package com.orestis.velen.quiz.helpPowers.shield;

import android.content.Context;
import android.view.View;

import com.orestis.velen.quiz.helpPowers.buttonScaleDownAnimation.ScaleDownAnimationHandler;
import com.orestis.velen.quiz.helpPowers.buttonScaleDownAnimation.ViewScaledDownListener;

public class WrongChoiceClickListener implements View.OnClickListener, ViewScaledDownListener {

    private ShieldEndListener shieldEndListener;
    private Context context;
    private ShieldBreakingAnimator shieldBreakingAnimator;

    public WrongChoiceClickListener(ShieldEndListener shieldEndListener, Context context, ShieldBreakingAnimator shieldBreakingAnimator) {
        this.shieldEndListener = shieldEndListener;
        this.context = context;
        this.shieldBreakingAnimator = shieldBreakingAnimator;
    }

    @Override
    public void onClick(View view) {
        shieldBreakingAnimator.animateBreakingShield();
        view.setVisibility(View.INVISIBLE);
        ScaleDownAnimationHandler animationHandler = new ScaleDownAnimationHandler(context);
        animationHandler.hide(view, this);
    }

    @Override
    public void onViewScaledDown() {
        shieldEndListener.onExtraTryEnd();
    }
}
