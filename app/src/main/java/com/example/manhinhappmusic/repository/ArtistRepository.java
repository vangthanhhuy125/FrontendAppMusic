package com.example.manhinhappmusic.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.manhinhappmusic.TestData;
import com.example.manhinhappmusic.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtistRepository extends AppRepository {

    private static ArtistRepository instance;

    public static ArtistRepository getInstance() {
        if(instance == null)
        {
            instance = new ArtistRepository();
        }
        return instance;
    }

    private ArtistRepository()
    {

    }


    public LiveData<User> getItemById(String id) {
        MutableLiveData<User> artist = new MutableLiveData<>();
        artist.setValue(TestData.getArtistById(id));
        return artist;
    }

    public LiveData<List<User>> getTrendingArtist() {
        MutableLiveData<List<User>> artists = new MutableLiveData<>();
        apiClient.getApiService().getTrendingArtist().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    artists.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable throwable) {
                callback.onError(throwable);
            }
        });
        return artists;
    }
}
