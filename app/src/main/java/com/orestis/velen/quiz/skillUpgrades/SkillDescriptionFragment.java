package com.orestis.velen.quiz.skillUpgrades;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

public class SkillDescriptionFragment extends Fragment {
    private SoundPoolHelper soundHelper;
    private Fragment fallBackFragment;
    private SkillUpgradesItem skill;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.skill_description_fragment, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ImageView skillImage = view.findViewById(R.id.skill_icon_drawable);
        ImageView skillImageBg = view.findViewById(R.id.skill_icon_bg_color);
        skillImage.setImageResource(skill.getIconDrawableId());
        skillImageBg.setImageResource(skill.getBackGroundColorId());
        TextView skill_description = view.findViewById(R.id.skill_description);
        skill_description.setText(skill.getDescriptionTitle(requireContext()));
        TextView levelText = view.findViewById(R.id.levelText);
        levelText.setText(requireContext().getString(R.string.level));
        TextView bonusText = view.findViewById(R.id.bonusText);
        bonusText.setText(skill.getDescriptionBonusText(requireContext()));
        TextView usagesText = view.findViewById(R.id.usagesText);
        usagesText.setText(skill.getDescriptionUsagesText(requireContext()));
        TextView powerName = view.findViewById(R.id.power_name);
        powerName.setText(skill.getDisplayName(requireContext()));

        Button closeDescription = view.findViewById(R.id.closeDescription);
        closeDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });

        ListView skillDetailsListView = view.findViewById(R.id.skillDetailsListView);
        SkillDescriptionAdapter adapter = new SkillDescriptionAdapter(skill.getDescriptionItems(requireContext()), requireContext());
        skillDetailsListView.setAdapter(adapter);
    }

    private void back() {
        soundHelper.playMenuBtnCloseSound();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.optionScreenPlaceholder, fallBackFragment).commit();
    }

    public static class Builder {
        private SoundPoolHelper soundHelper;
        private Fragment fallBackFragment;
        private SkillUpgradesItem skill;

        public Builder forSkill(SkillUpgradesItem skill) {
            this.skill = skill;
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

        public SkillDescriptionFragment build() {
            SkillDescriptionFragment skillDescriptionFragment = new SkillDescriptionFragment();
            skillDescriptionFragment.soundHelper = soundHelper;
            skillDescriptionFragment.fallBackFragment = fallBackFragment;
            skillDescriptionFragment.skill = skill;
            return skillDescriptionFragment;
        }
    }
}
