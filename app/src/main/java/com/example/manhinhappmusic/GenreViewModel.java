package com.example.manhinhappmusic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class GenreViewModel extends ViewModel {
    private final MutableLiveData<List<Genre>> genreListLiveData = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Genre>> getGenreList() {
        return genreListLiveData;
    }

    public void addGenre(Genre genre) {
        List<Genre> currentList = genreListLiveData.getValue();
        if (currentList != null) {
            currentList.add(genre);
            genreListLiveData.setValue(currentList);
        }
    }
}
