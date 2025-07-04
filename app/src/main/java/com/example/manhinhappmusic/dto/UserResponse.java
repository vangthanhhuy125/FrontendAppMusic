package com.example.manhinhappmusic.dto;

import com.example.manhinhappmusic.model.ListItem;
import com.example.manhinhappmusic.model.ListItemType;

import lombok.Data;

@Data
public class UserResponse implements ListItem {
    private String id;
    private String email;
    private String fullName;
    private String role;
    private String avatarUrl;
    private Boolean isVerifiedArtist;
    private Boolean isVerified;

    @Override
    public ListItemType getItemType() {
        return ListItemType.ARTIST;
    }
}
