package com.example.manhinhappmusic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ConfirmRejectFragment extends DialogFragment {

    public interface OnRejectConfirmedListener {
        void onRejectConfirmed(ArtistRequest request);
    }

    private static final String ARG_REQUEST = "artist_request";

    private ArtistRequest requestToRemove;
    private OnRejectConfirmedListener listener;

    public void setOnRejectConfirmedListener(OnRejectConfirmedListener listener) {
        this.listener = listener;
    }

    public static ConfirmRejectFragment newInstance(ArtistRequest request) {
        ConfirmRejectFragment fragment = new ConfirmRejectFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_REQUEST, request);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_NoActionBar);

        if (getArguments() != null) {
            requestToRemove = getArguments().getParcelable(ARG_REQUEST);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_confirm_reject, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnCancel = view.findViewById(R.id.btncancel);
        Button btnReject = view.findViewById(R.id.btn_reject);

        btnCancel.setOnClickListener(v -> dismiss());

        btnReject.setOnClickListener(v -> {
            if (listener != null && requestToRemove != null) {
                listener.onRejectConfirmed(requestToRemove);
            }
            dismiss();
        });
    }
}
