package com.example.manhinhappmusic.fragment.admin;

import android.content.Context;
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
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.fragment.ConfirmLoggingOutFragment;
import com.google.android.material.imageview.ShapeableImageView;
import com.example.manhinhappmusic.model.User;

public class AdminProfileFragment extends BaseFragment {

    private ShapeableImageView avatarImage;
    private TextView nameText, emailText, roleText;
    private ImageButton backButton;
    private Button editButton, logoutButton;

    private User user;

    public static AdminProfileFragment newInstance(User user) {
        AdminProfileFragment fragment = new AdminProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("user_data", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable("user_data");
        }
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
        backButton = view.findViewById(R.id.btnbackadmin);
        editButton = view.findViewById(R.id.btneditadmin);
        logoutButton = view.findViewById(R.id.btnlogoutadmin);

        if (user != null) {
            nameText.setText(user.getFullName());
            emailText.setText(user.getEmail());
            roleText.setText(user.getRole());

            Glide.with(this)
                    .load(user.getAvatarUrl())
                    .placeholder(R.drawable.avatar_app_music)
                    .error(R.drawable.error_image)
                    .into(avatarImage);
        }

        backButton.setOnClickListener(v -> {
            if (callback != null) {
                callback.onRequestGoBackPreviousFragment();
            }
        });

        editButton.setOnClickListener(v -> {
            if (callback != null) {
                callback.onRequestChangeFragment(FragmentTag.EDIT_PROFILE_ADMIN);
            }
        });

        logoutButton.setOnClickListener(v -> {
            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.confirm_log_out_container, new ConfirmLoggingOutFragment())
                    .commit();

            View container = requireActivity().findViewById(R.id.confirm_log_out_container);
            if (container != null) {
                container.setVisibility(View.VISIBLE);
            }
        });
    }
}