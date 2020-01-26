package com.orestis.velen.quiz.mainMenu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.player.PlayerSession;
import com.orestis.velen.quiz.questions.Difficulty;
import com.orestis.velen.quiz.sound.SoundPoolHelper;

import static com.orestis.velen.quiz.mainMenu.MainMenuActivity.XP_BOOST_DURATION;

public class GameStartConfirmationFragment extends Fragment {

    private ImageView darkBg;
    private String gameType;
    private GameStartRequestListener gameStartRequestListener;
    private Class activityToStart;
    private SparseArray<String> difficultiesTxt = new SparseArray<>();
    private SparseArray<Difficulty> difficulties = new SparseArray<>();
    private int difficultySelected = 1;
    private SoundPoolHelper soundHelper;
    private boolean xpBoostEnabled;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_start_confirmation_fragment, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        darkBg.setVisibility(View.VISIBLE);
        Button closeBtn = view.findViewById(R.id.close_game_start_confirmation);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFragment();
            }
        });
        TextView beginGameTypeText = view.findViewById(R.id.beginGameTypeText);
        beginGameTypeText.setText(Html.fromHtml(getString(R.string.startGameOf) + " <br><b><font color='#000000'>" + gameType + "</font></b>?"));

        Button gameStart = view.findViewById(R.id.game_start);
        gameStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundHelper.playMenuBtnOpenSound();
                gameStartRequestListener.onGameStartConfirm(activityToStart, difficulties.get(difficultySelected), xpBoostEnabled);
            }
        });

        difficultiesTxt.put(1, getString(R.string.easy));
        difficultiesTxt.put(2, getString(R.string.normal));
        difficultiesTxt.put(3, getString(R.string.hard));

        difficulties.put(1, Difficulty.EASY);
        difficulties.put(2, Difficulty.NORMAL);
        difficulties.put(3, Difficulty.HARD);

        final TextView difficultyLevelTxt = view.findViewById(R.id.difficultyLevel);
        difficultyLevelTxt.setText(difficultiesTxt.get(difficultySelected));
        Button leftDifficultyBtn = view.findViewById(R.id.leftDifficultyBtn);
        Button rightDifficultyBtn = view.findViewById(R.id.rightDifficultyBtn);

        leftDifficultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundHelper.playMenuBtnOpenSound();
                if(difficultySelected > 1) {
                    difficultySelected--;
                    difficultyLevelTxt.setText(difficultiesTxt.get(difficultySelected));
                }
            }
        });

        rightDifficultyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundHelper.playMenuBtnOpenSound();
                if(difficultySelected < 3) {
                    difficultySelected++;
                    difficultyLevelTxt.setText(difficultiesTxt.get(difficultySelected));
                }
            }
        });

        TextView gameStartXpBoostEnabledTxt = view.findViewById(R.id.gameStartXpBoostEnabledTxt);
        if(PlayerSession.getInstance().getCurrentPlayer().hasXpBoostEnabled(XP_BOOST_DURATION)){
            gameStartXpBoostEnabledTxt.setVisibility(View.VISIBLE);
            xpBoostEnabled = true;
        } else {
            gameStartXpBoostEnabledTxt.setVisibility(View.GONE);
            xpBoostEnabled = false;
        }
    }

    private void closeFragment() {
        soundHelper.playMenuBtnCloseSound();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(GameStartConfirmationFragment.this).commit();
        darkBg.setVisibility(View.GONE);
    }

    public static class Builder {
        private ImageView darkBg;
        private String gameType;
        private GameStartRequestListener gameStartRequestListener;
        private Class activityToStart;
        private SoundPoolHelper soundHelper;

        public Builder withDarkBg(ImageView darkBg) {
            this.darkBg = darkBg;
            return this;
        }

        public Builder forGameType(String gameType) {
            this.gameType = gameType;
            return this;
        }

        public Builder useGameStartRequestListener(GameStartRequestListener gameStartRequestListener) {
            this.gameStartRequestListener = gameStartRequestListener;
            return this;
        }

        public Builder forActivityToStart(Class activityToStart) {
            this.activityToStart = activityToStart;
            return this;
        }

        public Builder withSoundPoolHelper(SoundPoolHelper soundHelper) {
            this.soundHelper = soundHelper;
            return this;
        }

        public GameStartConfirmationFragment build() {
            GameStartConfirmationFragment gameStartConfirmationFragment = new GameStartConfirmationFragment();
            gameStartConfirmationFragment.darkBg = darkBg;
            gameStartConfirmationFragment.gameType = gameType;
            gameStartConfirmationFragment.gameStartRequestListener = gameStartRequestListener;
            gameStartConfirmationFragment.activityToStart = activityToStart;
            gameStartConfirmationFragment.soundHelper = soundHelper;
            return gameStartConfirmationFragment;
        }
    }
}
