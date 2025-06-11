package com.example.manhinhappmusic.api.admin;

import com.example.manhinhappmusic.model.Genre;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AdminGenreApi {
    // [POST] Tạo genre mới
    @POST("/api/admin/genres")
    Call<Genre> createGenre(@Body Genre genre);

    // [GET] Lấy toàn bộ genre
    @GET("/api/admin/genres")
    Call<List<Genre>> getAllGenres();

    // [GET] Lấy genre theo id
    @GET("/api/admin/genres/{id}")
    Call<Genre> getGenreById(@Path("id") String id);

    // [PUT] Cập nhật genre
    @PUT("/api/admin/genres/{id}")
    Call<Genre> updateGenre(@Path("id") String id, @Body Genre genre);

    // [DELETE] Xóa genre
    @DELETE("/api/admin/genres/{id}")
    Call<Void> deleteGenre(@Path("id") String id);
}
