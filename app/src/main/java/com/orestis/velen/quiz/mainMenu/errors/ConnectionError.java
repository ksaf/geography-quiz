package com.orestis.velen.quiz.mainMenu.errors;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

public class ConnectionError implements ErrorDisplayListener{

    private AppCompatActivity activity;
    private ConnectionErrorFragment errorFragment;
    private boolean errorIsDisplayed;

    public ConnectionError(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void show(int duration, int placeholderId, String errorMessage) {
        if(!errorIsDisplayed) {
            errorIsDisplayed = true;
            errorFragment = ConnectionErrorFragment.newInstance(errorMessage);
            ErrorDisplayTask errorTask = new ErrorDisplayTask(placeholderId, activity, errorFragment, duration, this);
            errorTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public void onErrorIsGone() {
        errorIsDisplayed = false;
    }
}
