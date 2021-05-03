package com.orestis.velen.quiz.gameStartingLoading;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.loadingScreen.BounceLoadingView;

public class GameStartingFragment extends Fragment implements GameStartingEndListener {

    private BounceLoadingView bounceLoading;
    private TextView countText;
    private Typeface face;
    private GameStartingEndListener endListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_starting, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        bounceLoading = view.findViewById(R.id.bounceLoading);
        countText = view.findViewById(R.id.countDown);
        enableBounceLoading();
    }

    private void enableBounceLoading() {
        countText.setTypeface(face);
        countText.setVisibility(View.VISIBLE);
        bounceLoading.setVisibility(View.VISIBLE);
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, metrics);
        for(int i = 1; i <= 11; i++) {
            int id = getContext().getResources().getIdentifier("landmark" + i, "drawable", getContext().getPackageName());
            Bitmap bm = BitmapFactory.decodeResource(getContext().getResources(), id);
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

    @Override
    public void onGameStartingScreenEnd() {
        stopBounceLoading();
        closeFragment();
        endListener.onGameStartingScreenEnd();
    }

    private void closeFragment() {
        if(getActivity() != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.fade_out_animation, R.anim.fade_out_animation);
            ft.remove(GameStartingFragment.this).commit();
        }
    }

    public static class Builder {

        private Typeface face;
        private GameStartingEndListener endListener;

        public Builder useTypeface(Typeface face) {
            this.face = face;
            return this;
        }

        public Builder withGameStartingEndListener(GameStartingEndListener endListener) {
            this.endListener = endListener;
            return this;
        }

        public GameStartingFragment build() {
            GameStartingFragment gameStartingFragment = new GameStartingFragment();
            gameStartingFragment.face = this.face;
            gameStartingFragment.endListener = this.endListener;
            return gameStartingFragment;
        }
    }
}
