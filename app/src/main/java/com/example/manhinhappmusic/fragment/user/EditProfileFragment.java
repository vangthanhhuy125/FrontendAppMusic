package com.example.manhinhappmusic.fragment.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.databinding.FragmentEditProfileBinding;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.fragment.ConfirmDiscardingChangesFragment;
import com.example.manhinhappmusic.model.User;
import com.example.manhinhappmusic.network.ApiService;
import com.example.manhinhappmusic.repository.UserRepository;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters

    private NavController navController;
    private FragmentEditProfileBinding binding;
    private ImageButton closeButton;
    private Button saveButton;
    private ImageView userAvatarImage;
    private EditText usernameEditText;
    private ImageView editIcon;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri selectedImageUri;
    private boolean isModified = false;
    private boolean isImagePicked = false;
    private User userProfile;
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        bottomNavVisibility = View.GONE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = getContext().getSharedPreferences("AppPreferences", getContext().MODE_PRIVATE);
        String token = preferences.getString("token", "");
        UserRepository.getInstance().getUserProfile(token).observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userProfile = user;
                usernameEditText.setText(user.getFullName());
                if(user.getAvatarUrl() != null && !user.getAvatarUrl().isBlank())
                    Glide.with(getContext())
                            .load(ApiService.BASE_URL + user.getAvatarUrl())
                            .circleCrop()
                            .into(userAvatarImage);
                else
                    Glide.with(getContext())
                            .load(R.drawable.person_default_cover)
                            .circleCrop()
                            .into(userAvatarImage);
                callback.setIsProcessing(false);




            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        closeButton = binding.closeButton;
        saveButton = binding.saveButton;
        userAvatarImage = binding.avatarImage;
        usernameEditText = binding.usernameEditText;
        editIcon = binding.editIcon;

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if(selectedImageUri != null)
                        {                            isImagePicked = true;

                            Glide.with(getContext())
                                    .load(selectedImageUri)
                                    .circleCrop()
                                    .into(userAvatarImage);
                            setModified(true);
                        }
                    }
                }
        );
        Glide.with(getContext())
                .load(R.drawable.person_default_cover)
                .circleCrop()
                .into(userAvatarImage);

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(userProfile.getFullName()))
                    setModified(true);
                else if(!isImagePicked)
                    setModified(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getContext().getSharedPreferences("AppPreferences", getContext().MODE_PRIVATE);
                String token = preferences.getString("token", "");
                try {

                    File file = uriToFile(getContext(), selectedImageUri);
                    UserRepository.getInstance().editUserProfile(token, usernameEditText.getText().toString(), file).observe(getViewLifecycleOwner(), new Observer<User>() {
                        @Override
                        public void onChanged(User user) {
                            Toast.makeText(getContext(), "Edit profile successfully!", Toast.LENGTH_SHORT).show();
                            navController.popBackStack();
                        }
                    });
                }
                catch (Exception ex)
                {

                }

            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isModified)
                {
                    ConfirmDiscardingChangesFragment confirmDiscardingChangesFragment = new ConfirmDiscardingChangesFragment();
                    confirmDiscardingChangesFragment.show(getParentFragmentManager(), "");

                }
                else
                {
                    navController.popBackStack();
                }
            }
        });

        userAvatarImage.setOnClickListener(this::achieveImageFile);
        editIcon.setOnClickListener(this::achieveImageFile);

        getParentFragmentManager().setFragmentResultListener("discard_changes",getViewLifecycleOwner() ,(request, result) -> {
            navController.popBackStack();
        });
    }


    private void achieveImageFile(View v)
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        imagePickerLauncher.launch(intent);
    }


    private void setModified(boolean modified) {
        isModified = modified;
        if(isModified)
        {
            saveButton.setClickable(true);
            saveButton.setTextColor(Color.WHITE);
        }
        else
        {
            saveButton.setClickable(false);
            saveButton.setTextColor(Color.parseColor("#AAAAAA"));
        }
    }

    public File uriToFile(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        String fileName = "temp_image_" + System.currentTimeMillis() + ".jpg";
        File file = new File(context.getCacheDir(), fileName);

        OutputStream outputStream = new FileOutputStream(file);
        byte[] buf = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) > 0) {
            outputStream.write(buf, 0, len);
        }

        outputStream.close();
        inputStream.close();

        return file;
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) cursor.close();
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}