package com.example.manhinhappmusic.model;
import lombok.Data;

@Data
public class Genre extends BaseDocument {
    private String id;
    private String name;
    private String description;
    private String urlCoverImage;

    public Genre(String id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Genre(String id, String name, String description, String urlCoverImage){
        this.id = id;
        this.name = name;
        this.description = description;
        this.urlCoverImage = urlCoverImage;
    }
}
