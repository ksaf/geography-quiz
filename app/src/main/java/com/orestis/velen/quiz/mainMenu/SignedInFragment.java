package com.orestis.velen.quiz.mainMenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orestis.velen.quiz.R;

public class SignedInFragment extends Fragment {

    private String playerNameString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.signed_in_fragment, container, false);
        TextView playerNameTextView = rootView.findViewById(R.id.profileName);
        playerNameTextView.setText(playerNameString);

        return rootView;
    }

    public static class Builder {
        private String playerNameString;

        public Builder withPlayerName(String playerNameString) {
            this.playerNameString = playerNameString;
            return this;
        }

        public SignedInFragment build() {
            SignedInFragment signedInFragment = new SignedInFragment();
            signedInFragment.playerNameString = playerNameString;
            return signedInFragment;
        }
    }
}
