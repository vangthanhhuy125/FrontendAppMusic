package com.example.manhinhappmusic.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.api.ApiClient;
import com.example.manhinhappmusic.api.authentication.AuthApi;
import com.example.manhinhappmusic.dto.AuthResponse;
import com.example.manhinhappmusic.dto.LoginRequest;
import com.example.manhinhappmusic.dto.LoginResponse;
import com.example.manhinhappmusic.ui.fragment.auth.ChangePasswordFragment;
import com.example.manhinhappmusic.ui.fragment.auth.RegisterFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signUpTextView, forgetPasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login); // Đổi lại đúng layout

        // Ánh xạ view
        emailEditText = findViewById(R.id.email_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        loginButton = findViewById(R.id.loginButton);
        signUpTextView = findViewById(R.id.signUpTextView);
        forgetPasswordTextView = findViewById(R.id.forgetPasswordTextView);

        // Sự kiện login
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest request = new LoginRequest(email, password);
            AuthApi authApi = ApiClient.getAuthApi();

            authApi.login(request).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        AuthResponse res = response.body();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Log.e("LOGIN_API", "Lỗi kết nối: " + t.getMessage(), t);  // In cả lỗi và stacktrace
                    Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Xử lý Sign Up
        signUpTextView.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển đến màn hình Đăng ký", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, RegisterFragment.class);
            startActivity(intent);
        });

        // Xử lý Forget Password
        forgetPasswordTextView.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển đến màn hình Reset mật khẩu", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, ChangePasswordFragment.class);
            startActivity(intent);
        });
    }
}