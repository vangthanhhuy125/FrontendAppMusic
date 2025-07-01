package com.example.manhinhappmusic.model;


import com.example.manhinhappmusic.dto.MultiResponse;
import com.example.manhinhappmusic.dto.MultiResponseImp;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class User extends MultiResponseImp implements ListItem {

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
    private String createdAt;
    private String updatedAt;

    //test purpose
    private int avatarResID;

    public User(String id, String email, String password, String lastName, String fullName, String role, String avatarUrl, String bio, Boolean isVerifiedArtist, String createdAt, String updatedAt)
    {
        this.id = id;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.fullName = fullName;
        this.role = role;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
        this.isVerifiedArtist = isVerifiedArtist;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User(String id, String email, String password, String lastName, String fullName, String role, int avatarResID, String bio, Boolean isVerifiedArtist, String createdAt, String updatedAt)
    {
        this.id = id;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.fullName = fullName;
        this.role = role;
        this.avatarResID = avatarResID;
        this.bio = bio;
        this.isVerifiedArtist = isVerifiedArtist;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public ListItemType getItemType() {
        return ListItemType.ARTIST;
    }

    @Override
    public List<String> getSearchKeyWord() {
        List<String> keyWords = new ArrayList<>();
        keyWords.add(fullName);
        return keyWords;    }

    @Override
    public String getType() {
        return "artist";
    }
}
