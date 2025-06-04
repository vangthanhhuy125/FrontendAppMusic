package com.example.manhinhappmusic;

import java.util.List;

public class MusicDisplayItem {

    private String id;
    private String title;
    private MusicDisplayFragment.HomeDisplayType homeDisplayType;
//    private HomeDisplayFragment.HomeDisplayLayout homeDisplayLayout;
    private List<ListItem> items;

//    public HomeDisplayItem(String id, String title, List<ListItem> items, HomeDisplayFragment.HomeDisplayLayout homeDisplayLayout)
//    {
//        this.id = id;
//        this.title = title;
//        this.items = items;
//        this.homeDisplayLayout = homeDisplayLayout;
//    }

    public MusicDisplayItem(String id, String title, List<ListItem> items, MusicDisplayFragment.HomeDisplayType homeDisplayType)
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


    public MusicDisplayFragment.HomeDisplayType getHomeDisplayType() {
        return homeDisplayType;
    }

    public List<ListItem> getItems() {
        return items;
    }
}
