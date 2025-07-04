package com.example.manhinhappmusic.dto;

import com.example.manhinhappmusic.model.ListItem;
import com.example.manhinhappmusic.model.ListItemType;
import com.example.manhinhappmusic.model.Playlist;

import java.util.ArrayList;

import lombok.Data;

@Data
public class PlaylistResponse implements ListItem {
    private String id;
    private String name;
    private String description;
    private ArrayList<String> songs = new ArrayList<>();
    private String thumbnailUrl;
    private Boolean isInLibrary;

    public Playlist toPlaylist()
    {
        return  new Playlist(id,name, description, songs, "", thumbnailUrl, true,  "", new ArrayList<>() );
    }

    @Override
    public ListItemType getItemType() {
        return ListItemType.PLAYLIST;
    }
}
