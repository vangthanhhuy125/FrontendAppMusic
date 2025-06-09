package com.example.manhinhappmusic.model;
import java.sql.Date;
import lombok.Data;

@Data
public class ArtistRequest {
    private String id;
    private String userId;
    private String portfolioUrl;
    private String status;
    private String reviewedBy;
    private Date submittedAt;
    private Date reviewedAt;

    public ArtistRequest(String id, String userId, String portfolioUrl, String status, String reviewedBy, Date submittedAt, Date reviewedAt) {
        this.id = id;
        this.userId = userId;
        this.portfolioUrl = portfolioUrl;
        this.status = status;
        this.reviewedBy = reviewedBy;
        this.submittedAt = submittedAt;
        this.reviewedAt = reviewedAt;
    }
}
