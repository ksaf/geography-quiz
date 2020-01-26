package com.orestis.velen.quiz.helpPowers.skip;

import android.view.View;

import com.orestis.velen.quiz.helpPowers.ChargeChangeListener;

public class SkipClickListener implements View.OnClickListener {

    private ChargeChangeListener chargeChangeListener;

    public SkipClickListener(ChargeChangeListener chargeChangeListener) {
        this.chargeChangeListener = chargeChangeListener;
    }

    @Override
    public void onClick(View view) {
        chargeChangeListener.onChargeDecreased();
    }
}
