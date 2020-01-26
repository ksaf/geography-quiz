package com.orestis.velen.quiz.gameEnd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.StreakBonus.StreakBonusManager;
import com.orestis.velen.quiz.adverts.FinishGameWithAdHandler;
import com.orestis.velen.quiz.leveling.LevelExperienceMetricsFactory;
import com.orestis.velen.quiz.mainMenu.MainMenuActivity;
import com.orestis.velen.quiz.player.PlayerSession;
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.questions.Difficulty;
import com.orestis.velen.quiz.repositories.GameTheme;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

import java.text.DecimalFormat;

public class GameEndWinFragment extends Fragment implements LevelUpListener, ExperienceGainEndListener {

    private long msTimeLeft;
    private long maxTime;
    private Difficulty difficulty;
    private StreakBonusManager bonusManager;
    private Player player;
    private TextView skillPointText;
    private Button revealScoreBtn;
    private int levelsGained = 0;
    private ImageView darkBg;
    private SoundPoolHelper soundHelper;
    private boolean hasXpBoostEnabled;
    private int totalQuestionAmount;
    private final static double XP_BOOST_MULTIPLIER = 1.5;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_end_win_fragment, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        TextView completionBonusTxt = view.findViewById(R.id.completionBonusTxt);
        TextView completionBonusStaticTxt = view.findViewById(R.id.completionBonusStaticTxt);
        TextView timeLeftBonusTxt = view.findViewById(R.id.timeLeftBonusTxt);
        TextView timeBonusStaticTxt = view.findViewById(R.id.timeBonusStaticTxt);
        TextView streakBonusTxt = view.findViewById(R.id.streakBonusTxt);
        TextView totalBonus = view.findViewById(R.id.totalBonus);
        TextView highScoreTxt = view.findViewById(R.id.highScoreTxt);
        skillPointText = view.findViewById(R.id.skillPointText);
        skillPointText.setVisibility(View.GONE);
        darkBg.setVisibility(View.VISIBLE);
        final RoundCornerProgressBar levelBar = view.findViewById(R.id.levelBar);
        TextView levelBarText = view.findViewById(R.id.levelBarText);
        revealScoreBtn = view.findViewById(R.id.revealScoreBtn);
        DecimalFormat form = new DecimalFormat("0.00");

        int timeLeftPercentage = (int)(msTimeLeft * 100.0 / maxTime + 0.5);
        LevelExperienceMetricsFactory metricsFactory = new LevelExperienceMetricsFactory();
        final int timeXpGained = metricsFactory.getLevelExperienceMetrics(GameTheme.GEOGRAPHY).getExperienceForTimeLeft(timeLeftPercentage, difficulty);
        final int completionXpGained = bonusManager.getaAcumulatedCorrectAnswerBonus();
        final int streakBonus = bonusManager.getAccumulatedStreakBonus();

        timeBonusStaticTxt.setText(Html.fromHtml(timeBonusStaticTxt.getText() + " <i>(" + form.format(msTimeLeft / 1000.00) + ")</i> "));
        completionBonusStaticTxt.setText(Html.fromHtml(completionBonusStaticTxt.getText() +
                " <i>(" + bonusManager.getCorrectAnswers() + "/" + totalQuestionAmount + ")</i> "));
        completionBonusTxt.setText(String.valueOf(completionXpGained));
        timeLeftBonusTxt.setText(String.valueOf(timeXpGained));
        streakBonusTxt.setText(String.valueOf(streakBonus));
        totalBonus.setText(String.valueOf(completionXpGained + timeXpGained + streakBonus));
        levelBarText.setText(String.valueOf(player.getCurrentXP()) + "/" + String.valueOf(player.getXpToLevel()));
        final int total = timeXpGained + completionXpGained + streakBonus;
        if(total > player.getHighScore()) {
            highScoreTxt.setVisibility(View.VISIBLE);
        }

        levelBar.setProgress((int) ((long)player.getCurrentXP() * 100 / (long)player.getXpToLevel()) );
        ExperienceTextHandler experienceTextHandler = new ExperienceTextHandler(1300, 1000/40, levelBarText, getActivity());
        final XPBarHandler xpBarHandler = new XPBarHandler(1300, 1000/40, levelBar,
                player, getActivity(), this, experienceTextHandler, this);

        revealScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundHelper.playInGameMenuSelectSound();
                soundHelper.playInGameGainXpSound();
                if(total > player.getHighScore()) {
                    player.saveHighScore(getActivity(), total);
                }
                int xp = hasXpBoostEnabled ? (int)(total * XP_BOOST_MULTIPLIER) : total;

                xpBarHandler.setScoreToProgress(xp);
                view.setEnabled(false);
            }
        });

        TextView gameEndXpBoostEnabledTxt = view.findViewById(R.id.gameEndXpBoostEnabledTxt);
        if(hasXpBoostEnabled) {
            String xpBonus = " <b><font color='#000000'>" + ((int)(total * XP_BOOST_MULTIPLIER) - total) + "</font></b> ";
            gameEndXpBoostEnabledTxt.setText(Html.fromHtml(getString(R.string.gameEndXpBoostEnabled) + xpBonus));
            gameEndXpBoostEnabledTxt.setVisibility(View.VISIBLE);
        } else {
            gameEndXpBoostEnabledTxt.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLevelUp() {
        levelsGained++;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                skillPointText.setText("+" + levelsGained + " " + getString(R.string.skill_point));
                skillPointText.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onExperienceGainEnd() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                revealScoreBtn.setText(R.string.finish);
                revealScoreBtn.setEnabled(true);
                revealScoreBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        soundHelper.playInGameMenuSelectSound();
                        FinishGameWithAdHandler.finishGame(new FinishGameWithAdHandler.ExitGameListener() {
                            @Override
                            public void onExitGame() {
                                goToMainMenu();
                            }
                        });
                    }
                });
            }
        });
    }

    private void goToMainMenu() {
        Intent intent = new Intent(GameEndWinFragment.this.getActivity(), MainMenuActivity.class);
        PlayerSession.getInstance().setRecoveredPlayer(player);
        GameEndWinFragment.this.getActivity().startActivity(intent);
        GameEndWinFragment.this.getActivity().finish();
        GameEndWinFragment.this.getActivity().overridePendingTransition(0, 0);
    }

    public static class Builder {
        private long msTimeLeft;
        private long maxTime;
        private Difficulty difficulty;
        private StreakBonusManager bonusManager;
        private Player player;
        private ImageView darkBg;
        private SoundPoolHelper soundHelper;
        private boolean hasXpBoostEnabled;
        private int totalQuestionAmount;

        public Builder withStreakBonusManager(StreakBonusManager bonusManager) {
            this.bonusManager = bonusManager;
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

        public Builder withDarkBg(ImageView darkBg) {
            this.darkBg = darkBg;
            return this;
        }

        public Builder withSoundPoolHelper(SoundPoolHelper soundHelper) {
            this.soundHelper = soundHelper;
            return this;
        }

        public Builder hasXpBoostEnabled(boolean hasXpBoostEnabled) {
            this.hasXpBoostEnabled = hasXpBoostEnabled;
            return this;
        }

        public Builder forTotalQuestionAmount(int totalQuestionAmount) {
            this.totalQuestionAmount = totalQuestionAmount;
            return this;
        }

        public GameEndWinFragment build() {
            GameEndWinFragment gameEndWinFragment = new GameEndWinFragment();
            gameEndWinFragment.msTimeLeft = msTimeLeft;
            gameEndWinFragment.maxTime = maxTime;
            gameEndWinFragment.difficulty = difficulty;
            gameEndWinFragment.bonusManager = bonusManager;
            gameEndWinFragment.player = player;
            gameEndWinFragment.darkBg = darkBg;
            gameEndWinFragment.soundHelper = soundHelper;
            gameEndWinFragment.hasXpBoostEnabled = hasXpBoostEnabled;
            gameEndWinFragment.totalQuestionAmount = totalQuestionAmount;
            return gameEndWinFragment;
        }
    }
}
