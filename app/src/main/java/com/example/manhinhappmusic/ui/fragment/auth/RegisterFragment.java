package com.example.manhinhappmusic.ui.fragment.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.api.ApiClient;
import com.example.manhinhappmusic.dto.LoginResponse;
import com.example.manhinhappmusic.dto.RegisterRequest;
import com.example.manhinhappmusic.dto.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private EditText emailEditText, passwordEditText, fullnameEditText;
    private Button registerButton;
    private TextView signInTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        emailEditText = view.findViewById(R.id.mail_edittext);
        passwordEditText = view.findViewById(R.id.password_edittext);
        registerButton = view.findViewById(R.id.loginButton);
        signInTextView = view.findViewById(R.id.signInTextView);
        fullnameEditText = view.findViewById(R.id.fullname_edittext);

        registerButton.setOnClickListener(v -> handleRegister());

        signInTextView.setOnClickListener(v -> {
            // Quay lại Login Fragment (tuỳ bạn điều hướng thế nào)
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }

    private void handleRegister() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String fullname = fullnameEditText.getText().toString().trim();


        if (email.isEmpty() || password.isEmpty() || fullname.isEmpty()) {
            Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request = new RegisterRequest(email, password, fullname, "ROLE_USER");

        ApiClient.getAuthApi().register(request).enqueue(new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse res = response.body();
                    Toast.makeText(getContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Register failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}