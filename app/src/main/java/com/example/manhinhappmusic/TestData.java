package com.example.manhinhappmusic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestData {
    public static List<Song> songList = new ArrayList<>();
    public static List<Playlist> playlistList = new ArrayList<>();

    static {
        songList.addAll(Arrays.asList(
                new Song("fdf", "Ocean View", "Hard Life", "", R.raw.ocean_view, R.raw.hardlife_cover, null),
                new Song("fdf", "Daydreams", "Hard Life", "", R.raw.daydreams, R.raw.hardlife_cover, null),
                new Song("fdf", "Antifreeze", "Hard Life", "", R.raw.antifreeze, R.raw.hardlife_cover, null),
                new Song("fdf", "Beeswax", "Hard Life", "", R.raw.beeswax, R.raw.hardlife_cover, null),
                new Song("fdf", "Earth", "Hard Life", "", R.raw.earth, R.raw.hardlife_cover, null),
                new Song("fdf", "Dead Celebrities", "Hard Life", "", R.raw.dead_celebrities, R.raw.hardlife_cover, null),
                new Song("fdf", "Again", "Still Woozy", "", R.raw.again, R.raw.still_woozy_cover, null),
                new Song("fdf", "Goodie Bag", "Still Woozy", "", R.raw.goodie_bag, R.raw.still_woozy_cover, null),
                new Song("fdf", "Tek it", "Cafune", "", R.raw.tek_it_i_watch_the_moon, R.raw.cafune_cover, null)
        ));

        playlistList.addAll(Arrays.asList(
                new Playlist("fdf", "Hard life", "Fdfd", new ArrayList<>(Arrays.asList(
                        songList.get(0),
                        songList.get(1),
                        songList.get(2),
                        songList.get(3),
                        songList.get(4),
                        songList.get(5)
                )), "Fdf", R.raw.hardlife_cover),

                new Playlist("fdf", "Still Woozy", "Fdfd", new ArrayList<>(Arrays.asList(
                        songList.get(6),
                        songList.get(7)
                )), "Fdf", R.raw.still_woozy_cover),

                new Playlist("fdf", "Cafune", "Fdfd", new ArrayList<>(Arrays.asList(
                        songList.get(8)
                )), "Fdf", R.raw.cafune_cover)
        ));
    }
}
