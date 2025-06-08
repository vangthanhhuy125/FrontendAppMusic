package com.example.manhinhappmusic.api.common;

import com.example.manhinhappmusic.model.Song;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CommonSongApi {
    @GET("/api/common/song/{id}")
    Call<Song> getSong(@Path("id") String id);
    @GET("/api/common/song/stream/{id}")
    Call<ResponseBody> streamSong(@Path("id") String id);
}
