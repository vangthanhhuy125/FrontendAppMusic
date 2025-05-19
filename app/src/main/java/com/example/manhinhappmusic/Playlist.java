package com.example.manhinhappmusic;

import java.util.ArrayList;
import java.util.List;


public class Playlist{

 private String id;
 private String name;
 private String description;
 private List<String> songs = new ArrayList<>();
 private String userId;
 private String thumbnailUrl;
 private Boolean isPublic;

 public Playlist(String id, String name, String description, List<String> songs, String userId, String thumbnailUrl){
  this.id = id;
  this.name = name;
  this.description = description;
  this.songs = new ArrayList<String>();
  if(songs != null)
   this.songs.addAll(songs);
  this.userId = userId;
  this.thumbnailUrl = thumbnailUrl;
 }

 public String getId() {
  return id;
 }
 public void setId(String id) {
  this.id = id;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public String getDescription() {
  return description;
 }

 public void setDescription(String description) {
  this.description = description;
 }

 public List<String> getSongs() {
  return songs;
 }

 public void setSongs(List<String> songs) {
  this.songs.clear();
  this.songs.addAll(songs);
 }

 public String getUserId() {
  return userId;
 }

 public void setUserId(String userId) {
  this.userId = userId;
 }

 public String getThumbnailUrl() {
  return thumbnailUrl;
 }

 public void setThumbnailUrl(String thumbnailUrl) {
  this.thumbnailUrl = thumbnailUrl;
 }
}

