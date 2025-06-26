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
    public class SongResponse extends MultiResponseImp implements ListItem{
        @Override
        public String getType() {
            return "song";
        }
        private String id;
        private String artistId;
        private String audioUrl;
        private String title;
        private String description;
        private String coverImageUrl;

        private List<String> playlistIds = new ArrayList<>();
        private Long views = 0l;
        private Double duration;

        public Song toSong()
        {
            return new Song(id, artistId, description, title, audioUrl, coverImageUrl, null, true, false, "", 0d, 0d);
        }

        @Override
        public ListItemType getItemType() {
            return ListItemType.SONG;
        }

        @Override
        public List<String> getSearchKeyWord() {
            return Collections.emptyList();
        }
    }


