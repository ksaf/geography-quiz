package com.orestis.velen.quiz.helpPowers.shield;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class PowerImageTask extends AsyncTask {

    private WeakReference<ImageView> helpPowerUsedImg;
    private WeakReference<ImageView> helpPowerUsedImgBg;
    private int displayDuration;
    private int helpPowerUsedResource;
    private int color;

    public PowerImageTask(int color, ImageView helpPowerUsedImg, ImageView helpPowerUsedImgBg, int helpPowerUsedResource, int displayDuration) {
        this.helpPowerUsedImg = new WeakReference<>(helpPowerUsedImg);
        this.helpPowerUsedImgBg = new WeakReference<>(helpPowerUsedImgBg);
        this.displayDuration = displayDuration;
        this.helpPowerUsedResource = helpPowerUsedResource;
        this.color = color;
    }

    @Override
    protected void onPreExecute() {
        helpPowerUsedImgBg.get().setImageResource(color);
        helpPowerUsedImg.get().setImageResource(helpPowerUsedResource);
        helpPowerUsedImgBg.get().setVisibility(View.VISIBLE);
        helpPowerUsedImg.get().setVisibility(View.VISIBLE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        SystemClock.sleep(displayDuration);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        helpPowerUsedImg.get().setVisibility(View.GONE);
        helpPowerUsedImgBg.get().setVisibility(View.GONE);
    }
}
