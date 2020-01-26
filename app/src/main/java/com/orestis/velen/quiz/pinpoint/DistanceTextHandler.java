package com.orestis.velen.quiz.pinpoint;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.orestis.velen.quiz.R;

public class DistanceTextHandler implements DisplayDistanceDurationEndListener{

    private TextView distanceText;
    private int displayDistanceDuration;
    private DisplayDistanceDurationEndListener endListener;
    private String distance;
    private boolean hasShield;

    public DistanceTextHandler(int displayDistanceDuration, TextView distanceText, DisplayDistanceDurationEndListener displayDistanceEndListener) {
        this.distanceText = distanceText;
        this.endListener = displayDistanceEndListener;
        this.displayDistanceDuration = displayDistanceDuration;
    }

    public void startShowDistanceTask(String distance, boolean hasShield) {
        this.distance = distance;
        this.hasShield = hasShield;
        distanceText.setVisibility(View.VISIBLE);
        distanceText.setText(distance);
        distanceText.setBackgroundResource(getBackgroundResource(distance));
        ShowDistanceTask task = new ShowDistanceTask(displayDistanceDuration, this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void hideDistance() {
        distanceText.setVisibility(View.INVISIBLE);
    }

    private int getBackgroundResource(String distance) {
        if(distance.toLowerCase().equals("perfect!")) {
            return R.drawable.distance_text_perfect_background;
        } else if(distance.toLowerCase().equals("close!")) {
            return R.drawable.distance_text_close_background;
        } else {
            return R.drawable.distance_text_far_background;
        }
    }

    @Override
    public void onDisplayDistanceDurationEnd() {
        hideDistance();
        if(!(distance.toLowerCase().equals("far!") && hasShield)) {
            endListener.onDisplayDistanceDurationEnd();
        }
    }
}
