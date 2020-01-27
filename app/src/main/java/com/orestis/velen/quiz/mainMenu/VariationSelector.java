package com.orestis.velen.quiz.mainMenu;

import android.content.Context;

import com.orestis.velen.quiz.R;
import com.orestis.velen.quiz.capitals.CapitalsActivity;
import com.orestis.velen.quiz.flags.FlagsActivity;
import com.orestis.velen.quiz.outlines.MainActivity;
import com.orestis.velen.quiz.outlinesToFlags.OutlinesToFlagsActivity;
import com.orestis.velen.quiz.pinpoint.CapitalsPointActivity;
import com.orestis.velen.quiz.pinpoint.MonumentsPointActivity;

import java.util.ArrayList;
import java.util.List;

import static com.orestis.velen.quiz.mainMenu.MainMenuActivity.VIEW_PAGER_SELECTION_CAPITALS;
import static com.orestis.velen.quiz.mainMenu.MainMenuActivity.VIEW_PAGER_SELECTION_FLAGS;
import static com.orestis.velen.quiz.mainMenu.MainMenuActivity.VIEW_PAGER_SELECTION_MONUMENTS;
import static com.orestis.velen.quiz.mainMenu.MainMenuActivity.VIEW_PAGER_SELECTION_OUTLINES;

public class VariationSelector {

    public static List<GameVariationInfo> getAvailableVariations(int viewPagerSelection, Context context) {
        List<GameVariationInfo> availableVariations = new ArrayList<>();
        switch (viewPagerSelection) {
            case VIEW_PAGER_SELECTION_FLAGS:
                availableVariations.add(new GameVariationInfo(FlagsActivity.class, R.drawable.text_type_icon,
                        context.getString(R.string.variationTextDescription)));
                availableVariations.add(new GameVariationInfo(OutlinesToFlagsActivity.class, R.drawable.flag_type_icon,
                        context.getString(R.string.variationImageButtonDescription)));
                break;
            case VIEW_PAGER_SELECTION_OUTLINES:
                availableVariations.add(new GameVariationInfo(MainActivity.class, R.drawable.text_type_icon,
                        context.getString(R.string.variationTextDescription)));
                break;
            case VIEW_PAGER_SELECTION_CAPITALS:
                availableVariations.add(new GameVariationInfo(CapitalsActivity.class, R.drawable.text_type_icon,
                        context.getString(R.string.variationTextDescription)));
                availableVariations.add(new GameVariationInfo(CapitalsPointActivity.class, R.drawable.map_type_icon,
                        context.getString(R.string.variationMapDescription)));
                break;
            case VIEW_PAGER_SELECTION_MONUMENTS:
                availableVariations.add(new GameVariationInfo(MonumentsPointActivity.class, R.drawable.map_type_icon,
                        context.getString(R.string.variationMapDescription)));
                break;
        }
        return availableVariations;
    }
}
