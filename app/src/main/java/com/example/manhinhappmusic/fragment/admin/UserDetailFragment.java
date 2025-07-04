package com.example.manhinhappmusic.fragment.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.model.User;

public class UserDetailFragment extends BaseFragment {

    private TextView tvTitleUser, tvNameUser, tvEmailUser, tvRoleUser, tvJoinedUser;
    private ImageView imgAvatarUser;
    private ImageButton backButtonUser;

    private String name;
    private String email;
    private String role;
    private String joinedDate;
    private String avatarUrl;

    public static UserDetailFragment newInstance(User user) {
        UserDetailFragment fragment = new UserDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("user_data", user);
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

        User user = user = (User) getArguments().getSerializable("user_data");
        if (user != null) {
            tvTitleUser.setText(user.getFullName());
            tvNameUser.setText(user.getFullName());
            tvEmailUser.setText(user.getEmail());
            tvRoleUser.setText(user.getRole());

            Glide.with(this)
                    .load(user.getAvatarUrl())
                    .placeholder(R.drawable.exampleavatar)
                    .into(imgAvatarUser);
        }

        backButtonUser.setOnClickListener(v -> {
            if (callback != null) {
                callback.onRequestGoBackPreviousFragment();
            }
        });
    }
}