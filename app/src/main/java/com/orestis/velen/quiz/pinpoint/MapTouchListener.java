package com.orestis.velen.quiz.pinpoint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.geography.Coordinates;
import com.orestis.velen.quiz.helpPowers.shield.ShieldEndListener;
import com.orestis.velen.quiz.questions.Question;
import com.orestis.velen.quiz.questions.QuestionChangedListener;
import com.orestis.velen.quiz.questions.QuestionHandler;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

public class MapTouchListener implements View.OnTouchListener, AnswerPointDelayEndListener, QuestionChangedListener {

    private int delayAnswerDuration;
    private Context context;
    private ImageView map;
    private Bitmap resultBitmap;
    private Coordinates correctCoordinates;
    private DistanceTextHandler distanceTextHandler;
    private QuestionHandler questionHandler;
    private PinpointAnswerGivenListener answerGivenListener;
    private float touchX;
    private float touchY;
    private boolean hasShield;
    private ShieldEndListener shieldEndListener;
    private SoundPoolHelper soundHelper;
    private DistanceCalculator distanceCalculator;

    private MapTouchListener() {}

    private void init() {
        questionHandler.registerQuestionChangedListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        map.onTouchEvent(motionEvent);

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP:
                touchX = motionEvent.getX();
                touchY = motionEvent.getY();

                distanceCalculator = new DistanceCalculator(touchX, touchY, getCorrectRelativeX(), getCorrectRelativeY(), map.getWidth(), answerGivenListener);
                distanceCalculator.calculate();

                //draw answer pointer
                drawTouchedSpot(touchX, touchY, R.drawable.map_pointer_red, false);

                startShowCorrectLocationTask(delayAnswerDuration);
                break;
        }

        return true;
    }

    private void drawTouchedSpot(float positionX, float positionY, int resourceId, boolean isTheSolutionPointer) {
        Bitmap mapBitmap = ((BitmapDrawable)map.getDrawable()).getBitmap();
        int mapWidth = mapBitmap.getWidth();
        int mapHeight = mapBitmap.getHeight();
        int ivMapWidth = map.getWidth();
        int ivMapHeight = map.getHeight();
        float relativeX =  mapWidth / (float)ivMapWidth;
        float relativeY = mapHeight / (float)ivMapHeight;
        resultBitmap = Bitmap.createBitmap(mapBitmap.getWidth(), mapBitmap.getHeight(), mapBitmap.getConfig());

        Canvas canvas = new Canvas(resultBitmap);

        Bitmap pointer = BitmapFactory.decodeResource(context.getResources(), resourceId);
        int pointerWidth = mapWidth / 12;
        int pointerHeight = (int)(pointerWidth * 1.41);
        canvas.drawBitmap(mapBitmap, new Matrix(), null);
        canvas.drawBitmap( Bitmap.createScaledBitmap(pointer, pointerWidth, pointerHeight, false),
                positionX * relativeX - (pointerWidth / 2), positionY * relativeY - pointerHeight, null);
        if(!isTheSolutionPointer) {
            //draw pointed circle
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#CD5C5C"));
            paint.setStyle(Paint.Style.STROKE);
            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, context.getResources().getDisplayMetrics());
            paint.setStrokeWidth(pixels);
            double radius = distanceCalculator.getTouchCircleRadius(resultBitmap.getWidth());
            canvas.drawCircle(positionX * relativeX, positionY * relativeY,
                    (float) radius, paint);
        }
        map.setImageBitmap(resultBitmap);
    }

    private void startShowCorrectLocationTask(int delay) {
        map.setOnTouchListener(null);
        AnswerPointDelayTask task = new AnswerPointDelayTask(delay, this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onAnswerDelayEnd() {
        String distanceString = distanceCalculator.getDistanceString(hasShield);
        distanceTextHandler.startShowDistanceTask(distanceString, hasShield);
        if(distanceString.toLowerCase().equals("far!") && hasShield) {
            //draw shield-break pointer
            drawTouchedSpot(touchX, touchY, R.drawable.map_pointer_red, true);
            shieldEndListener.onExtraTryEnd();
            hasShield = false;
            map.setOnTouchListener(this);
        } else {
            if(distanceString.toLowerCase().equals("far!")) {
                soundHelper.playInGameWrongChoiceSound();
            } else {
                soundHelper.playInGameCorrectChoiceSound();
            }
            //Draw correct Pointer
            drawTouchedSpot(getCorrectRelativeX(), getCorrectRelativeY(), R.drawable.map_pointer_green, true);
        }
    }

    private float getCorrectRelativeX() {
        return correctCoordinates.getWidthPercentage() * map.getWidth();
    }

    private float getCorrectRelativeY() {
        return correctCoordinates.getHeightPercentage() * map.getHeight();
    }

    @Override
    public void onQuestionChanged(Question question) {
        correctCoordinates = question.getCorrectAnswer().getCoordinates();
        map.setImageResource(question.getCorrectAnswer().getMapDrawbleId());
//        map.setOnTouchListener(this);
    }

    public void enableShield(ShieldEndListener shieldEndListener) {
        this.hasShield = true;
        this.shieldEndListener = shieldEndListener;
    }

    public void disableShield() {
        this.hasShield = false;
    }

    public static class Builder {

        private int delayAnswerDuration;
        private Context context;
        private ImageView map;
        private DistanceTextHandler distanceTextHandler;
        private QuestionHandler questionHandler;
        private PinpointAnswerGivenListener answerGivenListener;
        private SoundPoolHelper soundHelper;

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

        public Builder withSoundPoolHelper(SoundPoolHelper soundHelper) {
            this.soundHelper = soundHelper;
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
            mapTouchListener.soundHelper = this.soundHelper;

            mapTouchListener.init();
            return mapTouchListener;
        }
    }
}
