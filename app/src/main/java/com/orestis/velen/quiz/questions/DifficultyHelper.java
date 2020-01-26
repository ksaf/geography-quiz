package com.orestis.velen.quiz.questions;

import android.content.Intent;

public class DifficultyHelper {

    public static Intent addDifficultyToIntent(Intent intent, Difficulty difficulty) {
        intent.putExtra("intentDifficulty", difficulty);
        return intent;
    }

    public static Difficulty getDifficultyFromIntent(Intent intent) {
        return (Difficulty) intent.getSerializableExtra("intentDifficulty");
    }
}
