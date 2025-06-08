package com.example.manhinhappmusic.dto;


//import org.bson.types.ObjectId;

import lombok.Data;

@Data
public class SongRequest {
    private String artist_id;
    private String title;
    private String description;
    private String audioUrl;
    private String coverImageUrl;
    private Boolean isApproved;
    private Boolean isPublic;
    private Double duration;
}
