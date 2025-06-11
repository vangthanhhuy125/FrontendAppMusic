package com.example.manhinhappmusic.api.admin;

import com.example.manhinhappmusic.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AdminUserApi {
    // Lấy danh sách người dùng chưa xác minh
    @GET("/api/admin/users")
    Call<List<User>> getAllUsers();

    // Lấy danh sách artist
    @GET("/api/admin/users/artists")
    Call<List<User>> getAllArtists();

    // Lấy user theo ID
    @GET("/api/admin/users/{id}")
    Call<User> getUserById(@Path("id") String id);

    // Xóa user
    @DELETE("/api/admin/users/delete/{id}")
    Call<Void> deleteUser(@Path("id") String id);
}
