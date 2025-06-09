package com.example.manhinhappmusic.model;

import lombok.Data;

@Data
public class Feedback {
    private String id;
    private String userId;
    private String content;
    private String status;
    private String adminReply;
    private String createdAt;
    private String updatedAt;

    public Feedback(String id, String user_id, String content, String status, String admin_reply, String created_at, String updated_at){
        this.id = id;
        this.userId = user_id;
        this.content = content;
        this.status = status;
        this.adminReply = admin_reply;
        this.createdAt = created_at;
        this.updatedAt = updated_at;
    }
}
