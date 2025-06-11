package com.example.manhinhappmusic.api.artist;

import com.example.manhinhappmusic.dto.SongRequest;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface SongApi {
    // Thêm bài hát (Multipart)
    @Multipart
    @POST("/api/artist/song/add")
    Call<ResponseBody> uploadSong(
            @Part MultipartBody.Part file,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("coverImageUrl") RequestBody coverImageUrl
    );

    // Stream nhạc
    @GET("/api/artist/song/stream/{id}")
    @Streaming
    Call<ResponseBody> streamSong(@Path("id") String songId);

    // Sửa bài hát
    @PUT("/api/artist/song/edit/{id}")
    Call<ResponseBody> editSong(@Path("id") String songId, @Body SongRequest songRequest);
}
