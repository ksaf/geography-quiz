package com.orestis.velen.quiz.pinpoint;

import android.os.AsyncTask;
import android.os.SystemClock;

public class AnswerPointDelayTask extends AsyncTask{

    private int delayAnswerDuration;
    private AnswerPointDelayEndListener delayEndListener;

    public AnswerPointDelayTask(int delayAnswerDuration, AnswerPointDelayEndListener delayEndListener) {
        this.delayAnswerDuration = delayAnswerDuration;
        this.delayEndListener = delayEndListener;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        SystemClock.sleep(delayAnswerDuration);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        delayEndListener.onAnswerDelayEnd();
    }
}
