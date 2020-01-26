package com.orestis.velen.quiz.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.language.LanguageFragment;
import com.orestis.velen.quiz.login.UserSession;
import com.orestis.velen.quiz.login.facebookSignIn.FacebookSession;
import com.orestis.velen.quiz.login.googleSignIn.GoogleSession;
import com.orestis.velen.quiz.login.localStorage.LocalStorageManager;
import com.orestis.velen.quiz.mainMenu.PlayerRecoveredListener;
import com.orestis.velen.quiz.mainMenu.SocialSignInUIHandler;
import com.orestis.velen.quiz.player.PlayerSession;
import com.orestis.velen.quiz.resetProgress.ResetGameProgressFragment;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

public class SettingsFragment extends Fragment {

    private ImageView darkBg;
    private SocialSignInUIHandler socialSignInUIHandler;
    private PlayerRecoveredListener playerRecoveredListener;
    private SoundPoolHelper soundHelper;
    private boolean musicSettingOn;
    private boolean soundSettingOn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        darkBg.setVisibility(View.VISIBLE);
        Button closeBtn = view.findViewById(R.id.close_skill_upgrade_menu);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });
        Button logOutBtn = view.findViewById(R.id.logOutBtn);
        if(PlayerSession.getInstance().isConnected()) {
            logOutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    disconnect();
                    socialSignInUIHandler.setSignedOutUI();
                }
            });
        } else {
            logOutBtn.setOnClickListener(null);
        }

        Button resetProgressBtn = view.findViewById(R.id.resetProgressBtn);
        resetProgressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundHelper.playMenuBtnOpenSound();
                ResetGameProgressFragment resetGameProgressFragment = new ResetGameProgressFragment.Builder()
                        .withDarkBg(darkBg)
                        .withSoundPoolHelper(soundHelper)
                        .withFallBackFragment(SettingsFragment.this)
                        .withPlayerRecoveredListener(playerRecoveredListener)
                        .build();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.optionScreenPlaceholder, resetGameProgressFragment, "ResetGameProgressTag");
                ft.addToBackStack("settings");
                ft.commit();
            }
        });

        final LocalStorageManager localStorageManager = new LocalStorageManager(getContext());
        musicSettingOn = localStorageManager.getMusicSettings();
        soundSettingOn = localStorageManager.getSoundSettings();

        final Button musicBtn = view.findViewById(R.id.musicBtn);
        String musicSettingText = musicSettingOn ? getString(R.string.musicOn) : getString(R.string.musicOff);
        musicBtn.setText(musicSettingText);
        musicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundHelper.playMenuBtnOpenSound();
                localStorageManager.setMusicSetting(!musicSettingOn);
                musicSettingOn = !musicSettingOn;
                String musicSettingText = musicSettingOn ? getString(R.string.musicOn) : getString(R.string.musicOff);
                musicBtn.setText(musicSettingText);
                if(musicSettingOn) {
                    if(soundHelper.hasMenuBackgroundMusicStarted()) {
                        soundHelper.resumeMenuBackgroundMusic();
                    } else {
                        soundHelper.playMenuBackgroundMusic();
                    }
                } else {
                    soundHelper.pauseMenuBackgroundMusic();
                }
            }
        });

        final Button soundBtn = view.findViewById(R.id.soundBtn);
        String soundSettingText = soundSettingOn ? getString(R.string.soundOn) : getString(R.string.soundOff);
        soundBtn.setText(soundSettingText);
        soundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundHelper.playMenuBtnOpenSound();
                localStorageManager.setSoundSetting(!soundSettingOn);
                soundSettingOn = !soundSettingOn;
                if(soundSettingOn) {
                    soundHelper.playMenuBtnOpenSound();
                }
                String soundSettingText = soundSettingOn ? getString(R.string.soundOn) : getString(R.string.soundOff);
                soundBtn.setText(soundSettingText);
            }
        });

        final Button languageBtn = view.findViewById(R.id.languageBtn);
        languageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundHelper.playMenuBtnOpenSound();
                LanguageFragment languageFragment = new LanguageFragment.Builder()
                        .withDarkBg(darkBg)
                        .withSoundPoolHelper(soundHelper)
                        .withFallBackFragment(SettingsFragment.this)
                        .build();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.optionScreenPlaceholder, languageFragment, "LanguageTag");
                ft.addToBackStack("settings");
                ft.commit();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        darkBg.setVisibility(View.GONE);
    }

    private void closeFragment() {
        soundHelper.playMenuBtnCloseSound();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(SettingsFragment.this).commit();
        darkBg.setVisibility(View.GONE);
    }

    private void disconnect() {
        GoogleSession.getInstance().signOut(null, getContext());
        FacebookSession.getInstance().signOut();
        UserSession.getInstance().disconnectFromFirebase();
        PlayerSession.getInstance().setConnected(false);
    }

    public static class Builder {
        private ImageView darkBg;
        private SocialSignInUIHandler socialSignInUIHandler;
        private PlayerRecoveredListener playerRecoveredListener;
        private SoundPoolHelper soundHelper;

        public Builder withPlayerRecoveredListener(PlayerRecoveredListener playerRecoveredListener) {
            this.playerRecoveredListener = playerRecoveredListener;
            return this;
        }

        public Builder withDarkBg(ImageView darkBg) {
            this.darkBg = darkBg;
            return this;
        }

        public Builder withSocialSignInUIHandler(SocialSignInUIHandler socialSignInUIHandler) {
            this.socialSignInUIHandler = socialSignInUIHandler;
            return this;
        }

        public Builder withSoundPoolHelper(SoundPoolHelper soundHelper) {
            this.soundHelper = soundHelper;
            return this;
        }

        public SettingsFragment build() {
            SettingsFragment settingsFragment = new SettingsFragment();
            settingsFragment.darkBg = darkBg;
            settingsFragment.socialSignInUIHandler = socialSignInUIHandler;
            settingsFragment.playerRecoveredListener = playerRecoveredListener;
            settingsFragment.soundHelper = soundHelper;
            return settingsFragment;
        }
    }

}
