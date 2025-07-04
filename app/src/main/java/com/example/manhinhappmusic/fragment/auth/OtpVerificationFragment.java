package com.example.manhinhappmusic.fragment.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.dto.AuthResponse;
import com.example.manhinhappmusic.dto.VerifyRequest;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.network.ApiClient;
import com.example.manhinhappmusic.network.ApiService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerificationFragment extends BaseFragment {

    private EditText otpEditText;
    private Button verifyButton;
    private Button resendButton;
    private ImageButton backButton;
    private ApiService authApi;
    private String email;

    private static final String PREFS_NAME = "AppPreferences";
    private static final String PREF_EMAIL_KEY = "otp_email";

    public OtpVerificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authApi = ApiClient.getInstance().getApiService();

        // ✅ Lấy email từ SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        email = prefs.getString(PREF_EMAIL_KEY, null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_otp, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        otpEditText = view.findViewById(R.id.otp_edittext);
        verifyButton = view.findViewById(R.id.confirm_button);
        resendButton = view.findViewById(R.id.resend_button);
        backButton = view.findViewById(R.id.back_button);

        verifyButton.setOnClickListener(v -> verifyOtp());
        resendButton.setOnClickListener(v -> resendOtp());
        backButton.setOnClickListener(v -> callback.onRequestGoBackPreviousFragment());
    }

    private void verifyOtp() {
        String otp = otpEditText.getText().toString().trim();
        if (otp.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email == null) {
            Toast.makeText(getContext(), "Không tìm thấy email để xác thực!", Toast.LENGTH_SHORT).show();
            return;
        }

        VerifyRequest request = new VerifyRequest(email, otp);
        authApi.verifyEmail(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Xác thực thành công!", Toast.LENGTH_SHORT).show();
                    callback.onRequestChangeFragment(FragmentTag.LOGIN);
                } else {
                    Toast.makeText(getContext(), "Mã OTP không đúng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resendOtp() {
        if (email == null) {
            Toast.makeText(getContext(), "Không thể gửi lại OTP do thiếu email", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> body = new HashMap<>();
        body.put("email", email);

        authApi.resendOtp(body).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "OTP mới đã được gửi", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Không thể gửi lại OTP", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
