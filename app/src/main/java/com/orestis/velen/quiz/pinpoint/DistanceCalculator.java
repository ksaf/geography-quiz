package com.orestis.velen.quiz.pinpoint;

public class DistanceCalculator {

    private PinpointAnswerGivenListener answerGivenListener;
    private float touchX;
    private float touchY;
    private float correctX;
    private float correctY;
    private int mapSize;
    private double PERFECT_PERCENTAGE = 0.035;
    private double CLOSE_PERCENTAGE = 0.09;
    private boolean isPerfect;
    private boolean isClose;

    public DistanceCalculator(float touchX, float touchY, float correctX, float correctY, int mapSize, PinpointAnswerGivenListener answerGivenListener) {
        this.touchX = touchX;
        this.touchY = touchY;
        this.correctX = correctX;
        this.correctY = correctY;
        this.mapSize = mapSize;
        this.answerGivenListener = answerGivenListener;
    }

    public void calculate() {
        double distance = Math.sqrt(Math.pow(touchX - correctX, 2) + Math.pow(touchY - correctY, 2));
        double distancePercentage = distance / mapSize;
        if(distancePercentage <= PERFECT_PERCENTAGE) {
            isPerfect = true;
        } if(distancePercentage <= CLOSE_PERCENTAGE) {
            isClose = true;
        }
    }

    public String getDistanceString(boolean hasShield) {
        if(isPerfect) {
            answerGivenListener.onPerfectAnswerGiven();
            return "Perfect!";
        } if(isClose) {
            answerGivenListener.onCloseAnswerGiven();
            return "Close!";
        } else {
            if(!hasShield) {
                answerGivenListener.onFarAnswerGiven();
            }
            return "Far!";
        }
    }

    public double getTouchCircleRadius(int resultMapSize) {
        double resultToOriginalMapRatio = (double) resultMapSize / (double) mapSize;
        if(isPerfect) {
            return PERFECT_PERCENTAGE * mapSize * resultToOriginalMapRatio;
        } if(isClose) {
            return CLOSE_PERCENTAGE * mapSize * resultToOriginalMapRatio;
        } else {
            return CLOSE_PERCENTAGE * mapSize * resultToOriginalMapRatio;
        }
    }
}
