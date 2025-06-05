package com.example.manhinhappmusic.ui.fragment.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.api.authentication.AuthApi;
import com.example.manhinhappmusic.dto.AuthResponse;
import com.example.manhinhappmusic.dto.LoginRequest;
import com.example.manhinhappmusic.dto.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {

    private EditText edtEmail, edtPassword;
    private Button btnLogin;

    private AuthApi authApi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        edtEmail = view.findViewById(R.id.email_edittext);
        edtPassword = view.findViewById(R.id.password_edittext);
        btnLogin = view.findViewById(R.id.loginButton);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8081") // đổi IP nếu không dùng máy ảo
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authApi = retrofit.create(AuthApi.class);

        btnLogin.setOnClickListener(v -> loginUser());

        return view;
    }

    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(email, password);

        authApi.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    // Lưu token vào SharedPreferences hoặc xử lý tiếp
                    Toast.makeText(getContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}