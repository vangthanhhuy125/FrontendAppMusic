package com.example.manhinhappmusic.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.manhinhappmusic.model.Song;

public class SongRepository implements AppRepository<Song>{

    private static SongRepository instance;
    private SongRepository()
    {

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

        new Thread(() -> {
        }).start();
        return song;
    }
}
