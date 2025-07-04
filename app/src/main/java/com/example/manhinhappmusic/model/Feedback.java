package com.example.manhinhappmusic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
public class Feedback {
    private String id;
    private String userId;
    private String content;
    private int rating;
    private String reply;
    private String createdAt;
    private String userName;
    private String userAvatarUrl;
}
