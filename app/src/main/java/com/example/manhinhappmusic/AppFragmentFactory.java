package com.example.manhinhappmusic;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

import java.util.List;

public class AppFragmentFactory extends FragmentFactory {

    private List<Playlist> library;
    private Playlist playlist;
    private Song song;
    private MediaPlayerManager mediaPlayerManager;

    public AppFragmentFactory(List<Playlist> library, Playlist playlist, Song song, MediaPlayerManager mediaPlayerManager){
        this.library = library;
        this.playlist = playlist;
        this.song = song;
        this.mediaPlayerManager = mediaPlayerManager;
    }

    @NonNull
    @Override
    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
       if(className.equals(UserLibraryFragment.class.getName()))
       {
           return new UserLibraryFragment(library);
       }
       else if(className.equals(UserPlaylistFragment.class.getName()))
       {
           return new UserPlaylistFragment(playlist);
       }
       else if(className.equals(MiniPlayerFragment.class.getName()))
       {
           return new MiniPlayerFragment(mediaPlayerManager);
       }
       else if(className.equals(NowPlayingSongFragment.class.getName()))
       {
           return  new NowPlayingSongFragment(mediaPlayerManager);
       }
        return super.instantiate(classLoader, className);

    }

    public void setLibrary(List<Playlist> library) {
        this.library = library;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void setMediaPlayerManager(MediaPlayerManager mediaPlayerManager) {
        this.mediaPlayerManager = mediaPlayerManager;
    }

    public List<Playlist> getLibrary() {
        return library;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public Song getSong() {
        return song;
    }

    public MediaPlayerManager getMediaPlayerManager() {
        return mediaPlayerManager;
    }
}
