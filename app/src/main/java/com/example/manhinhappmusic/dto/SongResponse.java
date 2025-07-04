package com.example.manhinhappmusic.dto;
import com.example.manhinhappmusic.model.ListItem;
import com.example.manhinhappmusic.model.ListItemType;
import com.example.manhinhappmusic.model.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class SongResponse extends MultiResponse implements ListItem{

    private String id;
    private String artistId;
    private String audioUrl;
    private String title;
    private String description;
    private String coverImageUrl;
    private Boolean isInLibrary;
    private List<String> playlistIds = new ArrayList<>();
    private Long views = 0L;
    private Double duration;
    private String artistName;
    private List<String> lyrics;
    public Song toSong()
    {
        return new Song(id, artistId, artistName, description, title, audioUrl, coverImageUrl, null, true, false, 0d, 0, lyrics);
    }

    @Override
    public ListItemType getItemType() {
        return ListItemType.SONG;
    }


}


