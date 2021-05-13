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
import android.widget.SeekBar;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.credits.CreditsFragment;
import com.orestis.velen.quiz.language.LanguageFragment;
import com.orestis.velen.quiz.login.UserSession;
import com.orestis.velen.quiz.login.facebookSignIn.FacebookSession;
import com.orestis.velen.quiz.login.googleSignIn.GoogleSession;
import com.orestis.velen.quiz.login.localStorage.LocalStorageManager;
import com.orestis.velen.quiz.mainMenu.PlayerRecoveredListener;
import com.orestis.velen.quiz.mainMenu.SocialSignInUIHandler;
import com.orestis.velen.quiz.player.PlayerSession;
import com.orestis.velen.quiz.resetProgress.ResetGameProgressFragment;
import com.orestis.velen.quiz.sound.MainMenuBackgroundMusicPlayer;
import com.orestis.velen.quiz.sound.SoundPoolHelper;
import com.orestis.velen.quiz.termsAndConditions.TermsAndConditionsFragment;

public class SettingsFragment extends Fragment {

    private final static boolean ENABLED = true;
    private final static boolean DISABLED = false;
    private ImageView darkBg;
    private SocialSignInUIHandler socialSignInUIHandler;
    private PlayerRecoveredListener playerRecoveredListener;
    private SoundPoolHelper soundHelper;
    private MainMenuBackgroundMusicPlayer musicPlayer;
    private ImageView soundVolumeIcon;
    private ImageView musicVolumeIcon;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        darkBg.setVisibility(View.VISIBLE);

        //Close
        Button closeBtn = view.findViewById(R.id.close_skill_upgrade_menu);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });

        //Logout
        Button logOutBtn = view.findViewById(R.id.logOutBtn);
        if(PlayerSession.getInstance().isConnected()) {
            logOutBtn.setVisibility(View.VISIBLE);
            logOutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    disconnect();
                    socialSignInUIHandler.setSignedOutUI();
                    closeFragment();
                }
            });
        } else {
            logOutBtn.setVisibility(View.GONE);
            logOutBtn.setOnClickListener(null);
        }

        //Reset progress
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

        //Music
        musicVolumeIcon = view.findViewById(R.id.musicVolumeIcon);
        final SeekBar musicVolumeBar = view.findViewById(R.id.musicVolumeBar);
        setupMusicSettings(localStorageManager, musicVolumeBar);

        //Sound
        soundVolumeIcon = view.findViewById(R.id.soundVolumeIcon);
        final SeekBar soundVolumeBar = view.findViewById(R.id.soundVolumeBar);
        setupSoundSettings(localStorageManager, soundVolumeBar);

        //Language
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

        //Credits
        Button creditsBtn = view.findViewById(R.id.creditsBtn);
        creditsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundHelper.playMenuBtnOpenSound();
                CreditsFragment creditsFragment = new CreditsFragment.Builder()
                        .withDarkBg(darkBg)
                        .withSoundPoolHelper(soundHelper)
                        .withFallBackFragment(SettingsFragment.this)
                        .build();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.optionScreenPlaceholder, creditsFragment, "CreditsTag");
                ft.addToBackStack("settings");
                ft.commit();
            }
        });

        //Terms and Conditions
        Button termsAndConditionsBtn = view.findViewById(R.id.termsAndConditionsBtn);
        termsAndConditionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundHelper.playMenuBtnOpenSound();
                TermsAndConditionsFragment termsAndConditionsFragment = new TermsAndConditionsFragment.Builder()
                        .withDarkBg(darkBg)
                        .withSoundPoolHelper(soundHelper)
                        .withFallBackFragment(SettingsFragment.this)
                        .build();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.optionScreenPlaceholder, termsAndConditionsFragment, "TermsAndConditionsTag");
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

    private void setupMusicSettings(final LocalStorageManager localStorageManager, final SeekBar musicVolumeBar) {
        boolean musicSettingOn = localStorageManager.getMusicSettings();
        setMusicIcon((int) (localStorageManager.getMusicVolumeSettings() * 100), musicSettingOn);
        musicVolumeBar.setMax(100);
        if(musicSettingOn) {
            musicVolumeBar.setProgress((int) (localStorageManager.getMusicVolumeSettings() * 100));
        } else {
            musicVolumeBar.setProgress(0);
        }

        musicVolumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUse) {
                setMusicIcon(progress, progress > 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float volume = seekBar.getProgress() / 100f;
                localStorageManager.setMusicVolumeSetting(volume);
                localStorageManager.setMusicSetting(volume > 0);
                musicPlayer.setVolume(volume);
                if(volume == 0f) {
                    musicPlayer.pause();
                } else {
                    musicPlayer.start();
                }
            }
        });

        musicVolumeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean musicStateBeforeClick = localStorageManager.getMusicSettings();
                boolean musicStateAfterClick = !musicStateBeforeClick;
                int currentMusicVolume = (int) (localStorageManager.getMusicVolumeSettings() * 100);

                localStorageManager.setMusicSetting(musicStateAfterClick);
                setMusicIcon(currentMusicVolume, musicStateAfterClick);

                if(localStorageManager.getSoundSettings() == ENABLED) {
                    soundHelper.playMenuBtnOpenSound();
                }

                //remove progress if silencing, add progress if we are enabling music
                if(musicStateAfterClick == DISABLED) {
                    musicVolumeBar.setProgress(0);
                } else {
                    musicVolumeBar.setProgress(currentMusicVolume);
                }

                if(musicStateAfterClick == ENABLED) {
                    musicPlayer.start();
                } else {
                    musicPlayer.pause();
                }
            }
        });
    }

    private void setupSoundSettings(final LocalStorageManager localStorageManager, final SeekBar soundVolumeBar) {
        boolean soundSettingOn = localStorageManager.getSoundSettings();
        setSoundIcon((int) (localStorageManager.getSoundVolumeSettings() * 100), soundSettingOn);
        soundVolumeBar.setMax(100);
        if(soundSettingOn) {
            soundVolumeBar.setProgress((int) (localStorageManager.getSoundVolumeSettings() * 100));
        } else {
            soundVolumeBar.setProgress(0);
        }

        soundVolumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUse) {
                setSoundIcon(progress, progress > 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                float volume = seekBar.getProgress() / 100f;
                localStorageManager.setSoundVolumeSetting(volume);
                localStorageManager.setSoundSetting(volume > 0);
                soundHelper.setVolume(volume);
                soundHelper.playMenuBtnOpenSound();
            }
        });

        soundVolumeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean soundStateBeforeClick = localStorageManager.getSoundSettings();
                boolean soundStateAfterClick = !soundStateBeforeClick;
                int currentSoundVolume = (int) (localStorageManager.getSoundVolumeSettings() * 100);

                localStorageManager.setSoundSetting(soundStateAfterClick);
                setSoundIcon(currentSoundVolume, soundStateAfterClick);

                //play sound if we are enabling sounds, don't play if we are silencing
                //play this only after sound HAS been enabled or it won't be heard
                if(soundStateAfterClick == ENABLED) {
                    soundHelper.playMenuBtnOpenSound();
                }

                //remove progress if silencing, add progress if we are enabling sound
                if(soundStateAfterClick == DISABLED) {
                    soundVolumeBar.setProgress(0);
                } else {
                    soundVolumeBar.setProgress(currentSoundVolume);
                }
            }
        });
    }

    private void setMusicIcon(int volume, boolean isMusicOn) {
        if(volume > 0 && isMusicOn) {
            musicVolumeIcon.setImageResource(R.drawable.ic_music_volume_on);
        } else {
            musicVolumeIcon.setImageResource(R.drawable.ic_music_volume_off);
        }
    }

    private void setSoundIcon(int volume, boolean isSoundOn) {
        if(volume > 50 && isSoundOn) {
            soundVolumeIcon.setImageResource(R.drawable.ic_sound_high_volume);
        } else if(volume > 0 && isSoundOn) {
            soundVolumeIcon.setImageResource(R.drawable.ic_sound_low_volume);
        } else {
            soundVolumeIcon.setImageResource(R.drawable.ic_sound_volume_off);
        }
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
        private MainMenuBackgroundMusicPlayer musicPlayer;

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

        public Builder withMainMenuBackgroundMusicPlayer(MainMenuBackgroundMusicPlayer musicPlayer) {
            this.musicPlayer = musicPlayer;
            return this;
        }

        public SettingsFragment build() {
            SettingsFragment settingsFragment = new SettingsFragment();
            settingsFragment.darkBg = darkBg;
            settingsFragment.socialSignInUIHandler = socialSignInUIHandler;
            settingsFragment.playerRecoveredListener = playerRecoveredListener;
            settingsFragment.soundHelper = soundHelper;
            settingsFragment.musicPlayer = musicPlayer;
            return settingsFragment;
        }
    }

}
