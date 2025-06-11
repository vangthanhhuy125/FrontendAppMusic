package com.example.manhinhappmusic.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.manhinhappmusic.api.admin.AdminCommentApi;
import com.example.manhinhappmusic.api.admin.AdminFeedbackApi;
import com.example.manhinhappmusic.api.admin.AdminGenreApi;
import com.example.manhinhappmusic.api.admin.AdminUserApi;
import com.example.manhinhappmusic.api.artist.ArtistRequestApi;
import com.example.manhinhappmusic.api.artist.SongApi;
import com.example.manhinhappmusic.api.authentication.AuthApi;
import com.example.manhinhappmusic.api.authentication.PasswordApi;
import com.example.manhinhappmusic.api.common.CommonCommentApi;
import com.example.manhinhappmusic.api.common.CommonPlaylistApi;
import com.example.manhinhappmusic.api.common.CommonSongApi;
import com.example.manhinhappmusic.api.common.CommonUserApi;
import com.example.manhinhappmusic.api.common.NotificationApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "http://10.0.2.2:8081";

    private static Retrofit retrofitNoAuth;  // dùng cho api không cần token

    // Retrofit không cần token (ví dụ AuthApi, PasswordApi forgot, reset)
    public static Retrofit getRetrofitNoAuth() {
        if (retrofitNoAuth == null) {
            retrofitNoAuth = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient.Builder()
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .build())
                    .build();
        }
        return retrofitNoAuth;
    }

    // Retrofit có token, build mới mỗi lần để lấy đúng token mới nhất
    public static Retrofit getRetrofitWithAuth(Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    SharedPreferences prefs = context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
                    String token = prefs.getString("JWT_TOKEN", null);

                    Request.Builder requestBuilder = original.newBuilder();
                    if (token != null) {
                        requestBuilder.header("Authorization", "Bearer " + token);
                    }

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    // Api không cần token
    public static AuthApi getAuthApi() {
        return getRetrofitNoAuth().create(AuthApi.class);
    }

    public static PasswordApi getPasswordApi() {
        return getRetrofitNoAuth().create(PasswordApi.class);
    }

    // Api cần token
    public static AdminFeedbackApi getAdminFeedbackApi(Context context) {
        return getRetrofitWithAuth(context).create(AdminFeedbackApi.class);
    }

    public static AdminGenreApi getAdminGenreApi(Context context) {
        return getRetrofitWithAuth(context).create(AdminGenreApi.class);
    }

    public static AdminUserApi getAdminUserApi(Context context) {
        return getRetrofitWithAuth(context).create(AdminUserApi.class);
    }

    public static ArtistRequestApi getArtistRequestApi(Context context) {
        return getRetrofitWithAuth(context).create(ArtistRequestApi.class);
    }

    public static SongApi getSongApi(Context context) {
        return getRetrofitWithAuth(context).create(SongApi.class);
    }

    public static AdminCommentApi getAdminCommentApi(Context context) {
        return getRetrofitWithAuth(context).create(AdminCommentApi.class);
    }

    public static PasswordApi getAuthenticatedPasswordApi(Context context) {
        return getRetrofitWithAuth(context).create(PasswordApi.class);
    }

    public static CommonCommentApi getCommonCommentApi(Context context) {
        return getRetrofitWithAuth(context).create(CommonCommentApi.class);
    }

    public static CommonPlaylistApi getCommonPlaylistApi(Context context) {
        return getRetrofitWithAuth(context).create(CommonPlaylistApi.class);
    }

    public static CommonSongApi getCommonSongApi(Context context) {
        return getRetrofitWithAuth(context).create(CommonSongApi.class);
    }

    public static CommonUserApi getCommonUserApi(Context context) {
        return getRetrofitWithAuth(context).create(CommonUserApi.class);
    }

    public static NotificationApi getNotificationApi(Context context) {
        return getRetrofitWithAuth(context).create(NotificationApi.class);
    }
}
