package com.example.manhinhappmusic;

import java.util.ArrayList;
import java.util.List;


public class Playlist{

 private String id;
 private String name;
 private String description;
 private ArrayList<String> songs = new ArrayList<>();
 private String userId;
 private String thumbnailUrl;
 private Boolean isPublic;

 public String getId() {
  return id;
 }
 public void setId(String id) {
  this.id = id;
 }

}

