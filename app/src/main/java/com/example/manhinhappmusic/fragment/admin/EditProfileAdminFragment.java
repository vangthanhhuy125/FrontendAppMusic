package com.example.manhinhappmusic.fragment.admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.model.User;
import com.example.manhinhappmusic.network.ApiClient;
import com.example.manhinhappmusic.util.RealPathUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileAdminFragment extends BaseFragment {

    private ImageView imgThumbnail;
    private ImageButton btnEditImage;
    private EditText editName, editEmail;
    private Button btnSave;

    private Uri selectedImageUri = null;
    private User currentUser;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    Glide.with(this)
                            .load(selectedImageUri)
                            .into(imgThumbnail);
                }
            });

    public EditProfileAdminFragment() {}

    public static EditProfileAdminFragment newInstance() {
        return new EditProfileAdminFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgThumbnail = view.findViewById(R.id.img_thumbnail_admin);
        btnEditImage = view.findViewById(R.id.btn_edit_image_admin);
        editName = view.findViewById(R.id.edit_fullname_admin);
        editEmail = view.findViewById(R.id.edit_email_admin);
        btnSave = view.findViewById(R.id.btn_save_ad);

        // Lấy current_user từ SharedPreferences
        String userJson = requireContext().getSharedPreferences("AppPreferences", 0)
                .getString("current_user", null);

        if (userJson != null) {
            currentUser = new com.google.gson.Gson().fromJson(userJson, User.class);
            editName.setText(currentUser.getFullName());
            editEmail.setText(currentUser.getEmail());
            editEmail.setEnabled(false); // Email không cho sửa
            Glide.with(this)
                    .load(currentUser.getAvatarUrl())
                    .placeholder(R.drawable.avatar_app_music)
                    .into(imgThumbnail);
        }

        btnEditImage.setOnClickListener(v -> openImageChooser());

        btnSave.setOnClickListener(v -> {
            String fullName = editName.getText().toString().trim();

            if (fullName.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập tên đầy đủ", Toast.LENGTH_SHORT).show();
                return;
            }

            MultipartBody.Part avatarPart = null;

            if (selectedImageUri != null) {
                try {
                    File file = new File(RealPathUtil.getRealPathFromURI(requireContext(), selectedImageUri));
                    RequestBody requestFile = RequestBody.create(
                            MediaType.parse(requireContext().getContentResolver().getType(selectedImageUri)),
                            file
                                        );
                    avatarPart = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Lỗi ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), fullName);

            ApiClient.getInstance().getApiService().patchUser(namePart, avatarPart)
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();

                                // Cập nhật SharedPreferences
                                User updatedUser = response.body();
                                String json = new com.google.gson.Gson().toJson(updatedUser);
                                requireContext().getSharedPreferences("AppPreferences", 0)
                                        .edit()
                                        .putString("current_user", json)
                                        .apply();

                                if (callback != null) {
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("user_data", updatedUser);
                                    AdminProfileFragment fragment = new AdminProfileFragment();
                                    fragment.setArguments(bundle);
                                    callback.onRequestChangeFragment(FragmentTag.ADMIN_PROFILE);
                                }
                            } else {
                                Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }
}
