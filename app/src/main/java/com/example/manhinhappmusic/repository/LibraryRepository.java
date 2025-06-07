package com.example.manhinhappmusic.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.TestData;

import java.util.List;

public class LibraryRepository implements AppRepository<List<Playlist>> {

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

    @Override
    public LiveData<List<Playlist>> getItemById(String id) {

        MutableLiveData<List<Playlist>> library = new MutableLiveData<>();
//        new Thread(() -> {
//
//        }).start();

        library.setValue(TestData.userPlaylistList);
        return library;
    }

    @Override
    public LiveData<List<List<Playlist>>> getAll() {
        return null;
    }
}
