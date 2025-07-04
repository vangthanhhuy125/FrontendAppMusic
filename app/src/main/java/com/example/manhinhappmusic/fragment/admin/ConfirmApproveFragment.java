package com.example.manhinhappmusic.fragment.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.manhinhappmusic.R;

public class ConfirmApproveFragment extends Fragment {
    private String mParam1;
    private String mParam2;

    public ConfirmApproveFragment() {
        // Required empty public constructor
    }

    public static ConfirmApproveFragment newInstance(String param1, String param2) {
        ConfirmApproveFragment fragment = new ConfirmApproveFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("param1");
            mParam2 = getArguments().getString("param2");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_approve, container, false);

        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnApprove = view.findViewById(R.id.btn_approve);

        btnCancel.setOnClickListener(v -> closeFragment());

        btnApprove.setOnClickListener(v -> {

            closeFragment();
        });

        return view;
    }

    private void closeFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(this).commit();
    }
}