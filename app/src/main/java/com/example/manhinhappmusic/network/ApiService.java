package com.example.manhinhappmusic.network;


import com.example.manhinhappmusic.dto.AuthResponse;
import com.example.manhinhappmusic.dto.ChangePasswordRequest;
import com.example.manhinhappmusic.dto.ForgotPasswordRequest;
import com.example.manhinhappmusic.dto.LoginRequest;
import com.example.manhinhappmusic.dto.MessageResponse;
import com.example.manhinhappmusic.dto.MultiResponse;
import com.example.manhinhappmusic.dto.MultiResponseImp;
import com.example.manhinhappmusic.dto.PlaylistRequest;
import com.example.manhinhappmusic.dto.RegisterRequest;
import com.example.manhinhappmusic.dto.ResetPasswordRequest;
import com.example.manhinhappmusic.dto.SongResponse;
import com.example.manhinhappmusic.dto.VerifyRequest;
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
import retrofit2.http.Query;

public interface ApiService {

    String BASE_URL = "http://192.168.1.18:8081";
    @GET("/api/common/playlist/{playlistId}")
    Call<Playlist> getPlaylistById(@Path("playlistId") String id);

    @GET("/api/common/playlist")
    Call<List<Playlist>> getAllPlaylists();

    @GET("/api/common/playlist/{playlistId}/songs")
    Call<List<Song>> getAllSongs(@Path("playlistId") String id);

    @GET("/api/common/song/{songId}")
    Call<Song> getSongById(@Path("songId") String id);

    @POST("/api/register")
    Call<MessageResponse> register(@Body RegisterRequest request);

    // Xác thực OTP và hoàn tất đăng ký
    @POST("/api/verify-email")
    Call<AuthResponse> verifyEmail(@Body VerifyRequest request);

    // Gửi lại mã OTP (resend)
    @POST("/api/resend-otp")
    Call<String> resendOtp(@Body Map<String, String> emailBody);

    // Đăng nhập
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

    @PATCH("/api/common/playlist/change/{playlistId}")
    Call<Playlist> editPlaylist(@Path("playlistId") String id, @Body Map<String, Object> updates);

    @DELETE("/api/common/playlist/delete/{id}")
    Call<ResponseBody> deletePlaylist(@Path("id") String id);

    @PATCH("/api/common/playlist/{playlistId}/addSongs")
    Call<Playlist> addSongs(@Path("playlistId") String id, @Body Map<String, List<String>> changes);

    @PATCH("/api/common/playlist/{playlistId}/removeSongs")
    Call<Playlist> removeSongs(@Path("playlistId") String id, @Body Map<String, List<String>> changes);

    @GET("/api/common/song/search")
    Call<List<SongResponse>> searchSongs(@Query("keyword") String keyword);

    @GET("/api/common/song/search/multi")
    Call<List<MultiResponse>> search(@Query("keyword") String keyword);

    @GET("/api/common/song/new-release")
    Call<List<Song>> getNewReleaseSongs();
    String STREAMING_URL = BASE_URL + "/api/common/song/stream";

}
