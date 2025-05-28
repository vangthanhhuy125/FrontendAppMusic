package com.example.manhinhappmusic;

import androidx.lifecycle.ViewModel;

public class UserPlaylistViewModel extends ViewModel {
    private Playlist playlist;

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
