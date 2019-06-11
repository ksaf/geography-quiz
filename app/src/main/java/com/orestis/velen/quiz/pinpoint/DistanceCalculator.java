package com.orestis.velen.quiz.pinpoint;

public class DistanceCalculator {

    private PinpointAnswerGivenListener answerGivenListener;

    public DistanceCalculator(PinpointAnswerGivenListener answerGivenListener) {
        this.answerGivenListener = answerGivenListener;
    }

    public String getDistance(float touchX, float touchY, float correctX, float correctY, int mapSize) {
        double distance = Math.sqrt(Math.pow(touchX - correctX, 2) + Math.pow(touchY - correctY, 2));
        double distancePercentage = distance / mapSize;
        if(distancePercentage <= 0.05) {
            answerGivenListener.onPerfectAnswerGiven();
            return "Perfect!";
        } if(distancePercentage <= 0.15) {
            answerGivenListener.onCloseAnswerGiven();
            return "Close!";
        } else {
            answerGivenListener.onFarAnswerGiven();
            return "Far!";
        }
    }
}
