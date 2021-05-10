package com.orestis.velen.quiz.adverts;

import android.app.Activity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class VideoAdManager implements RewardedVideoAdListener {

    private static VideoAdManager instance;
    private static final String ADMOB_APP_ID = "ca-app-pub-8012215063928736~3520575240";
    private static final String AD_UNIT_ID = "ca-app-pub-8012215063928736/8222237587";
    private static final String TEST_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917";
//    private static final String TEST_DEVICE = "32D4B2F5BCFA3DC92225D54235591025";
    private static final String TEST_DEVICE = "77EC96E07440E50E28B31F916BCEDAF6";
    private RewardedVideoAd mRewardedVideoAd;
    private RewardHandler rewardHandler;

    public static VideoAdManager getInstance() {
        if(instance == null) {
            instance = new VideoAdManager();
        }
        return instance;
    }

    public void initialise(Activity activity) {
        MobileAds.initialize(activity, ADMOB_APP_ID);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();
    }

    public void setRewardHandler(RewardHandler rewardHandler) {
        this.rewardHandler = rewardHandler;
    }

    public void showAd() {
        if(mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(TEST_AD_UNIT_ID,
                new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .addTestDevice(TEST_DEVICE).build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        rewardHandler.onReward(rewardItem.getAmount());
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}
