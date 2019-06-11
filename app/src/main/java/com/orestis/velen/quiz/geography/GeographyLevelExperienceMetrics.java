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
        metrics.append(10, 10000);
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
            case TYPE_D:
                return 1;
            default:
                return 3;
        }
    }
}
