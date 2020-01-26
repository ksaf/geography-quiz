package com.orestis.velen.quiz.application;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.multidex.MultiDex;

public class QuizApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            MultiDex.install(this);
        }
    }
}
