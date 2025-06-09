package com.example.manhinhappmusic.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class Artist {

    private String id;
    private String name;
    private List<String> songIDs;
    private String AvatarUrl;
    private String createdAt;
    private String updatedAt;

    public Artist(String id, String name, List<String> songIDs, String AvatarUrl, String createdAt, String updatedAt){
        this.id = id;
        this.name = name;
        this.songIDs = new ArrayList<>();
        if(songIDs != null)
            this.songIDs.addAll(songIDs);
        this.AvatarUrl = AvatarUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
