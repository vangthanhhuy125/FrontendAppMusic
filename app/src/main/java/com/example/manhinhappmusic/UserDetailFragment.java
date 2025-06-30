package com.example.manhinhappmusic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UserDetailFragment extends BaseFragment {

    private TextView tvTitleUser, tvNameUser, tvEmailUser, tvRoleUser, tvJoinedUser;
    private ImageView imgAvatarUser;
    private ImageButton backButtonUser;

    private String name;
    private String email;
    private String role;
    private String joinedDate;
    private String avatarUrl;

    public static final String ARG_NAME = "arg_name";
    public static final String ARG_EMAIL = "arg_email";
    public static final String ARG_ROLE = "arg_role";
    public static final String ARG_JOINED = "arg_joined";
    public static final String ARG_AVATAR_URL = "arg_avatar_url";

    public static UserDetailFragment newInstance(String name, String email, String role, String joinedDate, String avatarUrl) {
        UserDetailFragment fragment = new UserDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_EMAIL, email);
        args.putString(ARG_ROLE, role);
        args.putString(ARG_JOINED, joinedDate);
        args.putString(ARG_AVATAR_URL, avatarUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTitleUser = view.findViewById(R.id.tvTitleUser);
        tvNameUser = view.findViewById(R.id.tvNameUser);
        tvEmailUser = view.findViewById(R.id.tvEmailUser);
        tvRoleUser = view.findViewById(R.id.tvRoleUser);
        tvJoinedUser = view.findViewById(R.id.tvJoinedUser);
        imgAvatarUser = view.findViewById(R.id.imgAvatarUser);
        backButtonUser = view.findViewById(R.id.back_button_user);

        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME, "Unknown");
            email = getArguments().getString(ARG_EMAIL, "Unknown");
            role = getArguments().getString(ARG_ROLE, "User");
            joinedDate = getArguments().getString(ARG_JOINED, "Unknown");
            avatarUrl = getArguments().getString(ARG_AVATAR_URL, "");

            tvNameUser.setText(name);
            tvEmailUser.setText(email);
            tvRoleUser.setText(role);
            tvJoinedUser.setText(joinedDate);

            // Nếu dùng Glide để load avatar:
            // Glide.with(this).load(avatarUrl).into(imgAvatarUser);
        }

        backButtonUser.setOnClickListener(v -> {
            if (callback != null) {
                callback.onRequestGoBackPreviousFragment();
            }
        });
    }
}
