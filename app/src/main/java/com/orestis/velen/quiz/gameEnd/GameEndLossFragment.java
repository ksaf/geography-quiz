package com.orestis.velen.quiz.gameEnd;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.adverts.FinishGameWithAdHandler;
import com.orestis.velen.quiz.mainMenu.MainMenuActivity;
import com.orestis.velen.quiz.player.Player;
import com.orestis.velen.quiz.player.PlayerHelper;
import com.orestis.velen.quiz.player.PlayerSession;
import com.orestis.velen.quiz.questions.Difficulty;
import com.orestis.velen.quiz.questions.DifficultyHelper;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

public class GameEndLossFragment extends Fragment {

    private Class activityToRestart;
    private Player player;
    private ImageView darkBg;
    private Difficulty difficulty;
    private SoundPoolHelper soundHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_end_loss_fragment, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Button retryBtn = view.findViewById(R.id.retryBtn);
        Button finishBtn = view.findViewById(R.id.finishBtn);
        darkBg.setVisibility(View.VISIBLE);

        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = (int) pixels;
        view.requestLayout();

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundHelper.playInGameMenuSelectSound();
                Intent intent = new Intent(GameEndLossFragment.this.getActivity(), activityToRestart);
                intent = PlayerHelper.addPlayerToIntent(intent, player);
                intent = DifficultyHelper.addDifficultyToIntent(intent, difficulty);
                GameEndLossFragment.this.getActivity().startActivity(intent);
                GameEndLossFragment.this.getActivity().finish();
                GameEndLossFragment.this.getActivity().overridePendingTransition(0, 0);
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundHelper.playInGameMenuSelectSound();
                FinishGameWithAdHandler.finishGame(new FinishGameWithAdHandler.ExitGameListener() {
                    @Override
                    public void onExitGame() {
                        goToMainMenu();
                    }
                });
            }
        });
    }

    private void goToMainMenu() {
        Intent intent = new Intent(GameEndLossFragment.this.getActivity(), MainMenuActivity.class);
        PlayerSession.getInstance().setRecoveredPlayer(player);
        GameEndLossFragment.this.getActivity().startActivity(intent);
        GameEndLossFragment.this.getActivity().finish();
        GameEndLossFragment.this.getActivity().overridePendingTransition(0, 0);
    }

    public static class Builder {

        private Player player;
        private Class activityToRestart;
        private ImageView darkBg;
        private Difficulty difficulty;
        private SoundPoolHelper soundHelper;

        public Builder forPlayer(Player player) {
            this.player = player;
            return this;
        }

        public Builder restartActivity(Class activityToRestart) {
            this.activityToRestart = activityToRestart;
            return this;
        }

        public Builder withDarkBg(ImageView darkBg) {
            this.darkBg = darkBg;
            return this;
        }

        public Builder forDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public Builder withSoundPoolHelper(SoundPoolHelper soundHelper) {
            this.soundHelper = soundHelper;
            return this;
        }

        public GameEndLossFragment build() {
            GameEndLossFragment gameEndLossFragment = new GameEndLossFragment();
            gameEndLossFragment.player = player;
            gameEndLossFragment.activityToRestart = activityToRestart;
            gameEndLossFragment.darkBg = darkBg;
            gameEndLossFragment.difficulty = difficulty;
            gameEndLossFragment.soundHelper = soundHelper;
            return gameEndLossFragment;
        }
    }
}
