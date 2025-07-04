package com.example.manhinhappmusic.fragment.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.manhinhappmusic.activity.AdminActivity;
import com.example.manhinhappmusic.activity.MainActivity;
import com.example.manhinhappmusic.dto.AuthResponse;
import com.example.manhinhappmusic.dto.LoginRequest;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.model.User;
import com.example.manhinhappmusic.network.ApiClient;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordText;
    private TextView signUpTextView;
    private final String ARG_PREFERENCES = "AppPreferences";

    private ApiClient apiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiClient = ApiClient.getInstance();
        apiClient.createApiService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailEditText = view.findViewById(R.id.email_edittext);
        passwordEditText = view.findViewById(R.id.password_edittext);
        loginButton = view.findViewById(R.id.login_button);
        forgotPasswordText = view.findViewById(R.id.forgetPasswordTextView);
        signUpTextView = view.findViewById(R.id.signUpTextView);

        SharedPreferences preferences = requireContext().getSharedPreferences(ARG_PREFERENCES, requireContext().MODE_PRIVATE);

        // ✅ Tự động điều hướng nếu đã đăng nhập
        if (preferences.getBoolean("isLoggedIn", false)) {
            String token = preferences.getString("token", "");
            String role = preferences.getString("role", "ROLE_USER");

            apiClient.createApiServiceWithToken(token);

            Intent intent;
            if ("ROLE_ADMIN".equalsIgnoreCase(role)) {
                intent = new Intent(requireActivity(), AdminActivity.class);
            } else {
                intent = new Intent(requireActivity(), MainActivity.class);
            }

            launcher.launch(intent);
            requireActivity().finish();
            return;
        }

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            apiClient.getApiService().login(new LoginRequest(email, password)).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String token = response.body().getToken();
                        String role = response.body().getRole();

                        apiClient.createApiServiceWithToken(token);

                        apiClient.getApiService().getProfile().enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    User user = response.body();

                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("token", token);
                                    editor.putString("role", role);
                                    editor.putBoolean("isLoggedIn", true);
                                    editor.putString("current_user", new Gson().toJson(user));
                                    editor.apply();

                                    Toast.makeText(requireContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                                    Intent intent = "ROLE_ADMIN".equalsIgnoreCase(role) ?
                                            new Intent(requireActivity(), AdminActivity.class) :
                                            new Intent(requireActivity(), MainActivity.class);

                                    launcher.launch(intent);
                                    requireActivity().finish();
                                } else {
                                    Toast.makeText(requireContext(), "Không lấy được thông tin người dùng", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(requireContext(), "Lỗi khi lấy thông tin người dùng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(requireContext(), "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        signUpTextView.setOnClickListener(v -> requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new RegisterFragment())
                .addToBackStack(null)
                .commit());

        forgotPasswordText.setOnClickListener(v -> requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new ForgotPasswordFragment())
                .addToBackStack(null)
                .commit());
    }
    private ActivityResultLauncher launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
        }
    });
}