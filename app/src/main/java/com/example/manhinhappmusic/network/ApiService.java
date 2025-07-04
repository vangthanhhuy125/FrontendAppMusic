package com.example.manhinhappmusic.network;


import com.example.manhinhappmusic.dto.AddSongsRequest;
import com.example.manhinhappmusic.dto.AuthResponse;
import com.example.manhinhappmusic.dto.ChangePasswordRequest;
import com.example.manhinhappmusic.dto.FeedbackResponse;
import com.example.manhinhappmusic.dto.ForgotPasswordRequest;
import com.example.manhinhappmusic.dto.GenreResponse;
import com.example.manhinhappmusic.dto.LoginRequest;
import com.example.manhinhappmusic.dto.MessageResponse;
import com.example.manhinhappmusic.dto.MultiResponse;
import com.example.manhinhappmusic.dto.RegisterRequest;
import com.example.manhinhappmusic.dto.ResetPasswordRequest;
import com.example.manhinhappmusic.dto.SongResponse;
import com.example.manhinhappmusic.dto.UserResponse;
import com.example.manhinhappmusic.dto.VerifyRequest;
import com.example.manhinhappmusic.model.Feedback;
import com.example.manhinhappmusic.model.Genre;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.model.User;

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
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    String BASE_URL = "http://<YOUR-URL>:8081";

    @GET("/api/common/playlist/{playlistId}")
    Call<Playlist> getPlaylistById(@Path("playlistId") String id);
    @GET("/api/common/playlist")
    Call<List<Playlist>> getAllPlaylists();
    @PATCH("/api/common/playlist/change/{playlistId}")
    Call<Playlist> editPlaylist(@Path("playlistId") String id, @Body Map<String, Object> updates);
    @GET("/api/common/playlist/new-releases")
    Call<List<Playlist>> getNewReleasePlaylists();
    @GET("/api/common/playlist/featured")
    Call<List<Playlist>> getFeaturedPlaylists();
    @GET("/api/common/playlist/public-playlists")
    Call<List<Playlist>> getAllPublicPlaylists();
    @POST("/api/common/playlist/addSongs")
    Call<ResponseBody> addSongsToPlaylist(@Body AddSongsRequest changes);
    @GET("/api/common/playlist/{playlistId}/songs")
    Call<List<SongResponse>> getAllSongs(@Path("playlistId") String id);
    @GET("/api/common/song/{songId}")
    Call<Song> getSongById(@Path("songId") String id);
    @GET("/api/common/song/recently-songs")
    Call<List<Song>> getRecentlySongs();
    @POST("/api/register")
    Call<MessageResponse> register(@Body RegisterRequest request);
    @POST("/api/verify-email")
    Call<AuthResponse> verifyEmail(@Body VerifyRequest request);
    @POST("/api/resend-otp")
    Call<String> resendOtp(@Body Map<String, String> emailBody);
    @POST("/api/login")
    Call<AuthResponse> login(@Body LoginRequest request);
    @POST("/api/password/forgot")
    Call<MessageResponse> forgotPassword(@Body ForgotPasswordRequest request);
    @POST("/api/password/change")
    Call<String> changePassword(@Body ChangePasswordRequest request);
    @POST("/api/password/reset-request")
    Call<String> sendOtp(@Query("email") String email);
    @POST("/api/password/reset")
    Call<MessageResponse> resetPassword(@Body ResetPasswordRequest request);
    @Multipart
    @POST("/api/common/playlist/create")
    Call<ResponseBody> createPlaylist(
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("songs") List<RequestBody> songs,
            @Part MultipartBody.Part thumbnail
    );
    @POST("/api/common/library/add/{playlistId}")
    Call<ResponseBody> addPlaylistToLibrary(@Path("playlistId") String id);
    @DELETE("/api/common/library/add/{playlistId}")
    Call<ResponseBody> removePlaylistFromLibrary(@Path("playlistId") String id);
    @GET("/api/common/song/search")
    Call<List<SongResponse>> searchSongs(@Query("keyword") String keyword);
    @GET("/api/common/song/search/multi")
    Call<List<MultiResponse>> search(@Query("keyword") String keyword);
    @DELETE("/api/common/playlist/delete/{id}")
    Call<ResponseBody> deletePlaylist(@Path("id") String id);
    @PATCH("/api/common/playlist/{playlistId}/addSongs")
    Call<Playlist> addSongs(@Path("playlistId") String id, @Body Map<String, List<String>> changes);

    @PATCH("/api/common/playlist/{playlistId}/removeSongs")
    Call<Playlist> removeSongs(@Path("playlistId") String id, @Body Map<String, List<String>> changes);
    @GET("api/common/song/{id}/playlist")
    Call<List<String>> getPlaylistsSongBelongs(@Path("id") String id);
    @GET("/api/common/song/new-release")
    Call<List<Song>> getNewReleaseSongs();
    @POST("/api/common/song/add")
    Call<ResponseBody> addSong(
            @Part MultipartBody.Part file,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part coverImage,
            @Part List<MultipartBody.Part> genreId, // nếu genreId là danh sách
            @Part MultipartBody.Part lyrics // có thể null
    );
    String STREAMING_URL = BASE_URL + "/api/common/song/stream";
    @GET("/api/common/song/artist/{artistId}")
    Call<List<Song>> getSongsByArtistId(@Path("artistId") String artistId);
    @GET("/api/common/users/trending-artists")
    Call<List<User>> getTrendingArtist();

    @Multipart
    @POST("/api/admin/genres")
    Call<Genre> createGenre(
            @Part MultipartBody.Part thumbnail,
            @Part("name") RequestBody name,
            @Part("description") RequestBody description
    );
    @Multipart
    @PUT("/api/admin/genres/{id}")
    Call<Genre> updateGenre(
            @Path("id") String id,
            @Part MultipartBody.Part thumbnail,
            @Part("name") RequestBody name,
            @Part("description") RequestBody description
    );
    @DELETE("/api/admin/genres/{id}")
    Call<Void> deleteGenre(@Path("id") String id);
    @GET("/api/common/genres")
    Call<List<Genre>> getAllGenres();
    @GET("/api/common/genres/{id}")
    Call<GenreResponse> getGenreById(@Path("id") String id);
    @GET("/api/admin/users")
    Call<List<User>> getAllUsers();
    @GET("/api/admin/users/artists")
    Call<List<User>> getAllArtists();
    @GET("/api/admin/users/{id}")
    Call<User> getUserById(@Path("id") String id);

    @DELETE("/api/admin/users/delete/{id}")
    Call<String> deleteUser(@Path("id") String id);
    @GET("api/common/users/me")
    Call<User> getProfile();
    @GET("api/common/users/search")
    Call<List<User>> searchUsersByName(@Query("keyword") String keyword);
    @PATCH("api/common/users/me/change")
    @Multipart
    Call<User> patchUser(
            @Part("fullName") RequestBody fullName,
            @Part MultipartBody.Part avatar
    );
    @GET("api/common/song/popular")
    Call<List<Song>> getPopularSongs();

    @Multipart
    @POST("/api/common/song/add")
    Call<String> addSong(
            @Part MultipartBody.Part file,
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part("coverImageUrl") RequestBody coverImageUrl
    );

    @Multipart
    @PUT("/api/common/song/edit/{id}")
    Call<Song> editSong(
            @Path("id") String id,
            @Part MultipartBody.Part coverImage,  // Có thể null
            @Part("title") RequestBody title,
            @Part("description") RequestBody description,
            @Part List<MultipartBody.Part> genreId // từng phần tử genreId là @Part("genreId") => phải tách từng genre
    );
    @PUT("/api/common/song/{genreId}/remove/{songId}")
    Call<String> removeSongFromGenre(
            @Path("genreId") String genreId,
            @Path("songId") String songId
    );
    @GET("/api/common/song/genre/{genreId}")
    Call<List<Song>> getSongsByGenre(@Path("genreId") String genreId);
    @GET("/api/admin/feedbacks")
    Call<List<Feedback>> getAllFeedbacks();
    @PUT("/api/admin/feedbacks/{id}/reply")
    Call<Feedback> replyFeedback(@Path("id") String id, @Body Feedback updates);
    @DELETE("/api/admin/feedbacks/{id}")
    Call<Void> deleteFeedback(@Path("id") String id);
    @POST("/api/common/feedbacks")
    Call<FeedbackResponse> sendFeedback(@Body Feedback feedback);
    @GET("/api/common/feedbacks/me")
    Call<List<Feedback>> getMyFeedbacks();
}
