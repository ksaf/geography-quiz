package com.orestis.velen.quiz.helpPowers.shield;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class BreakingShieldTask extends AsyncTask{

    private WeakReference<ImageView> shieldImage;
    private WeakReference<ImageView> shieldBreakingImg;
    private WeakReference<TextView> shieldTurnsLeftText;
    private WeakReference<FrameLayout> shieldGradient;
    private int displayDuration;

    public BreakingShieldTask(ImageView shieldImage, TextView shieldTurnsLeftText, ImageView shieldBreakingImg, FrameLayout shieldGradient, int displayDuration) {
        this.shieldImage = new WeakReference<>(shieldImage);
        this.shieldBreakingImg = new WeakReference<>(shieldBreakingImg);
        this.shieldTurnsLeftText = new WeakReference<>(shieldTurnsLeftText);
        this.displayDuration = displayDuration;
        this.shieldGradient = new WeakReference<>(shieldGradient);
    }

    @Override
    protected void onPreExecute() {
        shieldImage.get().setVisibility(View.GONE);
        shieldTurnsLeftText.get().setVisibility(View.GONE);
        shieldBreakingImg.get().setVisibility(View.VISIBLE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        SystemClock.sleep(displayDuration);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        shieldGradient.get().setVisibility(View.INVISIBLE);
        shieldImage.get().setVisibility(View.GONE);
        shieldTurnsLeftText.get().setVisibility(View.GONE);
        shieldBreakingImg.get().setVisibility(View.GONE);
    }
}
