package com.example.manhinhappmusic;

public interface ListItem {
    ListItemType getType();
}

enum ListItemType{
    SONG,
    PLAYLIST,
    ARTIST,
    HOME_SONG,
    FEATURING_PLAYLIST;

}
