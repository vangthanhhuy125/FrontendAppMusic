package com.example.manhinhappmusic.model;

import lombok.Data;

@Data
public class Feedback extends BaseDocument {
    private String id;
    private String userId;
    private String content;
    private String status;
    private String adminReply;

    public Feedback(String id, String user_id, String content, String status, String admin_reply){
        this.id = id;
        this.userId = user_id;
        this.content = content;
        this.status = status;
        this.adminReply = admin_reply;
    }
}
