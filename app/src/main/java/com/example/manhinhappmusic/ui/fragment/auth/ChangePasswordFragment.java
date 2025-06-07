package com.example.manhinhappmusic.ui.fragment.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.api.ApiClient;
import com.example.manhinhappmusic.api.authentication.PasswordApi;
import com.example.manhinhappmusic.dto.ChangePasswordRequest;
import com.example.manhinhappmusic.util.JwtUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {

    private EditText oldPaswordEditText, newPasswordEditText;
    private Button confirmButton;
    private PasswordApi passwordApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        // Ánh xạ view
        oldPaswordEditText = view.findViewById(R.id.oldpass_edittext);
        newPasswordEditText = view.findViewById(R.id.newpass_edittext);
        confirmButton = view.findViewById(R.id.confirmButton);

        passwordApi = ApiClient.getPasswordApi(requireContext());

        confirmButton.setOnClickListener(v -> handleChangePassword());

        return view;
    }

    private void handleChangePassword() {
        SharedPreferences sharedPref = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String token = sharedPref.getString("jwt_token", null);

        if (token == null) {
            Toast.makeText(getContext(), "Không tìm thấy token", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = JwtUtils.getEmailFromToken(token);

        if (email == null) {
            Toast.makeText(getContext(), "Không lấy được email từ token", Toast.LENGTH_SHORT).show();
            return;
        }
        String oldpasword = oldPaswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(oldpasword) || TextUtils.isEmpty(newPassword)) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ!", Toast.LENGTH_SHORT).show();
            return;
        }

        ChangePasswordRequest request = new ChangePasswordRequest(email, oldpasword, newPassword);

        passwordApi.changePassword(request).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String res = response.body();
                    Toast.makeText(requireContext(), "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
