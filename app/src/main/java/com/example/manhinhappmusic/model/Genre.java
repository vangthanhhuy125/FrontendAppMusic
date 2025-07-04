package com.example.manhinhappmusic.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Genre implements Serializable {
    private String id;
    private String name;
    private String description;
    private String thumbnailUrl;
}
