package com.orestis.velen.quiz.adverts;

import android.app.Activity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class FullscreenAdManager {

    private static final String ADMOB_APP_ID = "ca-app-pub-8012215063928736~3520575240";
    private static final String AD_UNIT_ID = "ca-app-pub-8012215063928736/2858477681";
    private static final String TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712";
    private static final String TEST_DEVICE = "32D4B2F5BCFA3DC92225D54235591025";
    private InterstitialAd mInterstitialAd;
    private static FullscreenAdManager instance;

    private FullscreenAdManager(){}

    public static FullscreenAdManager getInstance() {
        if(instance == null) {
            instance = new FullscreenAdManager();
        }
        return instance;
    }

    public void initialise(Activity activity) {
        MobileAds.initialize(activity, ADMOB_APP_ID);
        mInterstitialAd = new InterstitialAd(activity);
        mInterstitialAd.setAdUnitId(TEST_AD_UNIT_ID);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public void setAdListener(AdListener adListener) {
        mInterstitialAd.setAdListener(adListener);
    }

    public boolean isAdAvailable() {
        return mInterstitialAd.isLoaded();
    }

    public void showAd() {
        if(mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

}
