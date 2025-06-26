package com.example.manhinhappmusic.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.manhinhappmusic.dto.MultiResponse;
import com.example.manhinhappmusic.dto.MultiResponseImp;
import com.example.manhinhappmusic.dto.SongResponse;
import com.example.manhinhappmusic.model.Artist;
import com.example.manhinhappmusic.model.ListItem;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.model.Song;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository implements AppRepository<ListItem>{

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
                Log.d("apd", "" + response.body().size());

                if(response.isSuccessful() && response.body() != null)
                {

                    List<ListItem> items = new ArrayList<>();
                    for(MultiResponse multiResponse : response.body())
                    {
                        Log.d("apd", multiResponse.getType());

                        if(multiResponse.getType().equals("song"))
                        {
                            items.add((SongResponse) multiResponse);
                        }
                        else if(multiResponse.getType().equals("playlist"))
                        {
                            items.add((Playlist) multiResponse);
                        }
                        else if(multiResponse.getType().equals("artist"))
                        {
                            items.add((Artist) multiResponse);
                        }
                    }

                    results.setValue(items);
                }
            }

            @Override
            public void onFailure(Call<List<MultiResponse>> call, Throwable throwable) {

            }
        });
        return results;
    }

    @Override
    public LiveData<ListItem> getItemById(String id) {
        return null;
    }

    @Override
    public LiveData<List<ListItem>> getAll() {
        return null;
    }
}
