package com.orestis.velen.quiz.geography;

import android.support.v4.util.Pair;

public class Coordinates {

    private final Pair<Float, Float> coords;

    public Coordinates(Pair<Float, Float> coords) {
        this.coords = coords;
    }

    public float getWidthPercentage() {
        return coords.first;
    }

    public float getHeightPercentage() {
        return coords.second;
    }

}
