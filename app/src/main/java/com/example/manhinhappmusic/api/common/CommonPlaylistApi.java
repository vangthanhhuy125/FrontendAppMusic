package com.example.manhinhappmusic.api.common;

import com.example.manhinhappmusic.dto.PlaylistRequest;
import com.example.manhinhappmusic.model.Playlist;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommonPlaylistApi {
    // [GET] /api/common/playlist
    @GET("/api/common/playlist")
    Call<List<Playlist>> getAllPlaylists();

    // [GET] /api/common/playlist/{id}
    @GET("/api/common/playlist/{id}")
    Call<Playlist> getPlaylistById(@Path("id") String id);

    @GET("/api/common/playlist//{playlistId}/songs")
    Call<Playlist> getSongsInPlaylist(@Path("id") String id);
    // [GET] /api/common/playlist/search?keyword=...
    @GET("/api/common/playlist/search")
    Call<List<Playlist>> searchPlaylistByName(@Query("name") String name);

    // [POST] /api/common/playlist/create
    @POST("/api/common/playlist/create")
    Call<Void> createPlaylist(@Body PlaylistRequest request);

    // [PATCH] /api/common/playlist/change/{id}
    @PATCH("/api/common/playlist/change/{id}")
    Call<Playlist> updatePlaylist(
            @Path("id") String id,
            @Body Map<String, Object> updates
    );

    // [PATCH] /api/common/playlist/{playlistId}/addSongs
    @PATCH("/api/common/playlist/{playlistId}/addSongs")
    Call<Playlist> addSongsToPlaylist(
            @Path("playlistId") String playlistId,
            @Body Map<String, List<String>> songs
    );

    // [PATCH] /api/common/playlist/{playlistId}/removeSongs
    @PATCH("/api/common/playlist/{playlistId}/removeSongs")
    Call<Playlist> removeSongsFromPlaylist(
            @Path("playlistId") String playlistId,
            @Body Map<String, List<String>> songs
    );

    // [DELETE] /api/common/playlist/delete/{id}
    @DELETE("/api/common/playlist/delete/{id}")
    Call<Void> deletePlaylist(@Path("id") String id);
}
