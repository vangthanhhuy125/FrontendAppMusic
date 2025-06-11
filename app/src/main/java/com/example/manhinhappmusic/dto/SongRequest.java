package com.example.manhinhappmusic.dto;

import lombok.Data;

@Data
public class SongRequest {
    private String artistId;
    private String title;
    private String description;
    private String audioUrl;
    private String coverImageUrl;
    private Boolean isApproved;
    private Boolean isPublic;
    private Double duration;
}
