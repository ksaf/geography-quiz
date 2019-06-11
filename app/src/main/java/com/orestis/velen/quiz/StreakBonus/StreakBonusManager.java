package com.orestis.velen.quiz.StreakBonus;

import com.orestis.velen.quiz.leveling.LevelExperienceMetrics;
import com.orestis.velen.quiz.leveling.LevelExperienceMetricsFactory;
import com.orestis.velen.quiz.questions.Difficulty;
import com.orestis.velen.quiz.questions.GameType;
import com.orestis.velen.quiz.repositories.GameTheme;

public class StreakBonusManager {

    private int accumulatedBonus;
    private int streak;
    private int xpPerBonus;
    private int xpPerPerfectBonus;
    private int neededStreak;
    private BonusAwardedListener bonusAwardedListener;

    public StreakBonusManager(GameTheme gameTheme, Difficulty difficulty, GameType gameType, BonusAwardedListener bonusAwardedListener) {
        LevelExperienceMetricsFactory metricsFactory = new LevelExperienceMetricsFactory();
        LevelExperienceMetrics metrics = metricsFactory.getLevelExperienceMetrics(gameTheme);
        xpPerBonus = metrics.getExperienceForBonus(difficulty);
        xpPerPerfectBonus = metrics.getExperienceForPerfectBonus(difficulty);
        neededStreak = metrics.getStreakNeededForBonus(gameType);
        this.bonusAwardedListener = bonusAwardedListener;
    }

    public void correctAnswerGiven() {
        correctAnswerGiven(xpPerBonus);
    }

    public void perfectAnswerGiven() {
        correctAnswerGiven(xpPerPerfectBonus);
    }

    private void correctAnswerGiven(int xpPerBonus) {
        streak++;
        if(streak >= neededStreak) {
            int bonusAward = xpPerBonus * (streak - neededStreak + 1);
            accumulatedBonus += bonusAward;
            publishBonusAwarded(bonusAward);
        }
    }

    public void wrongAnswerGiven() {
        streak = 0;
    }

    private void publishBonusAwarded(int bonusNow) {
        bonusAwardedListener.onBonusAwarded(bonusNow, accumulatedBonus);
    }

    public int getAccumulatedBonus() {
        return accumulatedBonus;
    }

}
