package com.example.manhinhappmusic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

public class AdminProfileFragment extends BaseFragment {

    private ShapeableImageView avatarImage;
    private TextView nameText, emailText, roleText, joinedText;
    private ImageButton backButton;
    private Button editButton, logoutButton;

    public static AdminProfileFragment newInstance() {
        return new AdminProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        avatarImage = view.findViewById(R.id.avataradmin);
        nameText = view.findViewById(R.id.txtnameadmin);
        emailText = view.findViewById(R.id.tvEmailAdmin);
        roleText = view.findViewById(R.id.tvRoleAmin);
        joinedText = view.findViewById(R.id.tvJoinedAdmin);
        backButton = view.findViewById(R.id.btnbackadmin);
        editButton = view.findViewById(R.id.btneditadmin);
        logoutButton = view.findViewById(R.id.btnlogoutadmin);


        Glide.with(this)
                .load(R.drawable.avatar_app_music)
                .into(avatarImage);

        nameText.setText("Admin Name");
        emailText.setText("admin@example.com");
        roleText.setText("Admin");
        joinedText.setText("22/12/2024");


        backButton.setOnClickListener(v -> {
            if (callback != null) {
                callback.onRequestChangeFragment(FragmentTag.ADMIN_HOME);
            }
        });



        editButton.setOnClickListener(v -> {
            if (callback != null) {
                callback.onRequestChangeFragment(FragmentTag.EDIT_PROFILE);
            }
        });


        logoutButton.setOnClickListener(v -> {
            if (callback != null) {
                callback.onRequestOpenBottomSheetFragment(FragmentTag.CONFIRM_LOGGING_OUT);
            }
        });
    }
}
