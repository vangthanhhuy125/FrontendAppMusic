package com.example.manhinhappmusic.model;

import com.example.manhinhappmusic.dto.MultiResponse;
import com.example.manhinhappmusic.dto.MultiResponseImp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Playlist extends MultiResponseImp implements ListItem{

 private String id;
 private String name;
 private String description;
 private ArrayList<String> songs = new ArrayList<>();
 private String userId;
 private String thumbnailUrl;
 private Boolean isPublic;

private List<Song> songsList;


 public Boolean getPublic() {
  return isPublic;
 }

 public void setPublic(Boolean aPublic) {
  isPublic = aPublic;
 }

 @Override
 public ListItemType getItemType() {
  return ListItemType.PLAYLIST;
 }

 @Override
 public List<String> getSearchKeyWord() {
  List<String> keyWords = new ArrayList<>();
  keyWords.add(name);
//  keyWords.addAll(artists);
  return keyWords;
 }


 @Override
 public String getType() {
  return "playlist";
 }
}

