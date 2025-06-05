package com.example.manhinhappmusic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;

@Data
public class Playlist extends BaseDocument {
    private String id;
    private String name;
    private String description;
    private List<Song> songs = new ArrayList<>();
    private String userId;
    private String thumbnailUrl;
    private String artist;
    private int viewCount;
    private Boolean isPublic;

    public Playlist(String id, String name, String description, List<Song> songs, String userId, String thumbnailUrl, String artist, int viewCount, Boolean isPublic) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.songs = new ArrayList<Song>();
        if (songs != null)
            this.songs.addAll(songs);
        this.userId = userId;
        this.thumbnailUrl = thumbnailUrl;
        this.artist = artist;
        this.viewCount = viewCount;
        this.isPublic = isPublic;
    }
    public List<Song> getSongsList() {
        return Collections.unmodifiableList(songs);
    }
    public List<Song> getModifiableSongsList() {
        return songs;
    }
    public void setSongs(List<Song> songs) {
        this.songs.clear();
        this.songs.addAll(songs);
    }
    public void addSong(Song song) {
        if (song != null)
            songs.add(song);
    }
}

