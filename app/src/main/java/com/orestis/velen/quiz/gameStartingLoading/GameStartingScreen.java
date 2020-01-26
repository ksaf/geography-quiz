package com.orestis.velen.quiz.gameStartingLoading;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.loadingScreen.BounceLoadingView;

public class GameStartingScreen implements GameStartingEndListener {

    private BounceLoadingView bounceLoading;
    private TextView countText;
    private ViewGroup container;
    private Typeface face;
    private Context context;
    private GameStartingEndListener endListener;

    private GameStartingScreen() {}

    public void init() {
        setViewsScale(0f);
        enableBounceLoading();
    }

    private void setViewsScale(float scale) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View view = container.getChildAt(i);
            if(view.getId() != R.id.gameStarting && view.getId() != R.id.background) {
                view.setScaleX(scale);
                view.setScaleY(scale);
            } else if (view.getId() == R.id.gameStarting) {
                int visibility = scale == 0 ? View.VISIBLE : View.GONE;
                view.setVisibility(visibility);
            }
        }
    }

    private void setViewsAlpha(float alpha) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View view = container.getChildAt(i);
            if(view.getId() != R.id.gameStarting && view.getId() != R.id.background) {
                view.setAlpha(alpha);
            }
        }
    }

    private void enableBounceLoading() {
        countText.setTypeface(face);
        countText.setVisibility(View.VISIBLE);
        bounceLoading.setVisibility(View.VISIBLE);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, metrics);
        for(int i = 1; i <= 11; i++) {
            int id = context.getResources().getIdentifier("landmark" + i, "drawable", context.getPackageName());
            Bitmap bm = BitmapFactory.decodeResource(context.getResources(), id);
            bm = Bitmap.createScaledBitmap(bm, size, size, false);
            bounceLoading.addBitmap(bm);
        }
        bounceLoading.shuffleBitmaps();
        bounceLoading.setShadowColor(Color.BLACK);
        bounceLoading.setDuration(1000);
        bounceLoading.start();

        CountDownText countDownText = new CountDownText(countText, 3000, 1000, this);
        countDownText.start();
    }

    private void stopBounceLoading() {
        countText.setVisibility(View.INVISIBLE);
        bounceLoading.stop();
        bounceLoading.setVisibility(View.INVISIBLE);
    }

    private void animateUIAppearance() {
        for (int i = 0; i < container.getChildCount(); i++) {
            View view = container.getChildAt(i);
            ViewPropertyAnimatorCompat viewAnimator;

            viewAnimator = ViewCompat.animate(view)
                    .setStartDelay(50)
                    .alpha(1f)
                    .setDuration(400);

            viewAnimator.setInterpolator(new LinearInterpolator()).start();
        }
    }

    @Override
    public void onGameStartingScreenEnd() {
        stopBounceLoading();
        setViewsScale(1f);
        setViewsAlpha(0f);
        animateUIAppearance();
        endListener.onGameStartingScreenEnd();
    }

    public static class Builder {

        private BounceLoadingView bounceLoading;
        private TextView countText;
        private ViewGroup container;
        private Typeface face;
        private Context context;
        private GameStartingEndListener endListener;

        public Builder useBounceLoadingView(BounceLoadingView bounceLoading) {
            this.bounceLoading = bounceLoading;
            return this;
        }

        public Builder useCountText(TextView countText) {
            this.countText = countText;
            return this;
        }

        public Builder useTypeface(Typeface face) {
            this.face = face;
            return this;
        }

        public Builder forContainer(ViewGroup container) {
            this.container = container;
            return this;
        }

        public Builder withContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder withGameStartingEndListener(GameStartingEndListener endListener) {
            this.endListener = endListener;
            return this;
        }

        public GameStartingScreen init() {
            GameStartingScreen gameStartingScreen = new GameStartingScreen();
            gameStartingScreen.bounceLoading = this.bounceLoading;
            gameStartingScreen.countText = this.countText;
            gameStartingScreen.container = this.container;
            gameStartingScreen.face = this.face;
            gameStartingScreen.context = this.context;
            gameStartingScreen.endListener = this.endListener;
            gameStartingScreen.init();
            return gameStartingScreen;
        }
    }
}
