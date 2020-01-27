package com.orestis.velen.quiz.mainMenu;

public class GameVariationInfo {
    private final Class activityToStart;
    private final int variationIconResource;
    private final String variationDescription;

    public GameVariationInfo(Class activityToStart, int variationIconResource, String variationDescription) {
        this.activityToStart = activityToStart;
        this.variationIconResource = variationIconResource;
        this.variationDescription = variationDescription;
    }

    public Class getActivityToStart() {
        return activityToStart;
    }

    public int getVariationIconResource() {
        return variationIconResource;
    }

    public String getVariationDescription() {
        return variationDescription;
    }
}
