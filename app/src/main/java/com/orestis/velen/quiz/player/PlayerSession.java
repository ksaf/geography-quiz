package com.orestis.velen.quiz.player;

public class PlayerSession {

    private static PlayerSession instance;
    private Player currentPlayer;
    private boolean isConnected;
    private String currentPlayerName;
    private boolean wasConnectionError;

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
}
