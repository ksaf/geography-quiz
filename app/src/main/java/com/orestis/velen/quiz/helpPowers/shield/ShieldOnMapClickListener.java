package com.orestis.velen.quiz.helpPowers.shield;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.orestis.velen.quiz.helpPowers.ChargeChangeListener;
import com.orestis.velen.quiz.pinpoint.MapTouchListener;
import com.orestis.velen.quiz.questions.QuestionHandler;

public class ShieldOnMapClickListener extends ShieldClickListener {

    private MapTouchListener mapTouchListener;

    public ShieldOnMapClickListener(QuestionHandler questionHandler, ImageView shieldImg, ImageView shieldBreakingImg,
                                    FrameLayout shieldGradient, TextView shieldTurnsLeftText, ShieldPowerConfig shieldPowerConfig, ChargeChangeListener chargeChangeListener,
                                    Context context, MapTouchListener mapTouchListener, ImageView helpPowerUsedImg, ImageView helpPowerUsedImgBg) {
        super(questionHandler, shieldImg, shieldBreakingImg, shieldGradient, shieldTurnsLeftText, shieldPowerConfig, chargeChangeListener, context, helpPowerUsedImg, helpPowerUsedImgBg);
        this.mapTouchListener = mapTouchListener;
    }

    @Override
    public void stopCurrentTurnsMistake() {
        mapTouchListener.enableShield(this);
    }

    @Override
    public void onExtraTryEnd() {
        mapTouchListener.disableShield();
        isShieldEnabled = false;
        animateBreakingShield();
        chargeChangeListener.onChargeDurationEnd();
    }
}
