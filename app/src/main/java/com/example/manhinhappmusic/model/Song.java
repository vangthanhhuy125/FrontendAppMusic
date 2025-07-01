package com.example.manhinhappmusic.model;


import com.example.manhinhappmusic.dto.MultiResponseImp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

//import android.os.Bundle;

@Data
@AllArgsConstructor
public class Song extends MultiResponseImp implements ListItem{
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


    @Override
    public ListItemType getItemType() {
        return ListItemType.SONG;
    }

    @Override
    public List<String> getSearchKeyWord() {
        List<String> keyWords = new ArrayList<>();
        keyWords.add(title);
        //keyWords.add(artist_id);
        return keyWords;    }

    @Override
    public String getType() {
        return "song";
    }
}
