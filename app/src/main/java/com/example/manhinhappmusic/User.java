package com.example.manhinhappmusic;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class User implements ListItem, Parcelable {

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

    // test purpose
    private int avatarResID;

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public User(String id, String email, String password, String lastName, String fullName, String role, String avatarUrl) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.fullName = fullName;
        this.role = role;
        this.avatarUrl = avatarUrl;
        this.createdAt = getCurrentDateTime();
    }

    public User(String id, String email, String password, String lastName, String fullName, String role, int avatarResID) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.fullName = fullName;
        this.role = role;
        this.avatarResID = avatarResID;
        this.createdAt = getCurrentDateTime();
    }

    public User(String id, String email, String password, String lastName, String fullName, String role, int avatarResID, String createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.fullName = fullName;
        this.role = role;
        this.avatarResID = avatarResID;
        this.createdAt = createdAt;
    }

    protected User(Parcel in) {
        id = in.readString();
        email = in.readString();
        password = in.readString();
        lastName = in.readString();
        fullName = in.readString();
        role = in.readString();
        avatarUrl = in.readString();
        bio = in.readString();
        isVerifiedArtist = (Boolean) in.readValue(Boolean.class.getClassLoader());
        isVerified = (Boolean) in.readValue(Boolean.class.getClassLoader());
        resetToken = in.readString();
        createdAt = in.readString();
        avatarResID = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(lastName);
        dest.writeString(fullName);
        dest.writeString(role);
        dest.writeString(avatarUrl);
        dest.writeString(bio);
        dest.writeValue(isVerifiedArtist);
        dest.writeValue(isVerified);
        dest.writeString(resetToken);
        dest.writeString(createdAt);
        dest.writeInt(avatarResID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        User user = (User) obj;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public int getAvatarResID() { return avatarResID; }
    public void setAvatarResID(int avatarResID) { this.avatarResID = avatarResID; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Boolean getIsVerifiedArtist() { return isVerifiedArtist; }
    public void setIsVerifiedArtist(Boolean isVerifiedArtist) { this.isVerifiedArtist = isVerifiedArtist; }

    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }

    public String getResetToken() { return resetToken; }
    public void setResetToken(String resetToken) { this.resetToken = resetToken; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    @Override
    public ListItemType getType() {
        return ListItemType.ARTIST;
    }
}
