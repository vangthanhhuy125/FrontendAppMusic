package com.example.manhinhappmusic.dto;

import lombok.Data;

@Data
public class MultiResponse {
    private String type; // "song", "artist", "playlist"
    private Object data;

}
