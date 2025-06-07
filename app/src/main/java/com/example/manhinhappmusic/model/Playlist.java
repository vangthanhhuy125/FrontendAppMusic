package com.example.manhinhappmusic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Playlist implements ListItem {

 private String id;
 private String name;
 private String description;
 private ArrayList<String> songs = new ArrayList<>();
 private String userId;
 private String thumbnailUrl;
 private Boolean isPublic;

 //Test purpose
 private int thumnailResID;
private String artistName;
private List<Song> songList;

 public List<Song> getSongsList() {
  return songList;
 }

 public Boolean getPublic() {
  return isPublic;
 }

 public void setPublic(Boolean aPublic) {
  isPublic = aPublic;
 }

 @Override
 public ListItemType getType() {
  return ListItemType.PLAYLIST;
 }

 @Override
 public List<String> getSearchKeyWord() {
  List<String> keyWords = new ArrayList<>();
  keyWords.add(name);
//  keyWords.addAll(artists);
  return keyWords;
 }

 public void addSong(Song song)
 {

 }


}

