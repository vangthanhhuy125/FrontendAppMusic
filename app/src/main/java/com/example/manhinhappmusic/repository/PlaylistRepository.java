package com.example.manhinhappmusic.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.manhinhappmusic.TestData;
import com.example.manhinhappmusic.dto.AuthResponse;
import com.example.manhinhappmusic.dto.LoginRequest;
import com.example.manhinhappmusic.model.Playlist;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistRepository implements AppRepository<Playlist> {

    private static PlaylistRepository instance;
    private PlaylistRepository()
    {

    }
    private static String token;
    public static PlaylistRepository getInstance()
    {
        if(instance == null)
        {
            instance = new PlaylistRepository();
            apiClient.getApiService().login(new LoginRequest("23521766@gm.uit.edu.vn", "333")).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    token = response.body().getToken();
                    Log.d("Login", token);
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable throwable) {
                    Log.e("Login", throwable.getMessage());

                }
            });
        }
        return  instance;
    }
    @Override
    public LiveData<Playlist> getItemById(String id) {
        MutableLiveData<Playlist> playlist = new MutableLiveData<>();

        Call<Playlist> call = apiClient.getApiService().getPlaylistById(id);
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    playlist.setValue(response.body());
                }
                else {
                    Log.e("API", "Response Error: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable throwable) {
                Log.e("API", "Response error: " + throwable.getMessage());
            }
        });
        return playlist;
    }

    @Override
    public LiveData<List<Playlist>> getAll() {
        MutableLiveData<List<Playlist>> playlists = new MutableLiveData<>();

        Call<List<Playlist>> call = apiClient.getApiService().getAllPlaylists();
        call.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    playlists.setValue(response.body());
                }
                else {
                    Log.e("API", "Response Error: "+ response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable throwable) {
                Log.e("API", "Response error: " + throwable.getMessage());
            }
        });

        return playlists;
    }
}
