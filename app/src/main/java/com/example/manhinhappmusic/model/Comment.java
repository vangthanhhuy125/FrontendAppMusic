package com.example.manhinhappmusic.model;
import lombok.Data;

@Data
public class Comment extends BaseDocument {
  private String id;
  private String userId;
  private String content;
  private String parentCommentId;
  private String songId;

  public Comment(String id, String user_id, String content, String parentCommentId, String song_id){
    this.id = id;
    this.userId = user_id;
    this.content = content;
    this.parentCommentId = parentCommentId;
    this.songId = song_id;
  }
}
