package com.orestis.velen.quiz.repositories;

import com.orestis.velen.quiz.geography.GeographyDataRepository;
import com.orestis.velen.quiz.geography.GeographyMediaRepository;

public class RepositoryFactory {
    private GameTheme gameTheme;

    public RepositoryFactory(GameTheme gameTheme) {
        this.gameTheme = gameTheme;
    }

    public DataRepository getDataRepository() {
        switch (gameTheme) {
            case GEOGRAPHY:
                return new GeographyDataRepository();
            default:
                return new GeographyDataRepository();
        }
    }

    public MediaRepository getMediaRepository() {
        switch (gameTheme) {
            case GEOGRAPHY:
                return new GeographyMediaRepository();
            default:
                return new GeographyMediaRepository();
        }
    }
}
