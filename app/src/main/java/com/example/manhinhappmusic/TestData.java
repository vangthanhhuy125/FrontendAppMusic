package com.example.manhinhappmusic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestData {
    public static List<Song> songList = new ArrayList<>();
    public static List<Playlist> playlistList = new ArrayList<>();
    public static List<User> artistList = new ArrayList<>();
    public static  List<Genre> genreList = new ArrayList<>();

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

        artistList.addAll(Arrays.asList(
                new User("fdf", "","","Benson", "Benson Booner", "", R.raw.benson_boone_artist_image),
                new User("fdf", "","","d4vd", "d4vd", "", R.raw.d4vd_artist_image),
                new User("fdf", "","","Good Kid", "Good Kid", "", R.raw.good_kid_artist_image),
                new User("fdf", "","","Hard Life", "Hard Life", "", R.raw.hard_life_artist_image),
                new User("fdf", "","","Laufey", "Laufey", "", R.raw.laufey_artist_image),
                new User("fdf", "","","Oliver", "Oliver Tree", "", R.raw.oliver_tree_artist_image),
                new User("fdf", "","","Still Woozy", "Still Woozy", "", R.raw.still_woozy_artist_image)

                ));
        genreList.addAll(Arrays.asList(
                new Genre("","US-UK","",R.raw.us_uk_genre_thumbnail),
                new Genre("","Indie","",R.raw.indie_genre_thumbnail),
                new Genre("","Pop","",R.raw.pop_genre_thumbnail),
                new Genre("","Hip-Hop","",R.raw.hiphop_genre_thumbnail),
                new Genre("","Rock","",R.raw.rock_genre_thumbnail),
                new Genre("","J-POP","",R.raw.jpop_genre_thumbnail),
                new Genre("","Jazz","",R.raw.jazz_genre_thumbnail),
                new Genre("","Lofi","",R.raw.lofi_genre_thumbnail),
                new Genre("","Country","",R.raw.country_genre_thumbnail),
                new Genre("","Electronic","",R.raw.electronic_genre_thumbnail)

                ));
    }
}
