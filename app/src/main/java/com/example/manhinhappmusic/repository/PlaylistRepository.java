package com.example.manhinhappmusic.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.manhinhappmusic.model.Playlist;

public class PlaylistRepository implements AppRepository<Playlist>{

    private static PlaylistRepository instance;
    private PlaylistRepository()
    {

    }

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

        new Thread(() -> {
        }).start();
        return playlist;
    }
}
