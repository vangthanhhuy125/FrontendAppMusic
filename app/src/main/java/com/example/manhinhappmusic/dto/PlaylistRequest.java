package com.example.manhinhappmusic.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlaylistRequest {
 private String name;
 private String description;
 private ArrayList<String> songs = new ArrayList<>();
 private String thumbnailUrl;
}
