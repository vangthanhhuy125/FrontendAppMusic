package com.example.manhinhappmusic.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Song extends BaseDocument {
    private String id;
    private String artistId;
    private String description;
    private String title;
    private String audioUrl;
    private String coverImageUrl;
    private List<String> genreId;
    private Boolean isApproved;
    private Boolean isPublic;
    private String lyric;
    private Double duration;
    private Double views;

    public Song(String id, String title, String artistId, String description, String audioUrl, String coverImageUrl, List<String> genreId, Boolean isApproved, Boolean isPublic, String lyric, Double duration, Double views) {
        this.id = id;
        this.title = title;
        this.artistId = artistId;
        this.description = description;
        this.audioUrl = audioUrl;
        this.coverImageUrl = coverImageUrl;
        this.genreId = new ArrayList<String>();
        genreId.addAll(genreId);
        this.isApproved = isApproved;
        this.isPublic = isPublic;
        this.lyric = lyric;
        this.duration = duration;
        this.views = views;
    }

    public void setGenreId(List<String> genreId) {
        this.genreId.clear();
        this.genreId.addAll(genreId);
    }
}
