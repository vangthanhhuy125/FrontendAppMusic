package com.example.manhinhappmusic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;

@Data
public class Playlist {
    private String id;
    private String name;
    private String description;
    private List<String> songs = new ArrayList<>();
    private String userId;
    private String thumbnailUrl;
    private String artist;
    private int viewCount;
    private Boolean isPublic;
    private String createdAt;
    private String updatedAt;

    public Playlist(String id, String name, String description, List<String> songs, String userId, String thumbnailUrl, String artist, int viewCount, Boolean isPublic, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.songs = new ArrayList<String>();
        if (songs != null)
            this.songs.addAll(songs);
        this.userId = userId;
        this.thumbnailUrl = thumbnailUrl;
        this.artist = artist;
        this.viewCount = viewCount;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public List<String> getSongsList() {
        return Collections.unmodifiableList(songs);
    }
    public List<String> getModifiableSongsList() {
        return songs;
    }
    public void setSongs(List<String> songs) {
        this.songs.clear();
        this.songs.addAll(songs);
    }
    public void addSong(String song) {
        if (song != null)
            songs.add(song);
    }
}

