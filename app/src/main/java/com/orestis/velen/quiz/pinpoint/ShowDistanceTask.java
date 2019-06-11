package com.orestis.velen.quiz.pinpoint;

import android.os.AsyncTask;
import android.os.SystemClock;

public class ShowDistanceTask extends AsyncTask{

    private int displayDistanceDuration;
    private DisplayDistanceDurationEndListener endListener;

    public ShowDistanceTask(int displayDistanceDuration, DisplayDistanceDurationEndListener endListener) {
        this.displayDistanceDuration = displayDistanceDuration;
        this.endListener = endListener;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        SystemClock.sleep(displayDistanceDuration);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        endListener.onDisplayDistanceDurationEnd();
    }
}
