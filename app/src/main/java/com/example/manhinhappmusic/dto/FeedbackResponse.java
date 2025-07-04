package com.example.manhinhappmusic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedbackResponse {
    private String id;
    private String userId;
    private String content;
    private int rating;
    private String reply;
    private String createdAt;
    private String userName;
    private String userAvatarUrl;
}
