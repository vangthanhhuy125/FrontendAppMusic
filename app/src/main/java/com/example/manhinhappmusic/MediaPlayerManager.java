package com.example.manhinhappmusic;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MediaPlayerManager {
    public interface OnCompletionListener{
        void onCompletion();
    }
    public interface OnPlayingStateChangeListener{
        void onPlayingStateChange(boolean isPlaying);
    }

    public interface OnPlayingSongChangeListener{
        void onPlayingSongChange(Song song);
    }

    private Map<String,OnCompletionListener> onCompletionListeners = new HashMap<>();
    private Map<String,OnPlayingStateChangeListener> onPlayingStateChangeListeners = new HashMap<>();
    private Map<String,OnPlayingSongChangeListener> onPlayingSongChangeListeners = new HashMap<>();
    public  MediaPlayer mediaPlayer;
    private  Context context;
    private  List<Song> playlist;
    private  List<Song> shufflePlaylist;
    public  int currentPosition = -1;
    private  Song currentSong;
    public boolean isShuffle = false;
    private boolean isPlayingNextSong = true;
    public static enum RepeatMode{
        NONE,
        REPEAT_ALL,
        REPEAT_ONE;
    }
    private RepeatMode repeatMode = RepeatMode.NONE;


    private static MediaPlayerManager instance;

    public static MediaPlayerManager getInstance(@Nullable Context context) {
        if(instance == null && context != null)
        {
            instance = new MediaPlayerManager(context);
        }
        return  instance;
    }

    private MediaPlayerManager(Context context)
    {
        this.context = context;
    }

    private void complete(MediaPlayer mp)
    {
        if(repeatMode != RepeatMode.REPEAT_ONE)
        {
            if(!isShuffle)
            {
                if(playlist != null)
                {
                    loadNextSong(playlist);
                }
            }
            else
            {
                if(shufflePlaylist != null)
                {
                    loadNextSong(shufflePlaylist);
                }
            }

            for (OnCompletionListener onCompletionListener: onCompletionListeners.values()) {
                 onCompletionListener.onCompletion();
            }
        }
        else
        {
            mediaPlayer.seekTo(0);
            play();
        }
    }


    public void shuffle()
    {
            shufflePlaylist = new ArrayList<>(playlist);
            shufflePlaylist.remove(currentSong);
            Collections.shuffle(shufflePlaylist);
            shufflePlaylist.add(0,currentSong);
            currentPosition = 0;

    }

    public void play()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.start();
            for(OnPlayingStateChangeListener onPlayingStateChangeListener: onPlayingStateChangeListeners.values())
            {
                onPlayingStateChangeListener.onPlayingStateChange(mediaPlayer.isPlaying());
            }

        }
    }

    public void pause()
    {
        if(mediaPlayer != null)
        {
            mediaPlayer.pause();
            for(OnPlayingStateChangeListener onPlayingStateChangeListener: onPlayingStateChangeListeners.values())
            {
                onPlayingStateChangeListener.onPlayingStateChange(mediaPlayer.isPlaying());
            }
        }

    }

    public void skipNext()
    {
        if(!isShuffle){
            loadNextSong(playlist);

        }
        else {
            loadNextSong(shufflePlaylist);
        }

        for (OnCompletionListener onCompletionListener: onCompletionListeners.values()) {
            onCompletionListener.onCompletion();
        }
    }

    public void skipPrevious()
    {
        if(!isShuffle){
            loadPreviousSong(playlist);
        }
        else {
            loadPreviousSong(shufflePlaylist);
        }

        for (OnCompletionListener onCompletionListener: onCompletionListeners.values()) {
            onCompletionListener.onCompletion();
        }
    }

    public void loadNextSong(List<Song> playlist)
    {
        if(currentPosition < playlist.size() - 1)
        {
            isPlayingNextSong = true;
            currentSong = playlist.get(++currentPosition);
            //mediaPlayer.pause();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(context, currentSong.getAudioResID());
            mediaPlayer.setOnCompletionListener(this::complete);
            play();
        }
        else if(repeatMode == RepeatMode.REPEAT_ALL)
        {
            isPlayingNextSong = true;
            currentSong = playlist.get(0);
            //mediaPlayer.pause();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(context, currentSong.getAudioResID());
            mediaPlayer.setOnCompletionListener(this::complete);
            play();
        }
        else {
            isPlayingNextSong = false;
        }
    }

    public void loadPreviousSong(List<Song> playlist)
    {
        if(currentPosition > 0)
        {
            isPlayingNextSong = true;
            currentSong = playlist.get(--currentPosition);
            //mediaPlayer.pause();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(context, currentSong.getAudioResID());
            mediaPlayer.setOnCompletionListener(this::complete);
            play();
        }

    }


    public Song getCurrentSong() {
        return currentSong;
    }

    public RepeatMode getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(RepeatMode repeatMode) {
        this.repeatMode = repeatMode;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public List<Song> getPlaylist() {
        return playlist;
    }

    public void clearAllStates()
    {
        isShuffle = false;
        repeatMode = RepeatMode.NONE;
        shufflePlaylist = null;
    }


    public void addOnCompletionListener(OnCompletionListener onCompletionListener, String className)
    {
        if(onCompletionListener != null)
            onCompletionListeners.put(className, onCompletionListener);
    }

    public void addOnPlayingStateChangeListener(OnPlayingStateChangeListener onPlayingStateChangeListener, String className) {
        if(onPlayingStateChangeListener != null)
            onPlayingStateChangeListeners.put(className, onPlayingStateChangeListener);
    }

    public void addOnPlayingSongChangeListener(OnPlayingSongChangeListener onPlayingSongChangeListener, String className)
    {
        if(onPlayingSongChangeListener != null)
            onPlayingSongChangeListeners.put(className, onPlayingSongChangeListener);
    }

    public void removeOnCompletionListener(String className) {
            onCompletionListeners.remove(className);
    }
    public void removeOnPlayingStateChangeListener(String className) {
            onPlayingStateChangeListeners.remove(className);
    }

    public void removeOnPlayingSongChangeListener(String className)
    {
            onPlayingSongChangeListeners.remove(className);
    }

    public boolean isPlayingNextSong() {
        return isPlayingNextSong;
    }

    public void setPlaylist(List<Song> playlist) {

        clearAllStates();
       this.playlist = playlist;
    }

    public void setCurrentSong(int currentPosition)
    {
        if(playlist != null && playlist.size() > currentPosition)
        {
            this.currentPosition = currentPosition;
            this.currentSong = playlist.get(currentPosition);
            if(mediaPlayer != null)
                mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(context, currentSong.getAudioResID());
            mediaPlayer.setOnCompletionListener(this::complete);
            for(OnPlayingSongChangeListener onPlayingSongChangeListener: onPlayingSongChangeListeners.values())
            {
                onPlayingSongChangeListener.onPlayingSongChange(currentSong);
            }
        }
    }
}
