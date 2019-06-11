package com.orestis.velen.quiz.pinpoint;

public interface PinpointAnswerGivenListener {
    void onCloseAnswerGiven();
    void onPerfectAnswerGiven();
    void onFarAnswerGiven();
}
