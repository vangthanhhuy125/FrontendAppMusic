package com.example.manhinhappmusic.model;

import com.example.manhinhappmusic.dto.MultiResponse;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Artist extends MultiResponse implements ListItem {

    private String id;
    private String name;
    private List<String> songIDs;
    private String AvatarUrl;



    @Override
    public ListItemType getItemType() {
        return ListItemType.ARTIST;
    }

}
