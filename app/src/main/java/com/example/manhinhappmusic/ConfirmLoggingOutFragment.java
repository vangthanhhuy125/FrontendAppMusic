package com.example.manhinhappmusic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ConfirmLoggingOutFragment extends Fragment {

    public ConfirmLoggingOutFragment() {

    }

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
        Button logoutButton = view.findViewById(R.id.create_button);

        cancelButton.setOnClickListener(v -> closeThisFragment());

        logoutButton.setOnClickListener(v -> {
            closeThisFragment();

            if (requireActivity() instanceof BaseFragment.FragmentInteractionListener) {
                ((BaseFragment.FragmentInteractionListener) requireActivity())
                        .onRequestChangeFragment(BaseFragment.FragmentTag.LOGIN);
            }
        });
    }

    private void closeThisFragment() {
        View containerView = requireActivity().findViewById(R.id.confirm_log_out_container);
        if (containerView != null) {
            containerView.setVisibility(View.GONE);
        }

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(this)
                .commit();
    }
}
