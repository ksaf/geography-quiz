package com.orestis.velen.quiz.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.login.localStorage.LocalStorageManager;

import java.util.HashSet;
import java.util.Set;

public class SoundPoolHelper extends SoundPool {
    private final Set<Integer> mLoaded;
    private final Context mContext;
    private final LocalStorageManager localStorageManager;

    private int menuBtnOpenSound;
    private int menuBtnCloseSound;
    private int inGameCorrectChoiceSound;
    private int inGameWrongChoiceSound;
    private int inGamePowerEnableSound;
    private int inGameMenuSelectSound;
    private int inGameGainXpSound;
    private int inGameFreezePowerSound;
    private int levelUpSound;
    private int shieldPowerEndSound;
    private float volume;

    public SoundPoolHelper(int maxStreams, Context context) {
        this(maxStreams, AudioManager.STREAM_MUSIC, 0, context);
    }

    public SoundPoolHelper(int maxStreams, int streamType, int srcQuality, Context context) {
        super(maxStreams, streamType, srcQuality);
        localStorageManager = new LocalStorageManager(context);
        volume = localStorageManager.getSoundVolumeSettings();
        mContext = context;
        mLoaded = new HashSet<Integer>();
        setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                mLoaded.add(sampleId);
            }
        });
    }

    private int play(int soundID, int loop, float volumeLoweringFactor) {
        AudioManager audioManager = (AudioManager) mContext.getSystemService( Context.AUDIO_SERVICE);
        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;
        volume *= volumeLoweringFactor;
        // Is the sound loaded already?
        if (mLoaded.contains(soundID)) {
            return play(soundID, volume, volume, 1, loop, 1f);
        } else {
            return -100;
        }
    }

    private void play(int soundID, float volumeLoweringFactor) {
        if(localStorageManager.getSoundSettings()) {
            play(soundID, 0, volumeLoweringFactor);
        }
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void loadMainMenuSounds() {
        menuBtnOpenSound = load(mContext, R.raw.menu_btn_a, 1);
        menuBtnCloseSound = load(mContext, R.raw.menu_btn_b, 1);
    }

    public void loadInGameSounds() {
        inGameCorrectChoiceSound = load(mContext, R.raw.choice_good, 1);
        inGameWrongChoiceSound = load(mContext, R.raw.menu_btn_b, 1);
        inGamePowerEnableSound = load(mContext, R.raw.power_enable, 1);
        inGameMenuSelectSound = load(mContext, R.raw.menu_btn_a, 1);
        inGameGainXpSound = load(mContext, R.raw.gain_xp_sound, 1);
        inGameFreezePowerSound = load(mContext, R.raw.freeze_power_sound, 1);
        levelUpSound = load(mContext, R.raw.level_up, 1);
        shieldPowerEndSound = load(mContext, R.raw.shield_power_end, 1);
    }

    //MAIN MENU

    public void playMenuBtnOpenSound() {
        play(menuBtnOpenSound, 0.2f * volume);
    }

    public void playMenuBtnCloseSound() {
        play(menuBtnCloseSound, 0.2f * volume);
    }


    //IN GAME

    public void playInGameCorrectChoiceSound() {
        play(inGameCorrectChoiceSound, 0.2f * volume);
    }

    public void playInGameWrongChoiceSound() {
        play(inGameWrongChoiceSound, 0.2f * volume);
    }

    public void playInGamePowerEnableSound() {
        play(inGamePowerEnableSound, 0.2f * volume);
    }

    public void playInGameMenuSelectSound() {
        play(inGameMenuSelectSound, 0.2f * volume);
    }

    public void playInGameGainXpSound() {
        play(inGameGainXpSound, 0.2f * volume);
    }

    public void playInGameFreezePowerSound() {
        play(inGameFreezePowerSound, 0.05f * volume);
    }

    public void playLevelUpSound() {
        play(levelUpSound, 0.2f * volume);
    }

    public void playShieldPowerEndSound() {
        play(shieldPowerEndSound, 0.9f * volume);
    }
}
