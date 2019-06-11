package com.orestis.velen.quiz.login.errors;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orestis.velen.quiz.R;

public class ConnectionErrorFragment extends Fragment {

    private static final String ERROR_MESSAGE = "errorMessage";
    private String errorMessage;


    public ConnectionErrorFragment() {}

    public static ConnectionErrorFragment newInstance(String errorMessage) {
        ConnectionErrorFragment fragment = new ConnectionErrorFragment();
        Bundle args = new Bundle();
        args.putString(ERROR_MESSAGE, errorMessage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            errorMessage = getArguments().getString(ERROR_MESSAGE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connection_error, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView errorText = view.findViewById(R.id.errorText);
        errorText.setText(errorMessage);
    }
}
