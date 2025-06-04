package com.example.manhinhappmusic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ArtistRepository implements AppRepository<User>{

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
}
