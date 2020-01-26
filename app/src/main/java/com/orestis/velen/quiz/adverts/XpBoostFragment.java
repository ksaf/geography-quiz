package com.orestis.velen.quiz.adverts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.player.PlayerSession;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

public class XpBoostFragment extends Fragment implements RewardHandler {

    private ImageView darkBg;
    private SoundPoolHelper soundHelper;
    private VideoAdManager videoAdManager;
    private XpBoostEnabledListener xpBoostEnabledListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.xp_boost_fragment, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        darkBg.setVisibility(View.VISIBLE);
        videoAdManager.setRewardHandler(this);

        Button closeBtn = view.findViewById(R.id.close_menu);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });

        ImageButton watchVideoBtn = view.findViewById(R.id.watchVideoBtn);
        watchVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAdvert();
            }
        });
    }

    private void closeFragment() {
        soundHelper.playMenuBtnCloseSound();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(XpBoostFragment.this).commit();
        darkBg.setVisibility(View.GONE);
    }

    private void showAdvert() {
        videoAdManager.showAd();
    }

    @Override
    public void onReward(int rewardAmount) {
        replaceWithBonusActiveFragment();
        //save new time first
        PlayerSession.getInstance().getCurrentPlayer().saveXpBoostEnabledTime();
        //then call the listener
        xpBoostEnabledListener.onXpBoostEnabled();
    }

    private void replaceWithBonusActiveFragment() {
        XpBoostFragmentActive xpBoostFragmentActive = new XpBoostFragmentActive.Builder()
                .withDarkBg(darkBg)
                .withSoundPoolHelper(soundHelper)
                .build();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.optionScreenPlaceholder, xpBoostFragmentActive).commit();
    }

    public static class Builder {
        private ImageView darkBg;
        private SoundPoolHelper soundHelper;
        private VideoAdManager videoAdManager;
        private XpBoostEnabledListener xpBoostEnabledListener;

        public Builder withDarkBg(ImageView darkBg) {
            this.darkBg = darkBg;
            return this;
        }

        public Builder withVideoAdManager(VideoAdManager videoAdManager) {
            this.videoAdManager = videoAdManager;
            return this;
        }

        public Builder withSoundPoolHelper(SoundPoolHelper soundHelper) {
            this.soundHelper = soundHelper;
            return this;
        }

        public Builder withXpBoostEnabledListener(XpBoostEnabledListener xpBoostEnabledListener) {
            this.xpBoostEnabledListener = xpBoostEnabledListener;
            return this;
        }

        public XpBoostFragment build() {
            XpBoostFragment xpBoostFragment = new XpBoostFragment();
            xpBoostFragment.darkBg = darkBg;
            xpBoostFragment.soundHelper = soundHelper;
            xpBoostFragment.videoAdManager = videoAdManager;
            xpBoostFragment.xpBoostEnabledListener = xpBoostEnabledListener;
            return xpBoostFragment;
        }
    }
}
