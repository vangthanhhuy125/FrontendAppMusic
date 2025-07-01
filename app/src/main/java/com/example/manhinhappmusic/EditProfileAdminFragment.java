package com.example.manhinhappmusic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EditProfileAdminFragment extends BaseFragment {

    private ImageView imgThumbnail;
    private ImageButton btnEditImage;
    private EditText editName, editEmail;
    private Button btnSave;

    public EditProfileAdminFragment() {

    }

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
        editName = view.findViewById(R.id.edit_name_admin);
        editEmail = view.findViewById(R.id.edit_email_admin);
        btnSave = view.findViewById(R.id.btn_save_ad);


        btnEditImage.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Chức năng sửa ảnh chưa được hỗ trợ", Toast.LENGTH_SHORT).show();
        });


        btnSave.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String email = editEmail.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }


            Bundle result = new Bundle();
            result.putString("name", name);
            result.putString("email", email);
            getParentFragmentManager().setFragmentResult("editProfileResult", result);


            if (callback != null) {
                callback.onRequestChangeFragment(FragmentTag.ADMIN_PROFILE);
            }
        });
    }
}
