package com.example.manhinhappmusic.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final String token;
    public AuthInterceptor(String token)
    {
        this.token = token;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder()
                .header("Authorization", "Bearer " + token);
        Request request = builder.build();
        return chain.proceed(request);
    }
}
