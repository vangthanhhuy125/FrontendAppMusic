package com.example.manhinhappmusic;

import androidx.lifecycle.ViewModel;

public class NowPlayingSongViewModel extends ViewModel {
    private MediaPlayerManager mediaPlayerManager;

    public MediaPlayerManager getMediaPlayerManager() {
        return mediaPlayerManager;
    }

    public void setMediaPlayerManager(MediaPlayerManager mediaPlayerManager) {
        this.mediaPlayerManager = mediaPlayerManager;
    }
}
