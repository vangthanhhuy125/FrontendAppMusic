package com.example.manhinhappmusic.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.manhinhappmusic.network.ApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private boolean isShuffle = false;
    private boolean isPlayingNextSong = true;
    private boolean isPrepared = false;

    private Map<String, String> headers =  new HashMap<>();
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
        SharedPreferences preferences = context.getSharedPreferences("AppPreferences", context.MODE_PRIVATE);
        headers.put("Authorization", "Bearer " + preferences.getString("token", ""));
        mediaPlayer = new MediaPlayer();
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


    private void shuffle()
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
            mediaPlayer = new MediaPlayer();
            try{
                isPrepared = false;
                mediaPlayer.setAudioAttributes( new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());
                mediaPlayer.setDataSource(context, Uri.parse(ApiService.STREAMING_URL + "/" + currentSong.getId()), headers);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(mp -> {
                    isPrepared = true;
                    mediaPlayer.setOnCompletionListener(this::complete);
                    play();
                });
            }
            catch (Exception ex)
            {
                Log.e("Media player", ex.getMessage());
            }

//            mediaPlayer = MediaPlayer.create(context, currentSong.getAudioResID());
        }
        else if(repeatMode == RepeatMode.REPEAT_ALL)
        {
            isPlayingNextSong = true;
            currentSong = playlist.get(0);
            //mediaPlayer.pause();
            mediaPlayer.release();
            mediaPlayer = new MediaPlayer();
            try{
                isPrepared = false;
                mediaPlayer.setAudioAttributes( new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());
                mediaPlayer.setDataSource(context, Uri.parse(ApiService.STREAMING_URL + "/" + currentSong.getId()), headers);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(mp -> {
                    isPrepared = true;
                    mediaPlayer.setOnCompletionListener(this::complete);
                    play();
                });
            }
            catch (Exception ex)
            {
                Log.e("Media player", ex.getMessage());
            }
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
            mediaPlayer = new MediaPlayer();
            isPrepared = false;
            try{
                mediaPlayer.setAudioAttributes( new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());
                mediaPlayer.setDataSource(context, Uri.parse(ApiService.STREAMING_URL + "/" + currentSong.getId()), headers);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(mp -> {
                    isPrepared = true;
                    mediaPlayer.setOnCompletionListener(this::complete);
                    play();
                });
            }
            catch (Exception ex)
            {
                Log.e("Media player", ex.getMessage());
            }
        }

    }

    public boolean isShuffle() {
        return isShuffle;
    }

    public void setShuffle(boolean shuffle) {
        isShuffle = shuffle;
        if(isShuffle)
            shuffle();
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
            Log.d("media", currentSong.getTitle());
            if(mediaPlayer != null)
            {
                mediaPlayer.release();
            }
            try{
                isPrepared = false;
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioAttributes( new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());
                mediaPlayer.setDataSource(context, Uri.parse(ApiService.STREAMING_URL + "/" + currentSong.getId()), headers);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(mp -> {
                    isPrepared = true;
                    mediaPlayer.setOnCompletionListener(this::complete);
                    play();
                });
            }
            catch (Exception ex)
            {
                // Log.e("Media player", ex.getMessage());
            }
            for(OnPlayingSongChangeListener onPlayingSongChangeListener: onPlayingSongChangeListeners.values())
            {
                onPlayingSongChangeListener.onPlayingSongChange(currentSong);
            }
        }

    }


    public boolean isPrepared() {
        return isPrepared;
    }
}
