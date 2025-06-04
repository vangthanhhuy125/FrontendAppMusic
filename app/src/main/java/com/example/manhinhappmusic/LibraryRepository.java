package com.example.manhinhappmusic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
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
}
