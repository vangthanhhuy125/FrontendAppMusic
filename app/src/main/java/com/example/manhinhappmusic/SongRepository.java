package com.example.manhinhappmusic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class SongRepository implements AppRepository<Song>{

    private static SongRepository instance;
    private MutableLiveData<List<Song>> songs = new MutableLiveData<>();
    private SongRepository()
    {
        songs.setValue(TestData.songList);
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

//        new Thread(() -> {
//
//        }).start();

        song.setValue(TestData.getSongById(id));
        return song;
    }

    public LiveData<List<Song>> getAll()
    {
        return songs;
    }
}
