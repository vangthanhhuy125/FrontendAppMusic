package com.example.manhinhappmusic.network;


import com.example.manhinhappmusic.dto.AuthResponse;
import com.example.manhinhappmusic.dto.LoginRequest;
import com.example.manhinhappmusic.dto.PlaylistRequest;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.model.Song;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    String BASE_URL = "http://10.0.2.2:8081";
    @GET("/api/common/playlist/{playlistId}")
    Call<Playlist> getPlaylistById(@Path("playlistId") String id);

    @GET("/api/common/playlist")
    Call<List<Playlist>> getAllPlaylists();

    @GET("/api/common/playlist/{playlistId}/songs")
    Call<List<Song>> getAllSongs(@Path("playlistId") String id);

    @GET("/api/common/song/{songId}")
    Call<Song> getSongById(@Path("songId") String id);

    @POST("/api/login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);


    @Multipart
    @POST("/api/common/playlist/create")
    Call<ResponseBody> createPlaylist(
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("songs") List<RequestBody> songs,
            @Part MultipartBody.Part thumbnail
    );

    @PATCH("/api/common/playlist/change/{playlistId}")
    Call<Playlist> editPlaylist(@Path("playlistId") String id, @Body Map<String, Object> updates);

    @DELETE("/api/common/playlist/delete/{id}")
    Call<ResponseBody> deletePlaylist(@Path("id") String id);

    @PATCH("/api/common/playlist/{playlistId}/addSongs")
    Call<Playlist> addSongs(@Path("playlistId") String id, @Body Map<String, List<String>> changes);

    @PATCH("/api/common/playlist/{playlistId}/removeSongs")
    Call<Playlist> removeSongs(@Path("playlistId") String id, @Body Map<String, List<String>> changes);
    String STREAMING_URL = BASE_URL + "/api/common/song/stream";

}
