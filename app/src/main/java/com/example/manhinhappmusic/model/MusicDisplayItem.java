package com.example.manhinhappmusic.model;

import java.util.List;

public class MusicDisplayItem {

    private String id;
    private String title;
    private HomeDisplayType homeDisplayType;
//    private HomeDisplayFragment.HomeDisplayLayout homeDisplayLayout;
    private List<ListItem> items;

//    public HomeDisplayItem(String id, String title, List<ListItem> items, HomeDisplayFragment.HomeDisplayLayout homeDisplayLayout)
//    {
//        this.id = id;
//        this.title = title;
//        this.items = items;
//        this.homeDisplayLayout = homeDisplayLayout;
//    }

    public MusicDisplayItem(String id, String title, List<ListItem> items, HomeDisplayType homeDisplayType)
    {
        this.id = id;
        this.title = title;
        this.items = items;
        this.homeDisplayType = homeDisplayType;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

//    public HomeDisplayFragment.HomeDisplayLayout getHomeDisplayLayout() {
//        return homeDisplayLayout;
//    }


    public HomeDisplayType getHomeDisplayType() {
        return homeDisplayType;
    }

    public List<ListItem> getItems() {
        return items;
    }

    public enum HomeDisplayType
    {
        RELEASE_PLAYLIST,
        MIX_PLAYLIST,
        SONG,
        ARTIST,
    }
}
