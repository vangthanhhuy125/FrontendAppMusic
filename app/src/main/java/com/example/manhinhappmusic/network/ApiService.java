package com.example.manhinhappmusic.network;


import com.example.manhinhappmusic.dto.AuthResponse;
import com.example.manhinhappmusic.dto.LoginRequest;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.model.Song;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("/api/common/playlist/{playlistId}")
    Call<Playlist> getPlaylistById(@Path("playlistId") String id);

    @GET("/api/common/playlist")
    Call<List<Playlist>> getAllPlaylists();

    @GET("/api/artist/song/stream/{songId}")
    Call<Song> getSongById(@Path("songId") String id);

    @POST("/api/login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);



}
