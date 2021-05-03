package com.orestis.velen.quiz.mainMenu;

import android.util.SparseIntArray;

public class SelectionSaver {

    private int lastMenuSelection;
    private SparseIntArray lastDifficultyForSelection = new SparseIntArray();
    private SparseIntArray lastVariationForSelection = new SparseIntArray();

    public void setLastMenuSelection(int lastMenuSelection) {
        this.lastMenuSelection = lastMenuSelection;
    }

    public int getLastMenuSelection() {
        return lastMenuSelection;
    }

    public void setLastDifficultyForSelection(int menuSelection, int lastDifficultySelection) {
        this.lastDifficultyForSelection.put(menuSelection, lastDifficultySelection);
    }

    public int getLastDifficultyForSelection(int menuSelection) {
        return this.lastDifficultyForSelection.get(menuSelection) == 0 ? 1 : this.lastDifficultyForSelection.get(menuSelection);
    }

    public void setLastVariationForSelection(int menuSelection, int lastVariationSelection) {
        this.lastVariationForSelection.put(menuSelection, lastVariationSelection);
    }

    public int getLastVariationForSelection(int menuSelection) {
        return this.lastVariationForSelection.get(menuSelection);
    }
}
