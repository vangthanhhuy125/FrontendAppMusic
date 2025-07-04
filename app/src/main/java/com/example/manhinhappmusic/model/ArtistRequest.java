package com.example.manhinhappmusic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import lombok.Data;

@Data
public class ArtistRequest implements Parcelable {

    private String id;
    private String userId;
    private String portfolioUrl;
    private String status;
    private Date submittedAt;
    private Date reviewedAt;
    private String reviewedBy;

    public ArtistRequest() {}

    public ArtistRequest(String userId, String portfolioUrl, String status, Date submittedAt) {
        this.userId = userId;
        this.portfolioUrl = portfolioUrl;
        this.status = status;
        this.submittedAt = submittedAt;
    }

    protected ArtistRequest(Parcel in) {
        id = in.readString();
        userId = in.readString();
        portfolioUrl = in.readString();
        status = in.readString();
        long submittedAtMillis = in.readLong();
        submittedAt = submittedAtMillis != -1 ? new Date(submittedAtMillis) : null;
        long reviewedAtMillis = in.readLong();
        reviewedAt = reviewedAtMillis != -1 ? new Date(reviewedAtMillis) : null;
        reviewedBy = in.readString();
    }

    public static final Creator<ArtistRequest> CREATOR = new Creator<ArtistRequest>() {
        @Override
        public ArtistRequest createFromParcel(Parcel in) {
            return new ArtistRequest(in);
        }

        @Override
        public ArtistRequest[] newArray(int size) {
            return new ArtistRequest[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(portfolioUrl);
        dest.writeString(status);
        dest.writeLong(submittedAt != null ? submittedAt.getTime() : -1);
        dest.writeLong(reviewedAt != null ? reviewedAt.getTime() : -1);
        dest.writeString(reviewedBy);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
