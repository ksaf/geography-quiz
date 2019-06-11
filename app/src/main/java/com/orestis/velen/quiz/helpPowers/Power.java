package com.orestis.velen.quiz.helpPowers;

import com.google.gson.annotations.Expose;

public class Power {

    @Expose
    private int powerLevel;
    @Expose
    private int charges;
    @Expose
    private String powerType;
    @Expose
    private boolean available;

    public Power(){}

    public Power(int powerLevel, int charges, String powerType, boolean available) {
        this.powerLevel = powerLevel;
        this.charges = charges;
        this.powerType = powerType;
        this.available = available;
    }

    public int getPowerLevel() {
        return powerLevel;
    }

    public int getCharges() {
        return charges;
    }

    public String getPowerType() {
        return powerType;
    }

    public boolean isAvailable() {
        return available;
    }
}
