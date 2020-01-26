package com.orestis.velen.quiz.login.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orestis.velen.quiz.player.Player;

import java.util.Date;

public class FirebaseSession {

    public static final String LAST_UPDATE_TIME = "lastUpdateTime";

    private FirebaseUser user;
    private DatabaseReference lastUpdateTimeDbRef;
    private DatabaseReference userDbRef;

    public void signIn(FirebaseUser user, ValueEventListener valueEventListener) {
        if(user == null) {
            return;
        }
        this.user = user;
        userDbRef = FirebaseDatabase.getInstance().getReference().child(user.getUid());
        userDbRef.addValueEventListener(valueEventListener);
        lastUpdateTimeDbRef = userDbRef.child(LAST_UPDATE_TIME);
        lastUpdateTimeDbRef.addValueEventListener(valueEventListener);
    }

    public void updatePlayer(Player player) {
        if(userDbRef != null) {
            userDbRef.setValue(player);
            saveLastUpdateTime();
        }
    }

    public String getFirebaseUserId() {
        return user.getUid();
    }

    private void saveLastUpdateTime() {
        lastUpdateTimeDbRef.setValue(String.valueOf(new Date().getTime()));
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

}
