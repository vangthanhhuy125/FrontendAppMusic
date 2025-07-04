package com.example.manhinhappmusic.fragment.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.palette.graphics.Palette;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.activity.LoginActivity;
import com.example.manhinhappmusic.activity.MainActivity;
import com.example.manhinhappmusic.databinding.FragmentUserProfileBinding;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.fragment.ConfirmLoggingOutFragment;
import com.example.manhinhappmusic.model.User;
import com.example.manhinhappmusic.network.ApiClient;
import com.example.manhinhappmusic.network.ApiService;
import com.example.manhinhappmusic.repository.UserRepository;


public class UserProfileFragment extends BaseFragment {



    public UserProfileFragment() {
        // Required empty public constructor
    }

    private NavController navController;
    private FragmentUserProfileBinding binding;
    private Button editButton;
    private ImageButton backButton;
    private AppCompatButton logOutButton;
    private AppCompatButton feedbackButton;
    private ImageView avatarImage;
    private TextView emailText;
    private TextView userNameText;
    private TextView songCountText;
    private TextView playlistCountText;
    private TextView artistCountText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        editButton = binding.editButton;
        backButton = binding.backButton;
        logOutButton = binding.logOutButton;
        feedbackButton = binding.feedbackButton;
        avatarImage = binding.avatarImage;
        emailText = binding.emailText;
        userNameText = binding.userNameText;

        Glide.with(getContext())
                .load(R.drawable.person_default_cover)
                .circleCrop()
                .into(avatarImage);

        editButton.setOnClickListener(v -> navController.navigate(R.id.editProfileFragment));
        feedbackButton.setOnClickListener(v -> navController.navigate(R.id.feedbackFragment));

        backButton.setOnClickListener(v -> navController.popBackStack());

        logOutButton.setOnClickListener(v -> {
            getParentFragmentManager().setFragmentResultListener("request_log_out",getViewLifecycleOwner(), (requestKey, result) -> {
                SharedPreferences preferences = getContext().getSharedPreferences("AppPreferences", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token", "");
                editor.putBoolean("isLoggedIn", false);
                editor.apply();
                launcher.launch(new Intent(requireActivity(), LoginActivity.class));
                requireActivity().finish();

            });
            ConfirmLoggingOutFragment confirmLoggingOutFragment = new ConfirmLoggingOutFragment();
            confirmLoggingOutFragment.show(getParentFragmentManager(), "");
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = getContext().getSharedPreferences("AppPreferences", getContext().MODE_PRIVATE);
        String token = preferences.getString("token", "");
        UserRepository.getInstance().getUserProfile(token).observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userNameText.setText(user.getFullName());
                emailText.setText(user.getEmail());
                if(user.getAvatarUrl() != null && !user.getAvatarUrl().isBlank())
                    Glide.with(getContext())
                            .asBitmap()
                            .load(ApiService.BASE_URL + user.getAvatarUrl())
                            .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(25))))
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    avatarImage.setImageBitmap(resource);
                                    Palette.from(resource).generate(palette -> {
                                        int vibrant = palette.getVibrantColor(Color.GRAY);
                                        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                                new int[]{vibrant,
                                                        Color.parseColor("#121212"),
                                                        Color.parseColor("#121212"),
                                                        Color.parseColor("#121212"),
                                                });
                                        binding.mainLayout.setBackground(gradientDrawable);
                                    });
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }
                            });
                callback.setIsProcessing(false);


            }

        });
    }

    private ActivityResultLauncher launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {

        }
    });

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}