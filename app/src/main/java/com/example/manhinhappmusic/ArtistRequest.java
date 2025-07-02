package com.example.manhinhappmusic;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class ArtistRequest {

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
