package com.orestis.velen.quiz.adverts;

import com.google.android.gms.ads.AdListener;

import java.util.Random;

public class FinishGameWithAdHandler {

    private static final int AD_DISPLAY_PERCENTAGE = 35;

    public static void finishGame(final ExitGameListener exitGameListener) {
        Random rand = new Random();
        int n = rand.nextInt(100) + 1;
        if(n < AD_DISPLAY_PERCENTAGE + 1) {
            FullscreenAdManager.getInstance().setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    exitGameListener.onExitGame();
                }
            });
            FullscreenAdManager.getInstance().showAd();
            if(!FullscreenAdManager.getInstance().isAdAvailable()) {
                exitGameListener.onExitGame();
            }
        } else {
            exitGameListener.onExitGame();
        }
    }

    public interface ExitGameListener {
        void onExitGame();
    }
}
