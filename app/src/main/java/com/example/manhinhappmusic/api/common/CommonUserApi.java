package com.example.manhinhappmusic.api.common;

import com.example.manhinhappmusic.dto.ChangePasswordRequest;
import com.example.manhinhappmusic.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Query;

public interface CommonUserApi {
    // [GET] /api/common/users/search?keyword=...
    @GET("/api/common/users/search")
    Call<List<User>> searchUsersByName(@Query("keyword") String keyword);

    @GET("/api/common/users/search-artist")
    Call<List<User>> searchArtistByName(@Query("name") String name);

    // [GET] /api/common/users/me
    @GET("/api/common/users/me")
    Call<User> getMyProfile();

    // [PATCH] /api/common/users/me/change
    @PATCH("/api/common/users/me/change")
    Call<User> updateProfile(@Body User userData);

    // [PATCH] /api/common/users/me/password/change
    @PATCH("/api/common/users/me/password/change")
    Call<String> changePassword(@Body ChangePasswordRequest request);
}
