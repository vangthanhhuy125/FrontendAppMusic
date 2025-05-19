package com.example.manhinhappmusic;

import java.util.ArrayList;

public class Song {
    private String id;
    private String artistId;
    private String description;
    private String title;
    private String audioUrl;
    private String coverImageUrl;
    private ArrayList<String> genreId;
    private Boolean isApproved;
    private Boolean isPublic;
    private String lyric;
    private Double duration;
    private Double views;

    // Constructors, Getters và Setters
    public Song() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
