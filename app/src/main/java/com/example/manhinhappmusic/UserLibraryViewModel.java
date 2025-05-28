package com.example.manhinhappmusic;

import androidx.lifecycle.ViewModel;

import java.util.List;

public class UserLibraryViewModel extends ViewModel {
    private List<Playlist> playlistList;

    public List<Playlist> getPlaylistList() {
        return playlistList;
    }

    public void setPlaylistList(List<Playlist> playlistList) {
        this.playlistList = playlistList;
    }
}
