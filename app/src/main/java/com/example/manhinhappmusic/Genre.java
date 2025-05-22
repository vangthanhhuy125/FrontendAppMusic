package com.example.manhinhappmusic;


import java.util.Formatter;

public class Genre {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlCoverImage() {
        return urlCoverImage;
    }

    public void setUrlCoverImage(String urlCoverImage) {
        this.urlCoverImage = urlCoverImage;
    }
}
