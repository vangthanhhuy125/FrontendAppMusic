package com.example.manhinhappmusic.model;


import com.example.manhinhappmusic.dto.MultiResponse;
import com.example.manhinhappmusic.dto.MultiResponseImp;

import java.util.ArrayList;
import java.util.List;


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

    //test purpose
    private int avatarResID;

    public User(String id, String email, String password, String lastName, String fullName, String role, String avatarUrl)
    {
        this.id = id;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.fullName = fullName;
        this.role = role;
        this.avatarUrl = avatarUrl;
    }

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



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getAvatarResID() {
        return avatarResID;
    }

    public void setAvatarResID(int avatarResID) {
        this.avatarResID = avatarResID;
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
