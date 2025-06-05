package com.example.manhinhappmusic;

import java.util.List;

public interface ListItem {
    ListItemType getType();
    List<String> getSearchKeyWord();
}

enum ListItemType{
    SONG,
    PLAYLIST,
    ARTIST,
    HOME_SONG,
    HOME_PLAYLIST,
    FEATURING_PLAYLIST;

}
