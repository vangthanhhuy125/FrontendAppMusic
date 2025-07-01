package com.example.manhinhappmusic.model;

import lombok.Data;
@Data
public class Feedback {
    private String id;
    private String userId;
    private String content;
    private String status;
    private String adminReply;
    private int rating;
    private String date;

    public Feedback() {

    }
}
