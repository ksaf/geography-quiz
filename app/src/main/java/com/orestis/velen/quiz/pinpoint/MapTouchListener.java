package com.orestis.velen.quiz.pinpoint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.questions.Question;
import com.orestis.velen.quiz.questions.QuestionChangedListener;
import com.orestis.velen.quiz.questions.QuestionHandler;

public class MapTouchListener implements View.OnTouchListener, AnswerPointDelayEndListener, QuestionChangedListener {

    private int delayAnswerDuration;
    private Context context;
    private ImageView map;
    private Coordinates correctCoordinates;
    private DistanceTextHandler distanceTextHandler;
    private QuestionHandler questionHandler;
    private PinpointAnswerGivenListener answerGivenListener;
    private float touchX;
    private float touchY;

    private MapTouchListener() {}

    private void init() {
        questionHandler.registerQuestionChangedListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        map.onTouchEvent(motionEvent);


        touchX = motionEvent.getX();
        touchY = motionEvent.getY();

        drawTouchedSpot(touchX, touchY, R.drawable.map_pointer_red);

        startShowCorrectLocationTask(delayAnswerDuration);

        return false;
    }

    private void drawTouchedSpot(float positionX, float positionY, int resourceId) {
        Bitmap mapBitmap = ((BitmapDrawable)map.getDrawable()).getBitmap();
        int mapWidth = mapBitmap.getWidth();
        int mapHeight = mapBitmap.getHeight();
        int ivMapWidth = map.getWidth();
        int ivMapHeight = map.getHeight();
        float relativeX =  mapWidth / (float)ivMapWidth;
        float relativeY = mapHeight / (float)ivMapHeight;
        Bitmap resultBitmap = Bitmap.createBitmap(mapBitmap.getWidth(),mapBitmap.getHeight(), mapBitmap.getConfig());

        Canvas canvas = new Canvas(resultBitmap);

        Bitmap pointer = BitmapFactory.decodeResource(context.getResources(), resourceId);
        int pointerWidth = mapWidth / 12;
        int pointerHeight = (int)(pointerWidth * 1.41);
        canvas.drawBitmap(mapBitmap, new Matrix(), null);
        canvas.drawBitmap( Bitmap.createScaledBitmap(pointer, pointerWidth, pointerHeight, false),  positionX * relativeX - (pointerWidth / 2), positionY * relativeY - pointerHeight, null);

        map.setImageBitmap(resultBitmap);
    }

    private void startShowCorrectLocationTask(int delay) {
        map.setOnTouchListener(null);
        AnswerPointDelayTask task = new AnswerPointDelayTask(delay, this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onAnswerDelayEnd() {
        float correctRelativeX = correctCoordinates.getWidthPercentage() * map.getWidth();
        float correctRelativeY = correctCoordinates.getHeightPercentage() * map.getHeight();
        drawTouchedSpot(correctRelativeX, correctRelativeY, R.drawable.map_pointer_green);
        DistanceCalculator distanceCalculator = new DistanceCalculator(answerGivenListener);
        String distanceString = distanceCalculator.getDistance(touchX, touchY, correctRelativeX, correctRelativeY, map.getWidth());
        distanceTextHandler.startShowDistanceTask(distanceString);
    }

    @Override
    public void onQuestionChanged(Question question) {
        correctCoordinates = question.getCorrectCoordinates();
        map.setImageResource(question.getCorrectAnswerContinentResource());
        map.setOnTouchListener(this);
    }

    public static class Builder {

        private int delayAnswerDuration;
        private Context context;
        private ImageView map;
        private DistanceTextHandler distanceTextHandler;
        private QuestionHandler questionHandler;
        private PinpointAnswerGivenListener answerGivenListener;

        public Builder forMap(ImageView map) {
            this.map = map;
            return this;
        }

        public Builder withContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder withDelayedAnswerDuration(int delayAnswerDuration) {
            this.delayAnswerDuration = delayAnswerDuration;
            return this;
        }

        public Builder withDistanceTextHandler(DistanceTextHandler distanceTextHandler) {
            this.distanceTextHandler = distanceTextHandler;
            return this;
        }

        public Builder withQuestionHandler(QuestionHandler questionHandler) {
            this.questionHandler = questionHandler;
            return this;
        }

        public Builder withAnswerGivenListener(PinpointAnswerGivenListener answerGivenListener) {
            this.answerGivenListener = answerGivenListener;
            return this;
        }

        public MapTouchListener build() {
            MapTouchListener mapTouchListener = new MapTouchListener();
            mapTouchListener.context = this.context;
            mapTouchListener.map = this.map;
            mapTouchListener.delayAnswerDuration = this.delayAnswerDuration;
            mapTouchListener.distanceTextHandler = this.distanceTextHandler;
            mapTouchListener.questionHandler = this.questionHandler;
            mapTouchListener.answerGivenListener = this.answerGivenListener;

            mapTouchListener.init();
            return mapTouchListener;
        }
    }
}
