package com.example.manhinhappmusic.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Data
public class SongRepository implements AppRepository<Song> {

    private static SongRepository instance;
//    private MutableLiveData<List<Song>> songs = new MutableLiveData<>();
    private Song currentSong;
    private SongRepository()
    {

//        songs.setValue(TestData.songList);
    }

    public static SongRepository getInstance()
    {
        if(instance == null)
        {
            instance = new SongRepository();
        }

        return instance;
    }
    @Override
    public LiveData<Song> getItemById(String id) {
        MutableLiveData<Song> song = new MutableLiveData<>();

        Call<Song> call = apiClient.getApiService().getSongById(id);
        call.enqueue(new Callback<Song>() {
            @Override
            public void onResponse(Call<Song> call, Response<Song> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    song.setValue(response.body());
                }
                else
                {
                    Log.e("API", "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Song> call, Throwable throwable) {
                Log.e("API", "Response error: " + throwable.getMessage());
            }
        });
        return song;
    }

    public LiveData<List<Song>> getAll()
    {
        MutableLiveData<List<Song>> songs = new MutableLiveData<>();

        return songs;
    }

    public LiveData<List<Song>> getAll(List<String> ids)
    {
        MutableLiveData<List<Song>> songs = new MutableLiveData<>();
        List<Song> songList = new ArrayList<>();

        ApiService apiService = apiClient.getApiService();

        for(String id: ids)
        {
            apiService.getSongById(id).enqueue(new Callback<Song>() {
                @Override
                public void onResponse(Call<Song> call, Response<Song> response) {
                    if(response.isSuccessful() && response.body() != null)
                    {
                        songList.add(response.body());
                        songs.setValue(songList);
                    }
                    else
                    {
                        Log.e("API", "Response error: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Song> call, Throwable throwable) {
                    Log.e("API", "Response error: " + throwable.getMessage());
                }
            });
        }
        return songs;
    }

    public LiveData<List<Song>> searchSongs(String title)
    {
        MutableLiveData<List<Song>> songs = new MutableLiveData<>();

        apiClient.getInstance().getApiService().searchSongs(title).enqueue(new Callback<List<Song>>() {
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
