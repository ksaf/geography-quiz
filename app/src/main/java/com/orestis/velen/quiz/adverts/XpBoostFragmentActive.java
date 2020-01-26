package com.orestis.velen.quiz.adverts;

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
import com.orestis.velen.quiz.sound.SoundPoolHelper;

public class XpBoostFragmentActive extends Fragment {

    private ImageView darkBg;
    private SoundPoolHelper soundHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.xp_boost_active_fragment, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        darkBg.setVisibility(View.VISIBLE);
        Button closeBtn = view.findViewById(R.id.close_menu);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });
    }

    private void closeFragment() {
        soundHelper.playMenuBtnCloseSound();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(XpBoostFragmentActive.this).commit();
        darkBg.setVisibility(View.GONE);
    }

    public static class Builder {
        private ImageView darkBg;
        private SoundPoolHelper soundHelper;

        public Builder withDarkBg(ImageView darkBg) {
            this.darkBg = darkBg;
            return this;
        }

        public Builder withSoundPoolHelper(SoundPoolHelper soundHelper) {
            this.soundHelper = soundHelper;
            return this;
        }

        public XpBoostFragmentActive build() {
            XpBoostFragmentActive xpBoostFragmentActive = new XpBoostFragmentActive();
            xpBoostFragmentActive.darkBg = darkBg;
            xpBoostFragmentActive.soundHelper = soundHelper;
            return xpBoostFragmentActive;
        }
    }
}
