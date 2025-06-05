package com.example.manhinhappmusic.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Artist extends BaseDocument {

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
}
