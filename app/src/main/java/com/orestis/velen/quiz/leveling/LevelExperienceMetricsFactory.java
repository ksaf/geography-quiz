package com.orestis.velen.quiz.leveling;

import com.orestis.velen.quiz.geography.GeographyLevelExperienceMetrics;
import com.orestis.velen.quiz.repositories.GameTheme;

public class LevelExperienceMetricsFactory {

    public LevelExperienceMetrics getLevelExperienceMetrics(GameTheme gameTheme) {
        switch (gameTheme) {
            case GEOGRAPHY:
                return new GeographyLevelExperienceMetrics();
            default:
                return new GeographyLevelExperienceMetrics();
        }
    }
}
