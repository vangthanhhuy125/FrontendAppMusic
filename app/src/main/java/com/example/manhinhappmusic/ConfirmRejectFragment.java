package com.example.manhinhappmusic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ConfirmRejectFragment extends Fragment {

    public ConfirmRejectFragment() {
        // Required empty public constructor
    }

    public static ConfirmRejectFragment newInstance(String param1, String param2) {
        ConfirmRejectFragment fragment = new ConfirmRejectFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_reject, container, false);

        Button btnCancel = view.findViewById(R.id.btncancel);
        Button btnReject = view.findViewById(R.id.btn_reject);

        btnCancel.setOnClickListener(v -> closeFragment());

        btnReject.setOnClickListener(v -> {
            // TODO: Thực hiện hành động từ chối tại đây, ví dụ:
            // Toast.makeText(getContext(), "Request rejected", Toast.LENGTH_SHORT).show();
            closeFragment();
        });

        return view;
    }

    private void closeFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(this).commit();
    }
}
