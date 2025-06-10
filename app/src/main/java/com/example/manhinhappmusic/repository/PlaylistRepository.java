package com.example.manhinhappmusic.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.manhinhappmusic.TestData;
import com.example.manhinhappmusic.dto.AuthResponse;
import com.example.manhinhappmusic.dto.LoginRequest;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.network.ApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
@Data
public class PlaylistRepository implements AppRepository<Playlist> {

    private static PlaylistRepository instance;
    private PlaylistRepository()
    {

    }

    private Playlist currentPlaylist;
    public static PlaylistRepository getInstance()
    {
        if(instance == null)
        {
            instance = new PlaylistRepository();
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

       apiClient.getApiService().getAllPlaylists().enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    playlists.setValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable throwable) {
                Log.e("API", "Response error: " + throwable.getMessage());
            }
        });

        return playlists;
    }

    public LiveData<List<Playlist>> create(String name)
    {
        MutableLiveData<List<Playlist>> playlists = new MutableLiveData<>();
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), "new");

        List<RequestBody> songBodies = new ArrayList<>();

        ApiClient.getInstance().getApiService().createPlaylist(nameBody, descBody, songBodies, null).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                apiClient.getApiService().getAllPlaylists().enqueue(new Callback<List<Playlist>>() {
                    @Override
                    public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                        if(response.isSuccessful() && response.body() != null)
                        {
                            playlists.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Playlist>> call, Throwable throwable) {

                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });

        return playlists;
    }

    public LiveData<Playlist> edit(String id, Map<String, Object> changes)
    {
        MutableLiveData<Playlist> playlist = new MutableLiveData<>();

        apiClient.getApiService().editPlaylist(id, changes).enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if(response.isSuccessful() && response.body() != null)
                    playlist.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable throwable) {

            }
        });

        return playlist;
    }

    public LiveData<Boolean> delete(String id)
    {
        MutableLiveData<Boolean> isDeleted = new MutableLiveData<>();
        apiClient.getApiService().deletePlaylist(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isDeleted.setValue(Boolean.valueOf(true));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });

        return isDeleted;
    }

    public LiveData<Playlist> addSongs(String id, List<String> songs)
    {
        MutableLiveData<Playlist> playlist = new MutableLiveData<>();
        Map<String, List<String>> changes = new HashMap<>();
        changes.put("songs", songs);
        apiClient.getApiService().addSongs(id, changes).enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if(response.isSuccessful() && response.body() != null)
                    playlist.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable throwable) {

            }
        });

        return playlist;
    }

    public LiveData<Playlist> removeSongs(String id, List<String> songs)
    {
        MutableLiveData<Playlist> playlist = new MutableLiveData<>();
        Map<String, List<String>> changes = new HashMap<>();
        changes.put("songs", songs);
        apiClient.getApiService().removeSongs(id, changes).enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    playlist.setValue(response.body());

                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable throwable) {

            }
        });

        return playlist;
    }

    public LiveData<List<Song>> getAllSongs(String id)
    {
        MutableLiveData<List<Song>> songs = new MutableLiveData<>();
        apiClient.getApiService().getAllSongs(id).enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    songs.setValue(response.body());

                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable throwable) {

            }
        });

        return songs;
    }


}
