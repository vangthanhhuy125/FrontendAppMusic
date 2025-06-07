package com.example.manhinhappmusic.ui.fragment.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.api.ApiClient;
import com.example.manhinhappmusic.api.authentication.AuthApi;
import com.example.manhinhappmusic.dto.LoginResponse;
import com.example.manhinhappmusic.dto.MessageResponse;
import com.example.manhinhappmusic.dto.RegisterRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class RegisterFragment extends Fragment {
    public interface RegisterFragmentListener {
        void onBackToLogin();
        void onOtpRequested(String email);
    }

    private EditText emailEditText, passwordEditText, fullnameEditText;
    private Button registerButton;
    private TextView signInTextView;
    private RegisterFragmentListener listener;

    private AuthApi authApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        emailEditText = view.findViewById(R.id.mail_edittext);
        passwordEditText = view.findViewById(R.id.password_edittext);
        fullnameEditText = view.findViewById(R.id.fullname_edittext);
        registerButton = view.findViewById(R.id.registerButton);
        signInTextView = view.findViewById(R.id.signInTextView);

        authApi = ApiClient.getAuthApi(requireContext()); // Lấy API client

        registerButton.setOnClickListener(v -> handleRegister());

        signInTextView.setOnClickListener(v -> {
            // Quay lại Login Fragment
            if (listener != null) listener.onBackToLogin();
        });

        return view;
    }

    private void handleRegister() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String fullname = fullnameEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || fullname.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request = new RegisterRequest(email, password, fullname, "ROLE_USER");

        authApi.register(request).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "OTP đã được gửi đến email", Toast.LENGTH_SHORT).show();
                    if (listener != null) {
                        listener.onOtpRequested(email); // chuyển sang OTP Fragment
                    }
                } else {
                    Toast.makeText(getContext(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                Log.e("RegisterFragment", "Lỗi kết nối khi gọi API register", t);
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }
}
