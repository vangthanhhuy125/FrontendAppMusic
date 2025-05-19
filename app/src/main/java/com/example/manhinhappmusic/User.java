package com.example.manhinhappmusic;


import java.util.ArrayList;


public class User {

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
