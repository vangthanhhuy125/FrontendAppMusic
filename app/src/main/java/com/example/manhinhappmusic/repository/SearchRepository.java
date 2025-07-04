package com.example.manhinhappmusic.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.manhinhappmusic.dto.MultiResponse;
import com.example.manhinhappmusic.dto.PlaylistResponse;
import com.example.manhinhappmusic.dto.SongResponse;
import com.example.manhinhappmusic.dto.UserResponse;
import com.example.manhinhappmusic.model.Artist;
import com.example.manhinhappmusic.model.ListItem;
import com.example.manhinhappmusic.model.Playlist;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository extends AppRepository{

    private static SearchRepository instance;
    public static SearchRepository getInstance()
    {
        if(instance == null)
            instance = new SearchRepository();
        return instance;
    }

    public LiveData<List<ListItem>> search(String keyword)
    {
        MutableLiveData<List<ListItem>> results = new MutableLiveData<>();
        apiClient.getApiService().search(keyword).enqueue(new Callback<List<MultiResponse>>() {
            @Override
            public void onResponse(Call<List<MultiResponse>> call, Response<List<MultiResponse>> response) {

                if(response.isSuccessful() && response.body() != null)
                {

                    List<ListItem> items = new ArrayList<>();
                    for(MultiResponse multiResponse : response.body())
                    {

                        if(multiResponse.getType().equals("song"))
                        {
                            items.add((SongResponse) multiResponse.getData());
                        }
                        else if(multiResponse.getType().equals("playlist"))
                        {
                            items.add((PlaylistResponse) multiResponse.getData());
                        }
                        else if(multiResponse.getType().equals("artist"))
                        {
                            items.add((UserResponse) multiResponse.getData());
                        }
                    }

                    results.setValue(items);
                }
            }

            @Override
            public void onFailure(Call<List<MultiResponse>> call, Throwable throwable) {

                if(callback != null)
                    callback.onError(throwable);
            }
        });
        return results;
    }

    public LiveData<ListItem> getItemById(String id) {
        return null;
    }


    public LiveData<List<ListItem>> getAll() {
        return null;
    }
}
