package com.example.manhinhappmusic.model;

public class Comment{
  private String id;
  private String user_id;
  private String content;
  private String parentCommentId;
  private String song_id;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
