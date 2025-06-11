package com.example.manhinhappmusic.api.authentication;

import com.example.manhinhappmusic.dto.ChangePasswordRequest;
import com.example.manhinhappmusic.dto.ForgotPasswordRequest;
import com.example.manhinhappmusic.dto.MessageResponse;
import com.example.manhinhappmusic.dto.ResetPasswordRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PasswordApi {
    @POST("/api/password/forgot")
    Call<MessageResponse> forgotPassword(@Body ForgotPasswordRequest request);

    @POST("/api/password/change")
    Call<String> changePassword(@Body ChangePasswordRequest request);

    @POST("/api/password/reset-request")
    Call<String> sendOtp(@Query("email") String email);
    @POST("/api/password/reset")
    Call<MessageResponse> resetPassword(@Body ResetPasswordRequest request);
}
