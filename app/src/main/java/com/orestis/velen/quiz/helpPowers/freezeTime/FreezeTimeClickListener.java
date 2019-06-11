package com.orestis.velen.quiz.helpPowers.freezeTime;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.orestis.velen.quiz.helpPowers.ChargeChangeListener;
import com.orestis.velen.quiz.loadingBar.LoadingBarHandler;

public class FreezeTimeClickListener implements View.OnClickListener{

    private TextView freezeTimeTxt;
    private ImageView hourglassImg;
    private LoadingBarHandler loadingBarHandler;
    private Context context;
    private int freezeDuration;
    private ChargeChangeListener chargeChangeListener;

    public FreezeTimeClickListener(TextView freezeTimeTxt, ImageView hourglassImg,
                                   LoadingBarHandler loadingBarHandler, Context context, int freezeDuration, ChargeChangeListener chargeChangeListener) {
        this.freezeTimeTxt = freezeTimeTxt;
        this.hourglassImg = hourglassImg;
        this.loadingBarHandler = loadingBarHandler;
        this.context = context;
        this.freezeDuration = freezeDuration;
        this.chargeChangeListener = chargeChangeListener;
    }

    @Override
    public void onClick(View view) {
        view.setEnabled(false);
        chargeChangeListener.onChargeDecreased();
        FreezeTimeDisplayHandler freezeTimeDisplayHandler = new FreezeTimeDisplayHandler(freezeTimeTxt, hourglassImg, freezeDuration, loadingBarHandler, context, chargeChangeListener);
        loadingBarHandler.freezeLoadingBar(context);
        freezeTimeDisplayHandler.displayFreezeTime();
    }
}
