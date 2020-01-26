package com.orestis.velen.quiz.geography;

import android.content.Context;
import android.support.v4.util.Pair;
import android.util.Log;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.questions.Difficulty;

public class DataItem {

    public static final int NORTH_AMERICA = 1;
    public static final int SOUTH_AMERICA = 2;
    public static final int EUROPE = 3;
    public static final int EAST_ASIA = 4;
    public static final int WEST_ASIA = 5;
    public static final int AUSTRALIA = 6;
    public static final int AFRICA = 7;

    public static final int TYPE_COUNTRY = 8;
    public static final int TYPE_CAPITAL = 9;
    public static final int TYPE_MONUMENT = 10;

    private String itemCode;
    private Pair<Float, Float> coordinates;
    private int itemType;
    private int continent;
    private Difficulty difficulty;
    private String linkItemCode;

    public DataItem(String itemCode, Pair<Float, Float> coordinates, int continent, Difficulty difficulty, int itemType) {
        this.itemCode = itemCode;
        this.coordinates = coordinates;
        this.continent = continent;
        this.difficulty = difficulty;
        this.itemType = itemType;
    }

    public DataItem(String itemCode, Pair<Float, Float> coordinates, int continent, Difficulty difficulty, int itemType, String linkItemCode) {
        this(itemCode, coordinates, continent, difficulty, itemType);
        this.linkItemCode = linkItemCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public String getLinkItemCode() {
        return linkItemCode;
    }

    public Coordinates getCoordinates() {
        return new Coordinates(coordinates);
    }

    public int getContinent() {
        return continent;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getItemType() {
        return itemType;
    }

    public int getItemDrawableId(Context context, boolean isFlag) {
        String postFix = isFlag ? "_flag" : "";
        return getResourceId(itemCode + postFix, "drawable", context.getPackageName(), context);
    }

    public String getStringResource(Context context) {
        return context.getString(getResourceId(itemCode, "string", context.getPackageName(), context));
    }

    public int getMapDrawbleId() {
        switch (continent) {
            case NORTH_AMERICA:
                return R.drawable.north_america_map_2;
            case SOUTH_AMERICA:
                return R.drawable.south_america_map_2;
            case EUROPE:
                return R.drawable.europe_map_2;
            case EAST_ASIA:
                return R.drawable.east_asia_map_2;
            case WEST_ASIA:
                return R.drawable.west_asia_map_2;
            case AUSTRALIA:
                return R.drawable.australia_map_2;
            case AFRICA:
                return R.drawable.africa_map_2;
            default:
                return 0;
        }
    }

    private int getResourceId(String pVariableName, String pResourcename, String pPackageName, Context context) {
        try {
            System.out.println("Found " + pResourcename + " with code: " + itemCode);
            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            Log.d("ResourceError", "Could not find " + pResourcename + " with code: " + itemCode);
            e.printStackTrace();
            return -1;
        }
    }
}
