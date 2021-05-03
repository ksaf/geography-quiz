package com.orestis.velen.quiz.mainMenu;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

public class SignedOutFragment extends Fragment {

    private SignInRequestHandler signInRequestHandler;
    private SoundPoolHelper soundHelper;
    private Rect rect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.signed_out_fragment, container, false);
        final RelativeLayout signInBtnBg = rootView.findViewById(R.id.signInBtnBg);
        Button signInBtn = rootView.findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInRequestHandler.onGoogleSignInRequested();
                soundHelper.playMenuBtnOpenSound();
            }
        });
        signInBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                        signInBtnBg.setBackgroundResource(R.drawable.sign_in_button_pressed);
                        break;
                    case MotionEvent.ACTION_UP:
                        signInBtnBg.setBackgroundResource(R.drawable.sign_in_button_default);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(rect != null && !rect.contains(view.getLeft() + (int) motionEvent.getX(), view.getTop() + (int) motionEvent.getY())){
                            // User moved outside bounds
                            signInBtnBg.setBackgroundResource(R.drawable.sign_in_button_default);
                        }
                        break;
                }
                return false;
            }
        });
        return rootView;
    }

    public static class Builder {
        private SignInRequestHandler signInRequestHandler;
        private SoundPoolHelper soundHelper;

        public Builder withSignInRequestHandler(SignInRequestHandler signInRequestHandler) {
            this.signInRequestHandler = signInRequestHandler;
            return this;
        }

        public Builder withSoundPoolHelper(SoundPoolHelper soundHelper) {
            this.soundHelper = soundHelper;
            return this;
        }

        public SignedOutFragment build() {
            SignedOutFragment signedOutFragment = new SignedOutFragment();
            signedOutFragment.signInRequestHandler = signInRequestHandler;
            signedOutFragment.soundHelper = soundHelper;
            return signedOutFragment;
        }
    }
}
