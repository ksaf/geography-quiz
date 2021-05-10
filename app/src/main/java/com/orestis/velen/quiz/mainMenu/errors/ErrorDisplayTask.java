package com.orestis.velen.quiz.mainMenu.errors;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.orestis.velen.quiz.R;

import java.lang.ref.WeakReference;

public class ErrorDisplayTask extends AsyncTask{

    private int placeholderId;
    private WeakReference<AppCompatActivity> activity;
    private ConnectionErrorFragment errorFragment;
    private int duration;
    private ErrorDisplayListener errorDisplayListener;

    public ErrorDisplayTask(int placeholderId, AppCompatActivity activity, ConnectionErrorFragment errorFragment, int duration, ErrorDisplayListener errorDisplayListener) {
        this.placeholderId = placeholderId;
        this.activity = new WeakReference<>(activity);
        this.errorFragment = errorFragment;
        this.duration = duration;
        this.errorDisplayListener = errorDisplayListener;
    }

    @Override
    protected void onPreExecute() {
        showError();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        SystemClock.sleep(duration);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        hideError();
    }

    private void showError() {
        FragmentTransaction ft = activity.get().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.bounce_down_animation, R.anim.bounce_down_animation);
        ft.replace(placeholderId, errorFragment);
        ft.commit();
    }

    private void hideError() {
        FragmentTransaction ft = activity.get().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_up_animation, R.anim.slide_up_animation);
        ft.remove(errorFragment).commit();
        errorDisplayListener.onErrorIsGone();
    }
}
