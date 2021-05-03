package com.orestis.velen.quiz.mainMenu;

import android.graphics.drawable.Drawable;
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

import java.util.ArrayList;
import java.util.List;

import static com.orestis.velen.quiz.mainMenu.MainMenuActivity.XP_BOOST_DURATION;

public class GameStartConfirmationFragment extends Fragment {

    private ImageView darkBg;
    private String gameType;
    private GameStartRequestListener gameStartRequestListener;
    private int viewPagerSelection;
    private SparseArray<String> difficultiesTxt = new SparseArray<>();
    private SparseArray<Difficulty> difficulties = new SparseArray<>();
    private int difficultySelected;
    private SoundPoolHelper soundHelper;
    private boolean xpBoostEnabled;
    private Class activityToStart;
    private List<GameVariationInfo> availableGameTypes;
    private List<ImageView> availableGameTypesImages;

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

        difficultySelected = PlayerSession.getInstance().getDifficultySelection(viewPagerSelection);
        availableGameTypes = VariationSelector.getAvailableVariations(viewPagerSelection, getContext());
        availableGameTypesImages = new ArrayList<>();

        final TextView gameTypeSelectionText = view.findViewById(R.id.gameTypeSelectionText);
        final ImageView gameTypeAImage = view.findViewById(R.id.gameTypeAImage);
        final ImageView gameTypeBImage = view.findViewById(R.id.gameTypeBImage);

        if(availableGameTypes.size() > 0) {
            gameTypeAImage.setImageResource(availableGameTypes.get(0).getVariationIconResource());
            availableGameTypesImages.add(gameTypeAImage);
            gameTypeBImage.setVisibility(View.GONE);

            if(PlayerSession.getInstance().getVariationSelection(viewPagerSelection) == 0) {
                highLightImage(gameTypeAImage);
                activityToStart = availableGameTypes.get(0).getActivityToStart();
                gameTypeSelectionText.setText(availableGameTypes.get(0).getVariationDescription());
            } else {
                highLightImage(gameTypeBImage);
                activityToStart = availableGameTypes.get(1).getActivityToStart();
                gameTypeSelectionText.setText(availableGameTypes.get(1).getVariationDescription());
            }

            if(availableGameTypes.size() == 2) {
                availableGameTypesImages.add(gameTypeBImage);
                gameTypeBImage.setImageResource(availableGameTypes.get(1).getVariationIconResource());
                gameTypeBImage.setVisibility(View.VISIBLE);

                gameTypeAImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        soundHelper.playMenuBtnOpenSound();
                        activityToStart = availableGameTypes.get(0).getActivityToStart();
                        gameTypeSelectionText.setText(availableGameTypes.get(0).getVariationDescription());
                        highLightImage(gameTypeAImage);
                        PlayerSession.getInstance().setVariationSelection(viewPagerSelection, 0);
                    }
                });
                gameTypeBImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        soundHelper.playMenuBtnOpenSound();
                        activityToStart = availableGameTypes.get(1).getActivityToStart();
                        gameTypeSelectionText.setText(availableGameTypes.get(1).getVariationDescription());
                        highLightImage(gameTypeBImage);
                        PlayerSession.getInstance().setVariationSelection(viewPagerSelection, 1);
                    }
                });
            }
        }

        final Button gameStart = view.findViewById(R.id.game_start);
        gameStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundHelper.playMenuBtnOpenSound();
                gameStartRequestListener.onGameStartConfirm(activityToStart, difficulties.get(difficultySelected), xpBoostEnabled);
                gameStart.setOnClickListener(null);
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
                    PlayerSession.getInstance().setDifficultySelection(viewPagerSelection, difficultySelected);
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
                    PlayerSession.getInstance().setDifficultySelection(viewPagerSelection, difficultySelected);
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

    private void highLightImage(ImageView image) {
        for (int i=0; i < availableGameTypesImages.size(); i++) {
            if (availableGameTypesImages.get(i).getBackground() != null) {
                availableGameTypesImages.get(i).setBackground(null);
            }
        }
        Drawable highlight = getResources().getDrawable( R.drawable.game_type_selection_frame);
        image.setBackground(highlight);
    }

    public static class Builder {
        private ImageView darkBg;
        private String gameType;
        private GameStartRequestListener gameStartRequestListener;
        private int viewPagerSelection;
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

        public Builder forViewPagerSelection(int viewPagerSelection) {
            this.viewPagerSelection = viewPagerSelection;
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
            gameStartConfirmationFragment.viewPagerSelection = viewPagerSelection;
            gameStartConfirmationFragment.soundHelper = soundHelper;
            return gameStartConfirmationFragment;
        }
    }
}
