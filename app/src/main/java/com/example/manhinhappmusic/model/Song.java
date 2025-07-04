package com.example.manhinhappmusic.model;


import com.example.manhinhappmusic.dto.MultiResponse;
import com.example.manhinhappmusic.dto.SongResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

//import android.os.Bundle;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class Song extends MultiResponse implements ListItem, Serializable {
    private String id;
    private String artistId;
    private String artistName;
    private String description;
    private String title;
    private String audioUrl;
    private String coverImageUrl;
    private ArrayList<String> genreId;
    private Boolean isApproved;
    private Boolean isPublic;
    private Double duration;
    private Integer views;
    private List<String> lyrics;

    @Override
    public ListItemType getItemType() {
        return ListItemType.SONG;
    }

    public Song(SongResponse response) {
        this.id = response.getId();
        this.title = response.getTitle();
        this.duration = response.getDuration();
        this.audioUrl = response.getAudioUrl();
        this.coverImageUrl = response.getCoverImageUrl();
    }
}
