package com.orestis.velen.quiz.login;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.orestis.velen.quiz.login.firebase.FirebaseSession;
import com.orestis.velen.quiz.login.localStorage.LocalStorageManager;
import com.orestis.velen.quiz.player.Player;

import static com.orestis.velen.quiz.login.firebase.FirebaseSession.LAST_UPDATE_TIME;

public class UserSession implements ValueEventListener {

    private static UserSession instance;
    private FirebaseSession firebaseSession = new FirebaseSession();
    private LocalStorageManager localStorageManager;
    private String lastUpdateTimeRecovered;
    private boolean isLastUpdateTimeRecovered;
    private PlayerRecoveredListener playerRecoveredListener;
    private Player recoveredPlayer;
    private boolean isPlayerRecovered;

    public static UserSession getInstance() {
        if(instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void recoverPlayer(FirebaseUser firebaseUser, PlayerRecoveredListener playerRecoveredListener, Context context) {
        createStorageManagers(context);
        firebaseSession.signIn(firebaseUser, this);
        this.playerRecoveredListener = playerRecoveredListener;
    }

    public void recoverPlayerFromLocalStorage(PlayerRecoveredListener playerRecoveredListener, Context context) {
        createStorageManagers(context);
        this.playerRecoveredListener = playerRecoveredListener;
        createPlayerFromLocalStorage();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(areAllValuesRecoveredFromFirebase()) {
            return;
        }
        if(dataSnapshot.getKey().equals(firebaseSession.getFirebaseUserId()) && recoveredPlayer == null) {
            recoveredPlayer = dataSnapshot.getValue(Player.class);
            isPlayerRecovered = true;
            createPlayer();
        } else if(dataSnapshot.getKey().equals(LAST_UPDATE_TIME)) {
            lastUpdateTimeRecovered = dataSnapshot.getValue(String.class);
            isLastUpdateTimeRecovered = true;
            createPlayer();
        }
    }

    public void savePlayer(Player player) {
        recoveredPlayer = player;
        savePlayerToFirebase(player);
        savePlayerToLocalStorage(player);
    }

    private void createPlayer() {
        if(areAllValuesRecoveredFromFirebase()) {
            if(areAllValuesRecoveredFromFirebaseNotNull() && isFirebaseEntryMoreRecent()) {
                createPlayerFromFirebase();
                savePlayerToLocalStorage(recoveredPlayer);
            } else {
                createPlayerFromLocalStorage();
                savePlayerToFirebase(recoveredPlayer);
            }
        }
    }

    private void createStorageManagers(Context context) {
        localStorageManager = new LocalStorageManager(context);
    }

    private boolean areAllValuesRecoveredFromFirebase() {
        return isPlayerRecovered && isLastUpdateTimeRecovered;
    }

    private boolean areAllValuesRecoveredFromFirebaseNotNull() {
        return recoveredPlayer != null && lastUpdateTimeRecovered != null;
    }

    private boolean isFirebaseEntryMoreRecent() {
        long localLastUpdate = localStorageManager.getLastUpdateTime();
        return Long.parseLong(lastUpdateTimeRecovered) > localStorageManager.getLastUpdateTime();
    }

    private void createPlayerFromFirebase() {
        playerRecoveredListener.onPlayerRecovered(recoveredPlayer);
    }

    private void createPlayerFromLocalStorage() {
        recoveredPlayer = localStorageManager.getPlayer();
        playerRecoveredListener.onPlayerRecovered(recoveredPlayer);
    }

    private void savePlayerToFirebase(Player player) {
        firebaseSession.updatePlayer(player);
    }

    private void savePlayerToLocalStorage(Player player) {
        localStorageManager.updatePlayer(player);
    }

    public void disconnectFromFirebase() {
        isPlayerRecovered = true;
        lastUpdateTimeRecovered = null;
        isLastUpdateTimeRecovered = false;
        recoveredPlayer = null;
        firebaseSession.signOut();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        createPlayerFromLocalStorage();
    }
}
