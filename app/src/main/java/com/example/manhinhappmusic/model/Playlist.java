package com.example.manhinhappmusic.model;

import com.example.manhinhappmusic.dto.MultiResponse;
import com.example.manhinhappmusic.dto.SongResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Playlist extends MultiResponse implements ListItem, Serializable {

 private String id;
 private String name;
 private String description;
 private List<String> songs = new ArrayList<>();
 private String userId;
 private String thumbnailUrl;
 private Boolean isPublic;
 private String playlistType;

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





}

