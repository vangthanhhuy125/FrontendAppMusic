package com.example.manhinhappmusic;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

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

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Date submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Date getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Date reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }
}
