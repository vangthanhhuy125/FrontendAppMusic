package com.example.manhinhappmusic.api.authentication;

import com.example.manhinhappmusic.dto.AuthResponse;
import com.example.manhinhappmusic.dto.LoginRequest;
import com.example.manhinhappmusic.dto.MessageResponse;
import com.example.manhinhappmusic.dto.RegisterRequest;
import com.example.manhinhappmusic.dto.VerifyRequest;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    // Đăng ký - gửi OTP
    @POST("/api/register")
    Call<MessageResponse> register(@Body RegisterRequest request);

    // Xác thực OTP và hoàn tất đăng ký
    @POST("/api/verify-email")
    Call<AuthResponse> verifyEmail(@Body VerifyRequest request);

    // Gửi lại mã OTP (resend)
    @POST("/api/resend-otp")
    Call<String> resendOtp(@Body Map<String, String> emailBody);

    // Đăng nhập
    @POST("/api/login")
    Call<AuthResponse> login(@Body LoginRequest request);
}
