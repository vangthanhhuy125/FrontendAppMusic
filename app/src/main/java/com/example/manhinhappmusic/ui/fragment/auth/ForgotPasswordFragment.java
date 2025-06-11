package com.example.manhinhappmusic.ui.fragment.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.api.ApiClient;
import com.example.manhinhappmusic.api.authentication.PasswordApi;
import com.example.manhinhappmusic.dto.ForgotPasswordRequest;
import com.example.manhinhappmusic.dto.MessageResponse;
import com.example.manhinhappmusic.dto.ResetPasswordRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordFragment extends Fragment {
    public interface ForgotPasswordFragmentListener {}
    private EditText emailEditText, otpEditText, newPasswordEditText;
    private Button sendOtpButton, resetPasswordButton;
    private LinearLayout layoutEmail, layoutReset;
    private String sentEmail = null;

    private PasswordApi passwordApi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        // Bước 1
        emailEditText = view.findViewById(R.id.email_edit_text);
        sendOtpButton = view.findViewById(R.id.send_otp_button);

        // Bước 2
        otpEditText = view.findViewById(R.id.otp_edit_text);
        newPasswordEditText = view.findViewById(R.id.new_password_edit_text);
        resetPasswordButton = view.findViewById(R.id.reset_password_button);

        layoutEmail = view.findViewById(R.id.layout_email);
        layoutReset = view.findViewById(R.id.layout_reset);

        passwordApi = ApiClient.getPasswordApi();

        sendOtpButton.setOnClickListener(v -> handleSendOtp());
        resetPasswordButton.setOnClickListener(v -> handleResetPassword());

        return view;
    }

    private void handleSendOtp() {
        String email = emailEditText.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }
        sentEmail = email;
        ForgotPasswordRequest request = new ForgotPasswordRequest(email);
        passwordApi.forgotPassword(request).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(requireContext(), "Gửi OTP thành công", Toast.LENGTH_SHORT).show();
                    layoutEmail.setVisibility(View.GONE);
                    layoutReset.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "Gửi OTP thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                Log.e("ForgotPassword", "Lỗi mạng khi gọi API forgotPassword", t);
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleResetPassword() {
        String email = sentEmail;
        String otp = otpEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();

        if (otp.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập OTP và mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        ResetPasswordRequest request = new ResetPasswordRequest(email, otp, newPassword);
        Log.d("ResetRequest", "Email: " + email + ", OTP: " + otp + ", Password: " + newPassword);
        passwordApi.resetPassword(request).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(requireContext(), "Đặt lại mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Đặt lại mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
