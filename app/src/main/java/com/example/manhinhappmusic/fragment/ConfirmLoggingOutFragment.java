package com.example.manhinhappmusic.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.activity.LoginActivity;

public class ConfirmLoggingOutFragment extends DialogFragment {

    private static final String ARG_PREFERENCES = "AppPreferences";

    public ConfirmLoggingOutFragment() {}

    public static ConfirmLoggingOutFragment newInstance() {
        return new ConfirmLoggingOutFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_confirm_logging_out, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button cancelButton = view.findViewById(R.id.cancel_button);
        Button logoutButton = view.findViewById(R.id.logout_button);

        cancelButton.setOnClickListener(v -> closeThisFragment());

        logoutButton.setOnClickListener(v -> {
            closeThisFragment();

            // Clear SharedPreferences
            SharedPreferences preferences = requireContext().getSharedPreferences(ARG_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear(); // Xóa hết token, isLoggedIn, role, ...
            editor.apply();

            // Về lại LoginActivity và clear tất cả Activity stack
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void closeThisFragment() {
        View containerView = requireActivity().findViewById(R.id.confirm_log_out_container);
        if (containerView != null) {
            containerView.setVisibility(View.GONE);
        }

        dismiss();
    }
}