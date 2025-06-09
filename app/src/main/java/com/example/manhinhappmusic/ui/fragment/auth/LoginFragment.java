package com.example.manhinhappmusic.ui.fragment.auth;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.manhinhappmusic.dto.AuthResponse;
import com.example.manhinhappmusic.dto.LoginRequest;
import com.example.manhinhappmusic.ui.activity.LoginActivity;
import com.example.manhinhappmusic.ui.activity.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    public interface LoginFragmentListener {
        void onSignUpClicked();
        void onForgetPasswordClicked();
    }
    private EditText edtEmail, edtPassword;
    private TextView signUpTextView, forgetPasswordTextView;
    private Button btnLogin;
    private LoginFragmentListener listener;
    private AuthApi authApi;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragmentListener) {
            listener = (LoginFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement LoginFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        edtEmail = view.findViewById(R.id.email_edittext);
        edtPassword = view.findViewById(R.id.password_edittext);
        btnLogin = view.findViewById(R.id.loginButton);
        signUpTextView = view.findViewById(R.id.signUpTextView);
        forgetPasswordTextView = view.findViewById(R.id.forgetPasswordTextView);

        authApi = ApiClient.getAuthApi();

        btnLogin.setOnClickListener(v -> loginUser());

        signUpTextView.setOnClickListener(v -> {
            if (listener != null) listener.onSignUpClicked();
        });

        forgetPasswordTextView.setOnClickListener(v -> {
            if (listener != null) listener.onForgetPasswordClicked();
        });
        return view;
    }

    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(email, password);

        authApi.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    // Lưu token vào SharedPreferences
                    SharedPreferences prefs = requireContext().getSharedPreferences("APP_PREF", getContext().MODE_PRIVATE);
                    prefs.edit().putString("JWT_TOKEN", token).apply();
                    Log.d("LoginFragment", "Token: " + token);

                    Toast.makeText(requireContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                    // Chuyển sang MainActivity
                    Intent intent = new Intent(requireContext(), MainActivity.class);
                    startActivity(intent);

                    // Nếu muốn đóng Activity chứa fragment login
                    requireActivity().finish();

                } else {
                    Toast.makeText(requireContext(), "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
