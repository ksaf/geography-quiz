package com.orestis.velen.quiz.answerButtons;

import android.util.TypedValue;
import android.widget.Button;

import java.util.HashMap;

import static com.orestis.velen.quiz.answerButtons.AnswerChoice.A;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.B;
import static com.orestis.velen.quiz.answerButtons.AnswerChoice.C;

public class AnswerTextSizeHandler {

    private HashMap<AnswerChoice, Button> buttons;
    private HashMap<AnswerChoice, String> answers;

    public AnswerTextSizeHandler(HashMap<AnswerChoice, Button> buttons) {
        this.buttons = buttons;
    }

    public void adjustTextSizeIfNeeded() {
        buttons.get(A).post(new Runnable() {
            @Override
            public void run() {
                if(buttons.get(A).getLineCount()>1){buttons.get(A).setTextSize(TypedValue.COMPLEX_UNIT_SP,20);}
            }
        });
        buttons.get(B).post(new Runnable() {
            @Override
            public void run() {
                if(buttons.get(B).getLineCount()>1){buttons.get(B).setTextSize(TypedValue.COMPLEX_UNIT_SP,20);}
            }
        });
        buttons.get(C).post(new Runnable() {
            @Override
            public void run() {
                if(buttons.get(C).getLineCount()>1){buttons.get(C).setTextSize(TypedValue.COMPLEX_UNIT_SP,20);}
            }
        });
    }
}
