package com.orestis.velen.quiz.pinpoint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.ImageView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.geography.Coordinates;
import com.orestis.velen.quiz.questions.QuestionHandler;

import java.lang.ref.WeakReference;

public class CorrectPointTask extends AsyncTask {

    private int displayAnswerDuration;
    private QuestionHandler questionHandler;
    private Coordinates correctCoordinates;
    private WeakReference<ImageView> map;
    private WeakReference<Context> context;

    public CorrectPointTask(int displayAnswerDuration, QuestionHandler questionHandler, ImageView map, Context context) {
        this.displayAnswerDuration = displayAnswerDuration;
        this.questionHandler = questionHandler;
        this.map = new WeakReference<>(map);
        this.context = new WeakReference<>(context);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        SystemClock.sleep(displayAnswerDuration);
        return null;
    }

    @Override
    protected void onPreExecute() {
        correctCoordinates = questionHandler.getQuestion().getCorrectAnswer().getCoordinates();
        drawCorrectSpot(getCorrectRelativeX(), getCorrectRelativeY(), R.drawable.map_pointer_green);
    }

    @Override
    protected void onPostExecute(Object o) {
        questionHandler.nextQuestion();
    }

    private void drawCorrectSpot(float positionX, float positionY, int resourceId) {
        Bitmap mapBitmap = ((BitmapDrawable)map.get().getDrawable()).getBitmap();
        int mapWidth = mapBitmap.getWidth();
        int mapHeight = mapBitmap.getHeight();
        int ivMapWidth = map.get().getWidth();
        int ivMapHeight = map.get().getHeight();
        float relativeX =  mapWidth / (float)ivMapWidth;
        float relativeY = mapHeight / (float)ivMapHeight;
        Bitmap resultBitmap = Bitmap.createBitmap(mapBitmap.getWidth(),mapBitmap.getHeight(), mapBitmap.getConfig());

        Canvas canvas = new Canvas(resultBitmap);

        Bitmap pointer = BitmapFactory.decodeResource(context.get().getResources(), resourceId);
        int pointerWidth = mapWidth / 12;
        int pointerHeight = (int)(pointerWidth * 1.41);
        canvas.drawBitmap(mapBitmap, new Matrix(), null);
        canvas.drawBitmap( Bitmap.createScaledBitmap(pointer, pointerWidth, pointerHeight, false),
                positionX * relativeX - (pointerWidth / 2), positionY * relativeY - pointerHeight, null);
        map.get().setImageBitmap(resultBitmap);
    }

    private float getCorrectRelativeX() {
        return correctCoordinates.getWidthPercentage() * map.get().getWidth();
    }

    private float getCorrectRelativeY() {
        return correctCoordinates.getHeightPercentage() * map.get().getHeight();
    }

}
