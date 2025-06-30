package com.example.manhinhappmusic;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class ConfirmDeletingUserFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_USER = "user_data";

    private String mParam1;
    private String mParam2;

    public ConfirmDeletingUserFragment() {
        // Required empty public constructor
    }

    public static ConfirmDeletingUserFragment newInstance(String param1, String param2, User user) {
        ConfirmDeletingUserFragment fragment = new ConfirmDeletingUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putParcelable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }


    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            user = getArguments().getParcelable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout
        return inflater.inflate(R.layout.fragment_confirm_deleting_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button cancelButton = view.findViewById(R.id.cancel_button);
        Button deleteButton = view.findViewById(R.id.delete_button);

        cancelButton.setOnClickListener(v -> {

            requireParentFragment()
                    .getChildFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();


            View container = requireParentFragment().requireView().findViewById(R.id.confirm_delete_container);
            container.setVisibility(View.GONE);
        });


        deleteButton.setOnClickListener(v -> {
            if (user == null) {
                Toast.makeText(getContext(), "User is null!", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle result = new Bundle();
            result.putParcelable("deleted_user", user);


            if (getParentFragment() != null) {
                getParentFragment().getChildFragmentManager().setFragmentResult("user_deleted", result);
            }


            requireParentFragment()
                    .getChildFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();


            View container = requireParentFragment().requireView().findViewById(R.id.confirm_delete_container);
            container.setVisibility(View.GONE);
        });



    }
}
