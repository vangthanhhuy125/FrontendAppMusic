package com.example.manhinhappmusic;

public interface ListItem {
    ListItemType getType();
    String getSearchKeyWord();
}

enum ListItemType{
    SONG,
    PLAYLIST,
    ARTIST,
    HOME_SONG,
    HOME_PLAYLIST,
    FEATURING_PLAYLIST;

}
