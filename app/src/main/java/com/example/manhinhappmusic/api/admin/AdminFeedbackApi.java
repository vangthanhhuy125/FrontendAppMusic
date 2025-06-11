package com.example.manhinhappmusic.api.admin;

import com.example.manhinhappmusic.model.Feedback;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AdminFeedbackApi {
    // [POST] Gửi feedback
    @POST("/api/feedbacks")
    Call<Feedback> sendFeedback(@Body Feedback feedback);

    // [GET] Feedback của chính user
    @GET("/api/feedbacks/me")
    Call<List<Feedback>> getMyFeedbacks();

    // [GET] Feedback của một user cụ thể (admin dùng)
    @GET("/api/feedbacks/user/{userId}")
    Call<List<Feedback>> getUserFeedbacks(@Path("userId") String userId);

    // [GET] Lấy tất cả feedback (admin dùng)
    @GET("/api/feedbacks")
    Call<List<Feedback>> getAllFeedbacks(@Query("status") String status);

    // [PUT] Admin phản hồi feedback
    @PUT("/api/feedbacks/{id}/reply")
    Call<Feedback> replyFeedback(@Path("id") String id, @Body Feedback feedback);
}
