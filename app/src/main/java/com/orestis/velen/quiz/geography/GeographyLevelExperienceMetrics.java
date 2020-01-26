package com.orestis.velen.quiz.geography;

import android.util.SparseIntArray;

import com.orestis.velen.quiz.leveling.LevelExperienceMetrics;
import com.orestis.velen.quiz.questions.Difficulty;
import com.orestis.velen.quiz.questions.GameType;

public class GeographyLevelExperienceMetrics implements LevelExperienceMetrics {

    private SparseIntArray metrics;

    public GeographyLevelExperienceMetrics() {
        metrics = new SparseIntArray();
        metrics.append(1, 1000);
        metrics.append(2, 2000);
        metrics.append(3, 3000);
        metrics.append(4, 4000);
        metrics.append(5, 5000);
        metrics.append(6, 6000);
        metrics.append(7, 7000);
        metrics.append(8, 8000);
        metrics.append(9, 9000);
        metrics.append(10, 11000);
        metrics.append(11, 11000);
        metrics.append(12, 12000);
        metrics.append(13, 13000);
        metrics.append(14, 14000);
        metrics.append(15, 15000);
        metrics.append(16, 16000);
        metrics.append(17, 17000);
        metrics.append(18, 18000);
        metrics.append(19, 19000);
        metrics.append(20, 20000);
    }

    public int getExperienceForLevelUp(int level) {
        return metrics.get(level);
    }

    public int getExperienceForCompletion(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return 200;
            case NORMAL:
                return 500;
            case HARD:
                return 800;
            default:
                return 200;
        }
    }

    public int getExperienceForCorrectAnswer(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return 20;
            case NORMAL:
                return 40;
            case HARD:
                return 80;
            default:
                return 20;
        }
    }

    public int getExperienceForTimeLeft(int timeLeftPercentage, Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return timeLeftPercentage * 10;
            case NORMAL:
                return timeLeftPercentage * 15;
            case HARD:
                return timeLeftPercentage * 20;
            default:
                return timeLeftPercentage * 10;
        }
    }

    @Override
    public int getExperienceForBonus(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return 10;
            case NORMAL:
                return 15;
            case HARD:
                return 20;
            default:
                return 10;
        }
    }

    @Override
    public int getExperienceForPerfectBonus(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return 15;
            case NORMAL:
                return 20;
            case HARD:
                return 25;
            default:
                return 15;
        }
    }

    @Override
    public int getStreakNeededForBonus(GameType gameType) {
        switch (gameType) {
            case TYPE_OUTLINE_TO_FLAG:
                return 3;
            default:
                return 3;
        }
    }
}
