package com.example.manhinhappmusic.model;
import lombok.Data;

@Data
public class Comment {
  private String id;
  private String userId;
  private String content;
  private String parentCommentId;
  private String songId;
  private String createdAt;
  private String updatedAt;

  public Comment(String id, String user_id, String content, String parentCommentId, String song_id, String created_at, String updated_at){
    this.id = id;
    this.userId = user_id;
    this.content = content;
    this.parentCommentId = parentCommentId;
    this.songId = song_id;
    this.createdAt = created_at;
    this.updatedAt = updated_at;
  }
}
