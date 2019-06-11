package com.orestis.velen.quiz.leveling;

import com.orestis.velen.quiz.questions.Difficulty;
import com.orestis.velen.quiz.questions.GameType;

public interface LevelExperienceMetrics {
    int getExperienceForLevelUp(int level);
    int getExperienceForCompletion(Difficulty difficulty);
    int getExperienceForTimeLeft(int timeLeftPercentage, Difficulty difficulty);
    int getExperienceForBonus(Difficulty difficulty);
    int getExperienceForPerfectBonus(Difficulty difficulty);
    int getStreakNeededForBonus(GameType gameType);
}
