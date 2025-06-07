package com.example.manhinhappmusic.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.manhinhappmusic.TestData;
import com.example.manhinhappmusic.model.Genre;

import java.util.List;

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

    @Override
    public LiveData<List<Genre>> getAll() {
        return null;
    }
}
