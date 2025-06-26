package com.example.manhinhappmusic.model;

import com.example.manhinhappmusic.dto.MultiResponse;
import com.example.manhinhappmusic.dto.MultiResponseImp;

import java.util.ArrayList;
import java.util.List;

public class Artist extends MultiResponseImp implements ListItem {

    private String id;
    private String name;
    private List<String> songIDs;
    private String AvatarUrl;

    public Artist(String id, String name, List<String> songIDs, String AvatarUrl){
        this.id = id;
        this.name = name;
        this.songIDs = new ArrayList<>();
        if(songIDs != null)
            this.songIDs.addAll(songIDs);
        this.AvatarUrl = AvatarUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSongIDs() {
        return songIDs;
    }

    public void setSongIDs(List<String> songIDs) {
        this.songIDs.clear();
        if(songIDs != null)
            this.songIDs.addAll(songIDs);
    }

    public String getAvatarUrl() {
        return AvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        AvatarUrl = avatarUrl;
    }

    @Override
    public ListItemType getItemType() {
        return ListItemType.ARTIST;
    }

    @Override
    public List<String> getSearchKeyWord() {
        List<String> keyWords = new ArrayList<>();
        keyWords.add(name);
        return keyWords;
    }

    @Override
    public String getType() {
        return "artist";
    }
}
