package com.example.manhinhappmusic.dto;

import lombok.Data;

@Data
public class GenreResponse {
    private String id;
    private String name;
    private String description;
    private String urlCoverImage;

    public GenreResponse(String id, String name, String description, String urlCoverImage) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.urlCoverImage = urlCoverImage;
    }
}
