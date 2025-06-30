package com.example.manhinhappmusic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

//        new Thread(() -> {
//
//        }).start();

        playlist.setValue(TestData.getPlaylistById(id));
        return playlist;
    }
}
