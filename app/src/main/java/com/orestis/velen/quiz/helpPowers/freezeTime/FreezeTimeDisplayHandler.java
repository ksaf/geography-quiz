package com.orestis.velen.quiz.helpPowers.freezeTime;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.orestis.velen.quiz.helpPowers.ChargeChangeListener;
import com.orestis.velen.quiz.loadingBar.LoadingBarHandler;

public class FreezeTimeDisplayHandler implements FreezeTimeEndListener{

    private TextView freezeTimeTxt;
    private ImageView hourglassImg;
    private int freezeTimeDuration;
    private LoadingBarHandler loadingBarHandler;
    private Context context;
    private ChargeChangeListener chargeChangeListener;

    public FreezeTimeDisplayHandler(TextView freezeTimeTxt, ImageView hourglassImg, int freezeTimeDuration, LoadingBarHandler loadingBarHandler, Context context, ChargeChangeListener chargeChangeListener) {
        this.freezeTimeTxt = freezeTimeTxt;
        this.hourglassImg = hourglassImg;
        this.freezeTimeDuration = freezeTimeDuration;
        this.loadingBarHandler = loadingBarHandler;
        this.context = context;
        this.chargeChangeListener = chargeChangeListener;
    }

    public void displayFreezeTime() {
        DisplayFreezeTimeTask task = new DisplayFreezeTimeTask(freezeTimeTxt, hourglassImg, freezeTimeDuration, this);
        task.start();
    }

    @Override
    public void onFreezeTimeEnd() {
        loadingBarHandler.unFreezeLoadingBar(context);
        chargeChangeListener.onChargeDurationEnd();
    }
}
