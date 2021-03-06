package com.orestis.velen.quiz.skillUpgrades;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.orestis.velen.quiz.credits.CreditsFragment;
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.settings.SettingsFragment;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

import java.util.ArrayList;
import java.util.List;

public class SkillUpgradesFragment extends Fragment implements SkillPlusListener, SkillMinusListener, SkillDescriptionListener {

    private Player player;
    private ListView skillUpgradesListView;
    private int remainingSkillPoints;
    private int initialRemainingSkillPoints;
    private TextView skillPointsCount;
    private List<SkillUpgradesItem> skills;
    private SkillUpgradesListAdapter adapter;
    private Button skillsChosenConfirmationBtn;
    private ImageView darkBg;
    private SkillSumChangeListener skillSumChangeListener;
    private SoundPoolHelper soundHelper;
    private boolean releaseDarkBgOnClose;
    private LevelUpScreenClosedListener levelUpScreenClosedListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.skill_upgrades_fragment, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        skillUpgradesListView = view.findViewById(R.id.skillUpgradesListView);
        initialRemainingSkillPoints = remainingSkillPoints = player.getRemainingSkillPoints();
        skillPointsCount = view.findViewById(R.id.skillPointsCount);
        darkBg.setVisibility(View.VISIBLE);
        updateSkillPointsCountText();
        setupSkillsAdapter();
        skillsChosenConfirmationBtn = view.findViewById(R.id.skills_chosen_confirmation);
        setConfirmationBtnToSkip();
    }

    private void setConfirmationBtnToUpgrade() {
        skillsChosenConfirmationBtn.setText(getString(R.string.confirm));
        skillsChosenConfirmationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (initialRemainingSkillPoints > remainingSkillPoints) {
                    confirmSkillUpgrades();
                    closeFragment();
                    if(remainingSkillPoints <= 0) {
                        skillsChosenConfirmationBtn.setEnabled(false);
                    }
                }
            }
        });
    }

    private void setConfirmationBtnToSkip() {
        skillsChosenConfirmationBtn.setText(getString(R.string.close));
        skillsChosenConfirmationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });
    }

    private void closeFragment() {
        soundHelper.playMenuBtnCloseSound();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(SkillUpgradesFragment.this).commit();
        if(releaseDarkBgOnClose) {
            darkBg.setVisibility(View.GONE);
        }
        if(levelUpScreenClosedListener != null) {
            levelUpScreenClosedListener.onLevelUpScreenClosed();
        }
    }

    private void setupSkillsAdapter() {
        skills = new ArrayList<>();
        ShieldUpgradeItem shieldUpgradeItem = new ShieldUpgradeItem();
        shieldUpgradeItem.setSkillPlusListener(this);
        shieldUpgradeItem.setSkillMinusListener(this);
        shieldUpgradeItem.setSkillDescriptionListener(this);
        shieldUpgradeItem.setPlayer(player);
        FreezeTimeUpgradeItem freezeTimeUpgradeItem = new FreezeTimeUpgradeItem();
        freezeTimeUpgradeItem.setSkillPlusListener(this);
        freezeTimeUpgradeItem.setSkillMinusListener(this);
        freezeTimeUpgradeItem.setSkillDescriptionListener(this);
        freezeTimeUpgradeItem.setPlayer(player);
        FiftyFiftyUpgradeItem fiftyFiftyUpgradeItem = new FiftyFiftyUpgradeItem();
        fiftyFiftyUpgradeItem.setSkillPlusListener(this);
        fiftyFiftyUpgradeItem.setSkillMinusListener(this);
        fiftyFiftyUpgradeItem.setSkillDescriptionListener(this);
        fiftyFiftyUpgradeItem.setPlayer(player);
        SkipUpgradeItem skipUpgradeItem = new SkipUpgradeItem();
        skipUpgradeItem.setSkillPlusListener(this);
        skipUpgradeItem.setSkillMinusListener(this);
        skipUpgradeItem.setSkillDescriptionListener(this);
        skipUpgradeItem.setPlayer(player);
        ExtraTimeUpgradeItem extraTimeUpgradeItem = new ExtraTimeUpgradeItem();
        extraTimeUpgradeItem.setSkillPlusListener(this);
        extraTimeUpgradeItem.setSkillMinusListener(this);
        extraTimeUpgradeItem.setSkillDescriptionListener(this);
        extraTimeUpgradeItem.setPlayer(player);
        skills.add(skipUpgradeItem);
        skills.add(fiftyFiftyUpgradeItem);
        skills.add(shieldUpgradeItem);
        skills.add(freezeTimeUpgradeItem);
        skills.add(extraTimeUpgradeItem);
        setCanBeIncremented(canSkillsBeIncremented());
        adapter = new SkillUpgradesListAdapter(skills, getContext());
        skillUpgradesListView.setAdapter(adapter);
    }

    private void updateSkillPointsCountText() {
        skillPointsCount.setText(String.valueOf(remainingSkillPoints));
    }

    private void setCanBeIncremented(boolean canBeIncremented) {
        for (SkillUpgradesItem skill : skills) {
            skill.setCanBeIncremented(canBeIncremented);
        }
    }

    private boolean canSkillsBeIncremented() {
        return remainingSkillPoints > 0;
    }

    @Override
    public void onSkillPlus(String skillType) {
        soundHelper.playMenuBtnOpenSound();
        if (remainingSkillPoints > 0) {
            for (SkillUpgradesItem skill : skills) {
                if(skill.getSkillName().equals(skillType) && !skill.isMaxLevel()) {
                    remainingSkillPoints--;
                    updateSkillPointsCountText();
                    skill.incrementSkillLevel();
                    setCanBeIncremented(canSkillsBeIncremented());
                    adapter.notifyDataSetChanged();
                    setConfirmationBtnToUpgrade();
                    break;
                }
            }
        }
    }

    @Override
    public void onSkillMinus(String skillType) {
        soundHelper.playMenuBtnOpenSound();
        if (initialRemainingSkillPoints > remainingSkillPoints) {
            for (SkillUpgradesItem skill : skills) {
                if(skill.getSkillName().equals(skillType) && skill.getSkillLevelTimesIncreased() > 0) {
                    skill.decreaseSkillLevel();
                    remainingSkillPoints++;
                    updateSkillPointsCountText();
                    setCanBeIncremented(canSkillsBeIncremented());
                    adapter.notifyDataSetChanged();
                    if (initialRemainingSkillPoints <= remainingSkillPoints) {
                        setConfirmationBtnToSkip();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onSkillDescription(String skillType) {
        soundHelper.playMenuBtnOpenSound();
        for (SkillUpgradesItem skill : skills) {
            if(skill.getSkillName().equals(skillType)) {
                SkillDescriptionFragment skillDescriptionFragment = new SkillDescriptionFragment.Builder()
                        .withSoundPoolHelper(soundHelper)
                        .withFallBackFragment(SkillUpgradesFragment.this)
                        .forSkill(skill)
                        .build();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.optionScreenPlaceholder, skillDescriptionFragment, "SkillDescriptionTag");
                ft.addToBackStack("skillUpgrades");
                ft.commit();
            }
        }
    }

    private void confirmSkillUpgrades() {
        for (SkillUpgradesItem skill : skills) {
            player.getPowers().get(skill.getSkillName()).setPowerLevel(skill.getSkillLevel());
        }
        player.setRemainingSkillPoints(remainingSkillPoints);
        player.save();
        if(skillSumChangeListener != null) {
            skillSumChangeListener.onSkillSumChange(remainingSkillPoints);
        }
    }

    public static class Builder {
        private Player player;
        private ImageView darkBg;
        private SkillSumChangeListener skillSumChangeListener;
        private SoundPoolHelper soundHelper;
        private boolean releaseDarkBgOnClose = true;
        private LevelUpScreenClosedListener levelUpScreenClosedListener;

        public Builder forPlayer(Player player) {
            this.player = player;
            return this;
        }

        public Builder withDarkBg(ImageView darkBg) {
            this.darkBg = darkBg;
            return this;
        }

        public Builder doNotReleaseDarkBgOnClose() {
            this.releaseDarkBgOnClose = false;
            return this;
        }

        public Builder withSkillSumChangeListener(@Nullable SkillSumChangeListener skillSumChangeListener) {
            this.skillSumChangeListener = skillSumChangeListener;
            return this;
        }

        public Builder withSoundPoolHelper(SoundPoolHelper soundHelper) {
            this.soundHelper = soundHelper;
            return this;
        }

        public Builder withCloseListener(@Nullable LevelUpScreenClosedListener levelUpScreenClosedListener) {
            this.levelUpScreenClosedListener = levelUpScreenClosedListener;
            return this;
        }

        public SkillUpgradesFragment build() {
            SkillUpgradesFragment skillUpgradesFragment = new SkillUpgradesFragment();
            skillUpgradesFragment.player = player;
            skillUpgradesFragment.darkBg = darkBg;
            skillUpgradesFragment.skillSumChangeListener = skillSumChangeListener;
            skillUpgradesFragment.soundHelper = soundHelper;
            skillUpgradesFragment.releaseDarkBgOnClose = releaseDarkBgOnClose;
            skillUpgradesFragment.levelUpScreenClosedListener = levelUpScreenClosedListener;
            return skillUpgradesFragment;
        }
    }
}
