package com.example.manhinhappmusic.model;
import lombok.Data;

@Data
public class Genre {
    private String id;
    private String name;
    private String description;
    private String urlCoverImage;
    private String createdAt;
    private String updatedAt;

    public Genre(String id, String name, String description, String createdAt, String updatedAt ){
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Genre(String id, String name, String description, String urlCoverImage){
        this.id = id;
        this.name = name;
        this.description = description;
        this.urlCoverImage = urlCoverImage;
    }
}
