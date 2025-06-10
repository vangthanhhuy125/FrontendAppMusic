package com.example.manhinhappmusic.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

//import android.os.Bundle;

@Data
@AllArgsConstructor
public class Song implements ListItem {
    private String id;
    private String artist_id;
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

    //Test purpose

    private int audioResID;
    private int coverImageResID;
    private String artistId;
    @Override
    public ListItemType getType() {
        return ListItemType.SONG;
    }

    @Override
    public List<String> getSearchKeyWord() {
        List<String> keyWords = new ArrayList<>();
        keyWords.add(title);
        //keyWords.add(artist_id);
        return keyWords;    }
}
