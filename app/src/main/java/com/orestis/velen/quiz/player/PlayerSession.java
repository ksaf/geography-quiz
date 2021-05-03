package com.orestis.velen.quiz.player;

import com.orestis.velen.quiz.mainMenu.SelectionSaver;

public class PlayerSession {

    private static PlayerSession instance;
    private Player currentPlayer;
    private boolean isConnected;
    private String currentPlayerName;
    private boolean wasConnectionError;
    private SelectionSaver selectionSaver = new SelectionSaver();

    public static PlayerSession getInstance() {
        if(instance == null) {
            instance = new PlayerSession();
        }
        return instance;
    }

    public void setRecoveredPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public void setCurrentPlayerName(String currentPlayerName) {
        this.currentPlayerName = currentPlayerName;
    }

    public void setWasConnectionError(boolean wasConnectionError) {
        this.wasConnectionError = wasConnectionError;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public boolean isWasConnectionError() {
        return wasConnectionError;
    }

    public void setLastMenuSelection(int lastMenuSelection) {
        selectionSaver.setLastMenuSelection(lastMenuSelection);
    }

    public int getLastMenuSelection() {
        return selectionSaver.getLastMenuSelection();
    }

    public void setDifficultySelection(int menuSelection, int lastDifficultySelection) {
        selectionSaver.setLastDifficultyForSelection(menuSelection, lastDifficultySelection);
    }

    public int getDifficultySelection(int menuSelection) {
        return selectionSaver.getLastDifficultyForSelection(menuSelection);
    }

    public void setVariationSelection(int menuSelection, int lastVariationSelection) {
        selectionSaver.setLastVariationForSelection(menuSelection, lastVariationSelection);
    }

    public int getVariationSelection(int menuSelection) {
        return selectionSaver.getLastVariationForSelection(menuSelection);
    }
}
