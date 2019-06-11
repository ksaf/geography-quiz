package com.orestis.velen.quiz.victoryFragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.leveling.LevelExperienceMetricsFactory;
import com.orestis.velen.quiz.leveling.LevelUpListener;
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.questions.Difficulty;
import com.orestis.velen.quiz.remainingTime.CountDownTextHandler;
import com.orestis.velen.quiz.repositories.GameTheme;

import java.text.DecimalFormat;

import static com.orestis.velen.quiz.R.string.level;
import static com.orestis.velen.quiz.victoryFragment.RewardPhase.BONUS_REWARD;
import static com.orestis.velen.quiz.victoryFragment.RewardPhase.COMPLETION_REWARD;
import static com.orestis.velen.quiz.victoryFragment.RewardPhase.TIME_REWARD;

public class VictoryFragment extends Fragment implements LevelUpListener, VictoryPhaseEndListener {

    private long msTimeLeft;
    private long maxTime;
    private Difficulty difficulty;
    private ExperienceBarHandler timeExperienceBarHandler;
    private ExperienceBarHandler bonusExperienceBarHandler;
    private CountDownTextHandler timeCountDownTextHandler;
    private CountDownTextHandler bonusCountDownTextHandler;
    private Button revealScoreBtn;
    private TextView levelText;
    private int timeXpGained;
    private int completionXpGained;
    private int bonusAccumulated;
    private TextView completionBonusTxt;
    private TextView timeLeftBonusTxt;
    private TextView bonusSizeTxt;
    private TextView completionBonusStaticTxt;
    private TextView timeBonusStaticTxt;
    private TextView bounusStaticTxt;
    private Player player;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.victory_fragment, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        completionBonusTxt = view.findViewById(R.id.completionBonusTxt);
        timeLeftBonusTxt = view.findViewById(R.id.timeLeftBonusTxt);
        bonusSizeTxt = view.findViewById(R.id.bonusSizeTxt);
        completionBonusStaticTxt = view.findViewById(R.id.completionBonusStaticTxt);
        timeBonusStaticTxt = view.findViewById(R.id.timeBonusStaticTxt);
        bounusStaticTxt = view.findViewById(R.id.bounusStaticTxt);
        levelText = view.findViewById(R.id.levelText);
        ProgressBar progressBar = view.findViewById(R.id.levelBar);
        revealScoreBtn = view.findViewById(R.id.revealScoreBtn);
        DecimalFormat form = new DecimalFormat("0.00");

        int timeLeftPercentage = (int)(msTimeLeft * 100.0 / maxTime + 0.5);
        LevelExperienceMetricsFactory metricsFactory = new LevelExperienceMetricsFactory();
        timeXpGained = metricsFactory.getLevelExperienceMetrics(GameTheme.GEOGRAPHY).getExperienceForTimeLeft(timeLeftPercentage, difficulty);
        completionXpGained = metricsFactory.getLevelExperienceMetrics(GameTheme.GEOGRAPHY).getExperienceForCompletion(difficulty);

        completionBonusTxt.setText(String.valueOf(completionXpGained));
        timeLeftBonusTxt.setText(form.format(msTimeLeft / 1000.00));
        bonusSizeTxt.setText(String.valueOf(bonusAccumulated));

        setTextsBold(COMPLETION_REWARD);

        levelText.setText(getString(level) + player.getCurrentLevel());
        progressBar.setMax(player.getXpToLevel());
        progressBar.setProgress(player.getCurrentXP());


        final CountDownTextHandler completionCountDownTextHandler = new CountDownTextHandler(msTimeLeft / 15L, 1000/40, completionBonusTxt, completionXpGained, false);
        final ExperienceBarHandler completionExperienceBarHandler = new ExperienceBarHandler(1500, 1000/40, progressBar, player, this, this, COMPLETION_REWARD);

        timeCountDownTextHandler = new CountDownTextHandler(msTimeLeft / 15L, 1000/40, timeLeftBonusTxt, msTimeLeft, true);
        timeExperienceBarHandler = new ExperienceBarHandler(msTimeLeft / 15L, 1000/40, progressBar, player, this, this, TIME_REWARD);

        bonusCountDownTextHandler = new CountDownTextHandler(1500 , 1000/40, bonusSizeTxt, bonusAccumulated, false);
        bonusExperienceBarHandler = new ExperienceBarHandler(1500, 1000/40, progressBar, player, this, this, BONUS_REWARD);


        revealScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completionCountDownTextHandler.start();
                completionExperienceBarHandler.setScoreToProgress(completionXpGained);
                completionExperienceBarHandler.start();
                view.setEnabled(false);
            }
        });

    }

    private void setTextsBold(RewardPhase rewardPhase) {
        switch (rewardPhase) {
            case COMPLETION_REWARD:
                completionBonusTxt.setTypeface(completionBonusTxt.getTypeface(), Typeface.BOLD_ITALIC);
                completionBonusStaticTxt.setTypeface(completionBonusTxt.getTypeface(), Typeface.BOLD_ITALIC);
                break;
            case TIME_REWARD:
                completionBonusTxt.setTypeface(null, Typeface.NORMAL);
                completionBonusStaticTxt.setTypeface(null, Typeface.NORMAL);
                timeLeftBonusTxt.setTypeface(timeLeftBonusTxt.getTypeface(), Typeface.BOLD_ITALIC);
                timeBonusStaticTxt.setTypeface(timeBonusStaticTxt.getTypeface(), Typeface.BOLD_ITALIC);
                break;
            case BONUS_REWARD:
                timeLeftBonusTxt.setTypeface(null, Typeface.NORMAL);
                timeBonusStaticTxt.setTypeface(null, Typeface.NORMAL);
                bonusSizeTxt.setTypeface(bonusSizeTxt.getTypeface(), Typeface.BOLD_ITALIC);
                bounusStaticTxt.setTypeface(bounusStaticTxt.getTypeface(), Typeface.BOLD_ITALIC);
                break;
        }
    }

    @Override
    public void onLevelUp() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                levelText.setText(getString(level) + player.getCurrentLevel());
            }
        });

    }

    @Override
    public void onPhaseEnd(RewardPhase rewardPhase) {
        switch (rewardPhase) {
            case COMPLETION_REWARD:
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        revealScoreBtn.setEnabled(true);
                        revealScoreBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                timeCountDownTextHandler.start();
                                timeExperienceBarHandler.setScoreToProgress(timeXpGained);
                                timeExperienceBarHandler.start();
                                view.setEnabled(false);
                                setTextsBold(TIME_REWARD);
                            }
                        });
                    }
                });
                break;
            case TIME_REWARD:
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        revealScoreBtn.setEnabled(true);
                        revealScoreBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bonusCountDownTextHandler.start();
                                bonusExperienceBarHandler.setScoreToProgress(bonusAccumulated);
                                bonusExperienceBarHandler.start();
                                view.setEnabled(false);
                                setTextsBold(BONUS_REWARD);
                            }
                        });
                    }
                });
                break;
            case BONUS_REWARD:
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        revealScoreBtn.setEnabled(true);
                        revealScoreBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    }
                });
                break;
        }
    }

    public static class Builder {
        private long msTimeLeft;
        private long maxTime;
        private Difficulty difficulty;
        private int bonusAccumulated;
        private Player player;

        public Builder withBonusAccumulated(int bonusAccumulated) {
            this.bonusAccumulated = bonusAccumulated;
            return this;
        }

        public Builder withTimeLeft(long msTimeLeft) {
            this.msTimeLeft = msTimeLeft;
            return this;
        }

        public Builder withMaxTime(long maxTime) {
            this.maxTime = maxTime;
            return this;
        }

        public Builder forDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public Builder forPlayer(Player player) {
            this.player = player;
            return this;
        }

        public VictoryFragment build() {
            VictoryFragment victoryFragment = new VictoryFragment();
            victoryFragment.msTimeLeft = msTimeLeft;
            victoryFragment.maxTime = maxTime;
            victoryFragment.difficulty = difficulty;
            victoryFragment.bonusAccumulated = bonusAccumulated;
            victoryFragment.player = player;
            return victoryFragment;
        }
    }
}
