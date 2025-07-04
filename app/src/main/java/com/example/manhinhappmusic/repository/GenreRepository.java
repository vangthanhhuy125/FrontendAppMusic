package com.example.manhinhappmusic.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.manhinhappmusic.TestData;
import com.example.manhinhappmusic.model.Genre;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenreRepository extends AppRepository{

    private static GenreRepository instance;

    public static GenreRepository getInstance() {
        if(instance == null)
        {
            instance = new GenreRepository();
        }
        return  instance;
    }

    private GenreRepository()
    {

    }




    public LiveData<List<Genre>> getAllGenres() {

        MutableLiveData<List<Genre>> genres = new MutableLiveData<>();
        apiClient.getApiService().getAllGenres().enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    genres.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable throwable) {
                callback.onError(throwable);
            }
        });
        return genres;
    }
}
