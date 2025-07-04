package com.example.manhinhappmusic.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddSongsRequest {
    private List<String> songs;
    private String playlistId;
}
