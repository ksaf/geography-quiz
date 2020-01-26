package com.orestis.velen.quiz.helpPowers.fiftyFifty;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import com.orestis.velen.quiz.helpPowers.ChargeChangeListener;
import com.orestis.velen.quiz.geography.Coordinates;
import com.orestis.velen.quiz.questions.Question;
import com.orestis.velen.quiz.questions.QuestionHandler;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

import java.util.Random;

public class FiftyFiftyMapClickListener implements View.OnClickListener {

    private QuestionHandler questionHandler;
    private ImageView map;
    private ImageView hideMapOverlay;
    private ChargeChangeListener chargeChangeListener;
    private SoundPoolHelper soundHelper;

    public FiftyFiftyMapClickListener(ImageView map, ImageView hideMapOverlay, ChargeChangeListener chargeChangeListener,
                                      QuestionHandler questionHandler, SoundPoolHelper soundHelper) {
        this.map = map;
        this.hideMapOverlay = hideMapOverlay;
        this.chargeChangeListener = chargeChangeListener;
        this.questionHandler = questionHandler;
        this.soundHelper = soundHelper;
    }

    @Override
    public void onClick(View view) {
        view.setEnabled(false);
        soundHelper.playInGamePowerEnableSound();
        Question question = questionHandler.getQuestion();
        Coordinates correctCoordinates = question.getCorrectAnswer().getCoordinates();
        float correctRelativeX = correctCoordinates.getWidthPercentage() * map.getWidth();
        float correctRelativeY = correctCoordinates.getHeightPercentage() * map.getHeight();
        Bitmap mapBitmap = ((BitmapDrawable)map.getDrawable()).getBitmap();

        int mapWidth = mapBitmap.getWidth();
        int mapHeight = mapBitmap.getHeight();
        int ivMapWidth = map.getWidth();
        int ivMapHeight = map.getHeight();
        float relativeX =  mapWidth / (float)ivMapWidth;
        float relativeY = mapHeight / (float)ivMapHeight;

        Bitmap hideMapOverlayFrame = Bitmap.createBitmap(mapBitmap.getWidth(), mapBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        RectF outerRectangle = new RectF(0, 0, hideMapOverlayFrame.getWidth(),hideMapOverlayFrame.getHeight());
        Canvas hideCanvas = new Canvas(hideMapOverlayFrame);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#333333"));
        hideCanvas.drawRect(outerRectangle, paint);

        paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        float centerX = correctRelativeX * relativeX;
        float centerY = correctRelativeY * relativeY;
        float radius = (float)(hideMapOverlayFrame.getWidth() / 2.8);
        float errorMargin = radius / 4;
        Random rand = new Random();
        float randomX = (centerX - radius + errorMargin) + rand.nextFloat() * (centerX + radius - errorMargin - (centerX - radius + errorMargin));
        float randomY = (centerY - radius + errorMargin) + rand.nextFloat() * (centerY + radius - errorMargin - (centerY - radius + errorMargin));
        hideCanvas.drawCircle(randomX, randomY, radius, paint);
        hideMapOverlay.setVisibility(View.VISIBLE);
        hideMapOverlay.setImageBitmap(hideMapOverlayFrame);
        chargeChangeListener.onChargeDecreased();
    }
}
