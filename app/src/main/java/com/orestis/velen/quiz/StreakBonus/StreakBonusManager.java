package com.orestis.velen.quiz.StreakBonus;

import com.orestis.velen.quiz.leveling.LevelExperienceMetrics;
import com.orestis.velen.quiz.leveling.LevelExperienceMetricsFactory;
import com.orestis.velen.quiz.questions.Difficulty;
import com.orestis.velen.quiz.questions.GameType;
import com.orestis.velen.quiz.repositories.GameTheme;

public class StreakBonusManager {

    private int accumulatedStreakBonus;
    private int accumulatedCorrectAnswerBonus;
    private int accumulatedTotalBonus;
    private int streak;
    private int xpPerBonus;
    private int xpPerPerfectBonus;
    private int xpForCorrectAnswer;
    private int neededStreak;
    private StreakBonusDisplayHandler bonusDisplayHandler;
    private int correctAnswers;

    public StreakBonusManager(GameTheme gameTheme, Difficulty difficulty, GameType gameType, StreakBonusDisplayHandler bonusDisplayHandler) {
        LevelExperienceMetricsFactory metricsFactory = new LevelExperienceMetricsFactory();
        LevelExperienceMetrics metrics = metricsFactory.getLevelExperienceMetrics(gameTheme);
        xpPerBonus = metrics.getExperienceForBonus(difficulty);
        xpPerPerfectBonus = metrics.getExperienceForPerfectBonus(difficulty);
        xpForCorrectAnswer = metrics.getExperienceForCorrectAnswer(difficulty);
        neededStreak = metrics.getStreakNeededForBonus(gameType);
        this.bonusDisplayHandler = bonusDisplayHandler;
    }

    public void correctAnswerGiven() {
        correctAnswerGiven(xpPerBonus);
    }

    public void perfectAnswerGiven() {
        correctAnswerGiven(xpPerPerfectBonus);
    }

    private void correctAnswerGiven(int xpPerBonus) {
        correctAnswers++;
        streak++;
        if(streak >= neededStreak) {
            int bonusAward = xpPerBonus * (streak - neededStreak + 1);
            accumulatedStreakBonus += bonusAward;
            accumulatedCorrectAnswerBonus += xpForCorrectAnswer;
            accumulatedTotalBonus = accumulatedStreakBonus + accumulatedCorrectAnswerBonus;
            publishBonusChange(bonusAward + xpForCorrectAnswer, "+");
        } else {
            accumulatedCorrectAnswerBonus += xpForCorrectAnswer;
            accumulatedTotalBonus = accumulatedStreakBonus + accumulatedCorrectAnswerBonus;
            publishBonusChange(xpForCorrectAnswer, "+");
        }
    }

    public void wrongAnswerGiven() {
        streak = 0;
    }

    public void makeBonusChange(int bonusNow, String sign) {
        if(sign.equals("+")) {
            accumulatedTotalBonus += bonusNow;
        } else if(sign.equals("-")) {
            if(bonusNow < accumulatedTotalBonus) {
                accumulatedTotalBonus -= bonusNow;
            } else {
                accumulatedTotalBonus = 0;
            }
        }
        if(!sign.equals("") && bonusNow != 0) {
            publishBonusChange(bonusNow, sign);
        }
    }

    private void publishBonusChange(int bonusNow, String sign) {
        bonusDisplayHandler.displayBonus(bonusNow, accumulatedTotalBonus, sign);
    }

    public int getAccumulatedStreakBonus() {
        return accumulatedStreakBonus;
    }

    public int getaAcumulatedCorrectAnswerBonus() {
        return accumulatedCorrectAnswerBonus;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

}
