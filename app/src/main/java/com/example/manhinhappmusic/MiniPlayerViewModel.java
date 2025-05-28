package com.example.manhinhappmusic;

import androidx.lifecycle.ViewModel;

public class MiniPlayerViewModel extends ViewModel {
    private Playlist playlist;
    private int currentPosition;
    private Song currentSong;

    private MediaPlayerManager mediaPlayerManager;

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(Song currentSong) {
        this.currentSong = currentSong;
    }

    public MediaPlayerManager getMediaPlayerManager() {
        return mediaPlayerManager;
    }

    public void setMediaPlayerManager(MediaPlayerManager mediaPlayerManager) {
        this.mediaPlayerManager = mediaPlayerManager;
    }
}
