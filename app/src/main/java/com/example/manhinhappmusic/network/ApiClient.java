package com.example.manhinhappmusic.network;

import android.util.Log;

import com.example.manhinhappmusic.dto.MultiResponse;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    private ApiService apiService;

    public static ApiClient instance;

    public static ApiClient getInstance() {
        if(instance == null)
        {
            instance = new ApiClient();
        }
        return instance;
    }
    private String token;
    private String role;

    public void createApiService()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .registerTypeAdapter(MultiResponse.class, new MultiResponseDeserializer())
                        .create()))
                .build();
        apiService = retrofit.create(ApiService.class);
    }
    public void createApiServiceWithToken(String token){
        this.token = token;
        Log.d("token", token);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .registerTypeAdapter(MultiResponse.class, new MultiResponseDeserializer())
                        .create()))
                .client(new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();
                return chain.proceed(newRequest);
            }
        }).build())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }

    public String getToken() {
        return token;
    }

    public String getRole(){return role;}
}
