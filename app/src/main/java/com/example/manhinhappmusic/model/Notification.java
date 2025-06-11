package com.example.manhinhappmusic.model;
import lombok.Data;

@Data
public class Notification {
    private String id;
    private String userId;
    private String type; // newSong, replyFeedback, comment
    private String content;
    private boolean isRead;

    public Notification(String id, String userId, String type, String content, boolean isRead) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.content = content;
        this.isRead = isRead;
    }
}
