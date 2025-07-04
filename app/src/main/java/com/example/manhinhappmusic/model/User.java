package com.example.manhinhappmusic.model;


import com.example.manhinhappmusic.dto.MultiResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User extends MultiResponse implements ListItem, Serializable {

    private String id;
    private String email;
    private String password;
    private String lastName;
    private String fullName;
    private String role;
    private String avatarUrl;
    private String bio;
    private Boolean isVerifiedArtist;
    private Boolean isVerified;
    private String resetToken;
    private List<String> recentlyPlayed = new ArrayList<>();

    //test purpose
    private int avatarResID;

    public User(String id, String email, String password, String lastName, String fullName, String role, int avatarResID)
    {
        this.id = id;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.fullName = fullName;
        this.role = role;
        this.avatarResID = avatarResID;
    }


    @Override
    public ListItemType getItemType() {
        return ListItemType.ARTIST;
    }
}
