package com.orestis.velen.quiz.language;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.orestis.velen.quiz.mainMenu.MainMenuActivity;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.orestis.velen.quiz.mainMenu.MainMenuActivity.SHOULD_ANIMATE_ENTRANCE;

public class LanguageFragment extends Fragment {

    private ImageView darkBg;
    private SoundPoolHelper soundHelper;
    private String selectedLanguage;
    private List<ImageView> allLanguages;
    private String storedLanguage;
    private Button languageChosenConfirmationBtn;
    private Fragment fallBackFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.language_fragment, parent, false);
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

        languageChosenConfirmationBtn = view.findViewById(R.id.language_chosen_confirmation);
        languageChosenConfirmationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmChangingLanguage();
            }
        });

        allLanguages = new ArrayList<>();
        final ImageView englishLanguage = view.findViewById(R.id.englishLanguage);
        final ImageView greekLanguage = view.findViewById(R.id.greekLanguage);
        final ImageView italianLanguage = view.findViewById(R.id.italianLanguage);
        allLanguages.add(englishLanguage);
        allLanguages.add(greekLanguage);
        allLanguages.add(italianLanguage);

        Map<String, ImageView> localeCodeToImageMap = new HashMap<>();
        localeCodeToImageMap.put("en", englishLanguage);
        localeCodeToImageMap.put("el", greekLanguage);
        localeCodeToImageMap.put("it", italianLanguage);

        storedLanguage = LocaleHelper.getLanguage(getContext());
        highLightImage(localeCodeToImageMap.get(storedLanguage));

        englishLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundHelper.playMenuBtnOpenSound();
                selectedLanguage = "en";
                highLightImage(englishLanguage);
                adjustConfirmEnabled();
            }
        });

        greekLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundHelper.playMenuBtnOpenSound();
                selectedLanguage = "el";
                highLightImage(greekLanguage);
                adjustConfirmEnabled();
            }
        });

        italianLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundHelper.playMenuBtnOpenSound();
                selectedLanguage = "it";
                highLightImage(italianLanguage);
                languageChosenConfirmationBtn.setEnabled(false);
            }
        });
    }

    private void highLightImage(ImageView image) {
        for (int i=0; i < allLanguages.size(); i++) {
            if (allLanguages.get(i).getBackground() != null) {
                allLanguages.get(i).setBackground(null);
            }
        }
        Drawable highlight = getResources().getDrawable( R.drawable.image_frame);
        image.setBackground(highlight);
    }

    private void adjustConfirmEnabled() {
        if(!storedLanguage.equals(selectedLanguage)) {
            languageChosenConfirmationBtn.setEnabled(true);
        } else {
            languageChosenConfirmationBtn.setEnabled(false);
        }
    }

    private void confirmChangingLanguage() {
        if (selectedLanguage != null) {
            LocaleHelper.setLocale(getActivity(), selectedLanguage);
            Intent intent = new Intent(getActivity(), MainMenuActivity.class);
            intent.putExtra(SHOULD_ANIMATE_ENTRANCE, false);
            startActivity(intent);
            getActivity().overridePendingTransition( 0, 0);
            getActivity().finish();
            getActivity().overridePendingTransition( 0, 0);
        }
    }

    private void closeFragment() {
        soundHelper.playMenuBtnCloseSound();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(LanguageFragment.this).addToBackStack(null).commit();
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

        public LanguageFragment build() {
            LanguageFragment languageFragment = new LanguageFragment();
            languageFragment.darkBg = darkBg;
            languageFragment.soundHelper = soundHelper;
            languageFragment.fallBackFragment = fallBackFragment;
            return languageFragment;
        }
    }
}
