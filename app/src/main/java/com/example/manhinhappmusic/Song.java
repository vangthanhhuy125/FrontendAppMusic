package com.example.manhinhappmusic;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//import android.os.Bundle;


public class Song implements Parcelable, ListItem {
    private String id;
    private String artistId;
    private String description;
    private String title;
    private String audioUrl;
    private String coverImageUrl;
    private List<String> genreId;
    private Boolean isApproved;
    private Boolean isPublic;
    private String lyric;
    private Double duration;
    private Double views;

    //Test purpose

    private int audioResID;
    private int coverImageResID;

    public int getAudioResID() {
        return audioResID;
    }

    public void setAudioResID(int audioResID) {
        this.audioResID = audioResID;
    }

    public int getCoverImageResID() {
        return coverImageResID;
    }

    public void setCoverImageResID(int coverImageResID) {
        this.coverImageResID = coverImageResID;
    }

    // Constructors, Getters và Setters

    // Constructors, Getters và Setters
    public Song(String id, String title, String artistId, String description, String audioUrl, String coverImageUrl, List<String> genreId) {
        this.id = id;
        this.title = title;
        this.artistId = artistId;
        this.description = description;
        this.audioUrl = audioUrl;
        this.coverImageUrl = coverImageUrl;
        this.genreId = new ArrayList<>();
        if(genreId != null)
        {
            this.genreId.addAll(genreId);
        }
    }

    public Song(String id, String title, String artistId, String description, int audioUrl, int coverImageUrl, List<String> genreId) {
        this.id = id;
        this.title = title;
        this.artistId = artistId;
        this.description = description;
        this.audioResID = audioUrl;
        this.coverImageResID = coverImageUrl;
        this.genreId = new ArrayList<>();
        if(genreId != null)
        {
            this.genreId.addAll(genreId);
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public List<String> getGenreId() {
        return genreId;
    }

    public void setGenreId(List<String> genreId) {
        this.genreId.clear();
        this.genreId.addAll(genreId);
    }


    @Override
    public ListItemType getType() {
        return ListItemType.SONG;
    }

    protected Song(Parcel in) {
        id = in.readString();
        artistId = in.readString();
        description = in.readString();
        title = in.readString();
        audioUrl = in.readString();
        coverImageUrl = in.readString();
        genreId = in.createStringArrayList();
        isApproved = (Boolean) in.readValue(Boolean.class.getClassLoader());
        isPublic = (Boolean) in.readValue(Boolean.class.getClassLoader());
        lyric = in.readString();
        duration = in.readDouble();
        views = in.readDouble();
        audioResID = in.readInt();
        coverImageResID = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(artistId);
        dest.writeString(description);
        dest.writeString(title);
        dest.writeString(audioUrl);
        dest.writeString(coverImageUrl);
        dest.writeStringList(genreId);
        dest.writeValue(isApproved);
        dest.writeValue(isPublic);
        dest.writeString(lyric);
        dest.writeDouble(duration != null ? duration : 0);
        dest.writeDouble(views != null ? views : 0);
        dest.writeInt(audioResID);
        dest.writeInt(coverImageResID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

}
