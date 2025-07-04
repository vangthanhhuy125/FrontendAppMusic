package com.example.manhinhappmusic.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.TestData;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibraryRepository extends AppRepository {

    private static LibraryRepository instance;
    private LibraryRepository()
    {
    }

    public static LibraryRepository getInstance() {
        if(instance == null)
        {
            instance = new LibraryRepository();
        }

        return instance;
    }

    public LiveData<ResponseBody> addPlaylistToLibrary(String id)
    {
        MutableLiveData<ResponseBody> result = new MutableLiveData<>();
        apiClient.getApiService().addPlaylistToLibrary(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    result.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                callback.onError(throwable);
            }
        });
        return result;
    }

    public LiveData<ResponseBody> removePlaylistFromLibrary(String id)
    {
        MutableLiveData<ResponseBody> result = new MutableLiveData<>();
        apiClient.getApiService().addPlaylistToLibrary(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    result.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                callback.onError(throwable);
            }
        });
        return result;
    }
}
