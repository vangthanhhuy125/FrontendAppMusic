package com.example.manhinhappmusic.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.manhinhappmusic.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SongViewModel extends ViewModel {

    private final MutableLiveData<List<Song>> songListLiveData = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Song>> getSongList() {
        return songListLiveData;
    }

    public void addSong(Song song) {
        List<Song> currentList = songListLiveData.getValue();
        if (currentList != null) {
            currentList.add(song);
            songListLiveData.setValue(currentList);
        }
    }

    public void removeSong(Song song) {
        List<Song> currentList = songListLiveData.getValue();
        if (currentList != null) {
            currentList.remove(song);
            songListLiveData.setValue(currentList);
        }
    }

    public void setSongs(List<Song> songs) {
        songListLiveData.setValue(songs);
    }

    public void deleteSong(Song song) {
        List<Song> currentList = songListLiveData.getValue();
        if (currentList != null) {
            currentList.removeIf(s -> s.getId().equals(song.getId()));
            songListLiveData.setValue(new ArrayList<>(currentList));
        }
    }
}