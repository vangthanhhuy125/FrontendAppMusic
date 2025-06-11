package com.example.manhinhappmusic.api.common;

import com.example.manhinhappmusic.model.Notification;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationApi {
    // [GET] /api/notifications/user/{userId}
    @GET("/api/notifications/user/{userId}")
    Call<List<Notification>> getNotificationsByUserId(@Path("userId") String userId);

    // [PUT] /api/notifications/{id}/read
    @PUT("/api/notifications/{id}/read")
    Call<Notification> markNotificationAsRead(@Path("id") String id);

    // [POST] /api/notifications
    @POST("/api/notifications")
    Call<Notification> sendNotification(@Body Notification notification);
}
