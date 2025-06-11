package com.example.manhinhappmusic.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.manhinhappmusic.TestData;
import com.example.manhinhappmusic.model.MusicDisplayItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MusicDisplayRepository implements AppRepository<MusicDisplayItem> {

    private MutableLiveData<List<MusicDisplayItem>> homeDisplayItems = new MutableLiveData<>();
    private static MusicDisplayRepository instance;
    public static MusicDisplayRepository getInstance()
    {
        if(instance == null)
        {
            instance = new MusicDisplayRepository();
        }

        return  instance;
    }

    public LiveData<List<MusicDisplayItem>> getAll(){
        return homeDisplayItems;
    }

    private MusicDisplayRepository()
    {
        homeDisplayItems.setValue(new ArrayList<>());
        homeDisplayItems.getValue().addAll(Arrays.asList(
                new MusicDisplayItem("1","Recently", new ArrayList<>(TestData.songList), MusicDisplayItem.HomeDisplayType.SONG),
                new MusicDisplayItem("3","New release", new ArrayList<>(TestData.userPlaylistList), MusicDisplayItem.HomeDisplayType.RELEASE_PLAYLIST),
                new MusicDisplayItem("2","Featuring", new ArrayList<>(TestData.mixPlaylistList), MusicDisplayItem.HomeDisplayType.MIX_PLAYLIST),
                new MusicDisplayItem("4", "Trending artists", new ArrayList<>(TestData.artistList), MusicDisplayItem.HomeDisplayType.ARTIST)

                ));

    }
    @Override
    public LiveData<MusicDisplayItem> getItemById(String id) {
        MutableLiveData<MusicDisplayItem> homeDisplayItem = new MutableLiveData<>();

        homeDisplayItem.setValue(
                homeDisplayItems.getValue().stream()
                        .filter(currentHomeDisplayItem -> currentHomeDisplayItem.getId().equals(id))
                        .findFirst()
                        .orElse(null)
        );
        return homeDisplayItem;
    }
}
