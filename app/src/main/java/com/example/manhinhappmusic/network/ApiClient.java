package com.example.manhinhappmusic.network;

import com.example.manhinhappmusic.R;
import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public final String baseUrl = "http://localhost:8081";
    private Retrofit retrofit ;
    private ApiService apiService;

    public static ApiClient instance;

    public static ApiClient getInstance() {
        if(instance == null)
        {
            instance = new ApiClient();
        }
        return instance;
    }
    private ApiClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public ApiService getApiService()
    {
        return apiService;
    }
}
