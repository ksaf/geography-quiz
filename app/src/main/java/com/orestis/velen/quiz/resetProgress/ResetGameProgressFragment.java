package com.orestis.velen.quiz.resetProgress;

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
import com.orestis.velen.quiz.login.UserSession;
import com.orestis.velen.quiz.mainMenu.PlayerRecoveredListener;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

public class ResetGameProgressFragment extends Fragment {

    private ImageView darkBg;
    private SoundPoolHelper soundHelper;
    private Fragment fallBackFragment;
    private PlayerRecoveredListener playerRecoveredListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reset_progress_fragment, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        Button closeBtn = view.findViewById(R.id.close_menu);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment(true);
            }
        });

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        Button acceptResetBtn = view.findViewById(R.id.acceptResetBtn);
        acceptResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetProgress();
            }
        });
    }

    private void closeFragment(boolean withSound) {
        if(withSound)
            soundHelper.playMenuBtnCloseSound();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(ResetGameProgressFragment.this).addToBackStack(null).commit();
        darkBg.setVisibility(View.GONE);
    }

    private void back() {
        soundHelper.playMenuBtnCloseSound();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.optionScreenPlaceholder, fallBackFragment).commit();
    }

    private void resetProgress() {
        soundHelper.playMenuBtnOpenSound();
        UserSession.getInstance().resetCurrentPlayer();
        UserSession.getInstance().recoverPlayerFromLocalStorage(playerRecoveredListener, getContext());
        closeFragment(false);
    }

    public static class Builder {
        private ImageView darkBg;
        private SoundPoolHelper soundHelper;
        private Fragment fallBackFragment;
        private PlayerRecoveredListener playerRecoveredListener;

        public Builder withPlayerRecoveredListener(PlayerRecoveredListener playerRecoveredListener) {
            this.playerRecoveredListener = playerRecoveredListener;
            return this;
        }

        public Builder withDarkBg(ImageView darkBg) {
            this.darkBg = darkBg;
            return this;
        }

        public Builder withSoundPoolHelper(SoundPoolHelper soundHelper) {
            this.soundHelper = soundHelper;
            return this;
        }

        public Builder withFallBackFragment(Fragment fallBackFragment) {
            this.fallBackFragment = fallBackFragment;
            return this;
        }

        public ResetGameProgressFragment build() {
            ResetGameProgressFragment resetGameProgressFragment = new ResetGameProgressFragment();
            resetGameProgressFragment.darkBg = darkBg;
            resetGameProgressFragment.soundHelper = soundHelper;
            resetGameProgressFragment.fallBackFragment = fallBackFragment;
            resetGameProgressFragment.playerRecoveredListener = playerRecoveredListener;
            return resetGameProgressFragment;
        }
    }
}
