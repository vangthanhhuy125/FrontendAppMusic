package com.example.manhinhappmusic;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Formatter;

public class Genre implements Parcelable {
    private String id;
    private String name;
    private String description;

    private String urlCoverImage;

    //test purpose

    private int thumbnailResID;
    public Genre(String id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Genre(String id, String name, String description, String urlCoverImage){
        this.id = id;
        this.name = name;
        this.description = description;
        this.urlCoverImage = urlCoverImage;
    }

    public Genre(String id, String name, String description, int thumbnailResID){
        this.id = id;
        this.name = name;
        this.description = description;
        this.thumbnailResID = thumbnailResID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlCoverImage() {
        return urlCoverImage;
    }

    public void setUrlCoverImage(String urlCoverImage) {
        this.urlCoverImage = urlCoverImage;
    }

    public int getThumbnailResID() {
        return thumbnailResID;
    }

    public void setThumbnailResID(int thumbnailResID) {
        this.thumbnailResID = thumbnailResID;
    }

    protected Genre(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        urlCoverImage = in.readString();
        thumbnailResID = in.readInt();
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(urlCoverImage);
        parcel.writeInt(thumbnailResID);
    }


}
