package com.example.manhinhappmusic.model;
import lombok.Data;

@Data
public class User extends BaseDocument {

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

    public User(String id, String email, String password, String lastName, String fullName, String role, String avatarUrl, String bio, Boolean isVerifiedArtist, Boolean isVerified, String resetToken) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.fullName = fullName;
        this.role = role;
        this.avatarUrl = avatarUrl;
        this.bio = bio;
        this.isVerifiedArtist = isVerifiedArtist;
        this.isVerified = isVerified;
        this.resetToken = resetToken;
    }
}
