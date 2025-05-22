package com.example.manhinhappmusic;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MediaPlayerManager {
    public interface OnCompletionListener{
        void onCompletion();

    }

    private  OnCompletionListener completionListener;
    public  MediaPlayer mediaPlayer;
    private  Context context;
    private  List<Song> playlist;
    private  List<Song> shufflePlaylist;
    public  int currentPosition = -1;
    private  Song currentSong;
    public  boolean isShuffle = false;
    public static enum RepeatMode{
        NONE,
        REPEAT_ALL,
        REPEAT_ONE;
    }
    private RepeatMode repeatMode = RepeatMode.NONE;

    public MediaPlayerManager(Context context, List<Song> playlist, int currentPosition, OnCompletionListener completionListener)
    {
        this.context = context;
        this.playlist = playlist;
        this.currentPosition = currentPosition;
        this.completionListener = completionListener;
        mediaPlayer = MediaPlayer.create(context, playlist.get(currentPosition).getAudioResID());
        mediaPlayer.setOnCompletionListener(this::complete);
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

            if(completionListener != null)
                completionListener.onCompletion();
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
            mediaPlayer.start();
    }

    public void pause()
    {
        if(mediaPlayer != null)
            mediaPlayer.pause();
    }

    public void skipNext()
    {
        if(!isShuffle){
            loadNextSong(playlist);
            completionListener.onCompletion();
        }
        else {
            loadNextSong(shufflePlaylist);
            completionListener.onCompletion();
        }
    }

    public void skipPrevious()
    {
        if(!isShuffle){
            loadPreviousSong(playlist);
            completionListener.onCompletion();

        }
        else {
            loadPreviousSong(shufflePlaylist);
            completionListener.onCompletion();

        }
    }

    public void loadNextSong(List<Song> playlist)
    {
        if(currentPosition < playlist.size() - 1)
        {
            currentSong = playlist.get(++currentPosition);
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(context, currentSong.getAudioResID());
            mediaPlayer.setOnCompletionListener(this::complete);
            play();
        }
        else if(repeatMode == RepeatMode.REPEAT_ALL)
        {
            currentSong = playlist.get(0);
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(context, currentSong.getAudioResID());
            mediaPlayer.setOnCompletionListener(this::complete);
            play();
        }
    }

    public void loadPreviousSong(List<Song> playlist)
    {
        if(currentPosition > 0)
        {
            currentSong = playlist.get(--currentPosition);
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

    public void clear()
    {
        if(mediaPlayer != null)
            mediaPlayer.release();
        mediaPlayer = null;
        currentPosition = -1;
        currentSong = null;
        context = null;
        isShuffle = false;
        repeatMode = RepeatMode.NONE;
        playlist = null;
        shufflePlaylist = null;
    }
}
