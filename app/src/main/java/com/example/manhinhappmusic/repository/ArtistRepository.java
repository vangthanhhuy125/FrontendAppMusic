package com.example.manhinhappmusic.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.manhinhappmusic.TestData;
import com.example.manhinhappmusic.model.User;

import java.util.List;

public class ArtistRepository implements AppRepository<User> {

    private static ArtistRepository instance;

    public static ArtistRepository getInstance() {
        if(instance == null)
        {
            instance = new ArtistRepository();
        }
        return instance;
    }

    private ArtistRepository()
    {

    }

    @Override
    public LiveData<User> getItemById(String id) {
        MutableLiveData<User> artist = new MutableLiveData<>();
        artist.setValue(TestData.getArtistById(id));
        return artist;
    }

    @Override
    public LiveData<List<User>> getAll() {
        return null;
    }
}
