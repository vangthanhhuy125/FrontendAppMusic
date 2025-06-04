package com.example.manhinhappmusic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class GenreRepository implements AppRepository<Genre> {

    private static GenreRepository instance;

    public static GenreRepository getInstance() {
        if(instance == null)
        {
            instance = new GenreRepository();
        }
        return  instance;
    }

    private GenreRepository()
    {

    }

    @Override
    public LiveData<Genre> getItemById(String id) {
        MutableLiveData<Genre> genre = new MutableLiveData<>();

        genre.setValue(TestData.getGenreById(id));
        return genre;
    }
}
