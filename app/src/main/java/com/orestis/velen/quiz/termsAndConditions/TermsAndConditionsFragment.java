package com.orestis.velen.quiz.termsAndConditions;

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
import com.orestis.velen.quiz.sound.SoundPoolHelper;

public class TermsAndConditionsFragment extends Fragment {

    private ImageView darkBg;
    private SoundPoolHelper soundHelper;
    private Fragment fallBackFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.terms_and_conditions_fragment, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        Button closeBtn = view.findViewById(R.id.close_menu);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        Button closeTerms = view.findViewById(R.id.closeTerms);
        closeTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });
    }

    private void closeFragment() {
        soundHelper.playMenuBtnCloseSound();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(this).addToBackStack(null).commit();
        darkBg.setVisibility(View.GONE);
    }

    private void back() {
        soundHelper.playMenuBtnCloseSound();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.optionScreenPlaceholder, fallBackFragment).commit();
    }

    public static class Builder {
        private ImageView darkBg;
        private SoundPoolHelper soundHelper;
        private Fragment fallBackFragment;

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

        public TermsAndConditionsFragment build() {
            TermsAndConditionsFragment termsAndConditionsFragment = new TermsAndConditionsFragment();
            termsAndConditionsFragment.darkBg = darkBg;
            termsAndConditionsFragment.soundHelper = soundHelper;
            termsAndConditionsFragment.fallBackFragment = fallBackFragment;
            return termsAndConditionsFragment;
        }
    }
}
