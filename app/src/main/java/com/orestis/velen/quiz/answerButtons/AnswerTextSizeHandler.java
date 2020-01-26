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
                fixMaxLines(buttons.get(A));
            }
        });
        buttons.get(B).post(new Runnable() {
            @Override
            public void run() {
                fixMaxLines(buttons.get(B));
            }
        });
        buttons.get(C).post(new Runnable() {
            @Override
            public void run() {
                fixMaxLines(buttons.get(C));
            }
        });
    }

    private void withLineCount(Button button) {
        if(button.getLineCount()>1) {
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        }
    }

    private void fixMaxLines(Button button) {
        String btnTxt = (String) button.getText();
        int wordNo = countWords(btnTxt);
        button.setMaxLines(wordNo);
    }

    private int countWords(String s){

        int wordCount = 0;

        boolean word = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }
}
