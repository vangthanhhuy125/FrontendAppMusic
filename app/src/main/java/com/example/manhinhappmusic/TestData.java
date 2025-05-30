package com.example.manhinhappmusic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestData {
    public static List<Song> songList = new ArrayList<>();
    public static List<Playlist> playlistList = new ArrayList<>();
    public static List<User> artistList = new ArrayList<>();
    public static  List<Genre> genreList = new ArrayList<>();

    public static Playlist getPlaylistById(String id)
    {
        return playlistList.stream()
                .filter(playlist -> playlist.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static Song getSongById(String id)
    {
        return songList.stream()
                .filter(song -> song.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static User getArtistById(String id)
    {
        return artistList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static Genre getGenreById(String id)
    {
        return genreList.stream()
                .filter(genre -> genre.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    static {
        songList.addAll(Arrays.asList(
                new Song("0", "Ocean View", "Hard Life", "", R.raw.ocean_view, R.raw.hardlife_cover, null),
                new Song("1", "Daydreams", "Hard Life", "", R.raw.daydreams, R.raw.hardlife_cover, null),
                new Song("2", "Antifreeze", "Hard Life", "", R.raw.antifreeze, R.raw.hardlife_cover, null),
                new Song("3", "Beeswax", "Hard Life", "", R.raw.beeswax, R.raw.hardlife_cover, null),
                new Song("4", "Earth", "Hard Life", "", R.raw.earth, R.raw.hardlife_cover, null),
                new Song("5", "Dead Celebrities", "Hard Life", "", R.raw.dead_celebrities, R.raw.hardlife_cover, null),
                new Song("6", "Again", "Still Woozy", "", R.raw.again, R.raw.still_woozy_cover, null),
                new Song("7", "Goodie Bag", "Still Woozy", "", R.raw.goodie_bag, R.raw.still_woozy_cover, null),
                new Song("8", "Tek it", "Cafune", "", R.raw.tek_it_i_watch_the_moon, R.raw.cafune_cover, null)
        ));

        playlistList.addAll(Arrays.asList(
                new Playlist("0", "Hard life", "Fdfd", new ArrayList<>(Arrays.asList(
                        songList.get(0),
                        songList.get(1),
                        songList.get(2),
                        songList.get(3),
                        songList.get(4),
                        songList.get(5)
                )), "Fdf", R.raw.hardlife_cover),

                new Playlist("1", "Still Woozy", "Fdfd", new ArrayList<>(Arrays.asList(
                        songList.get(6),
                        songList.get(7)
                )), "Fdf", R.raw.still_woozy_cover),

                new Playlist("2", "Cafune", "Fdfd", new ArrayList<>(Arrays.asList(
                        songList.get(8)
                )), "Fdf", R.raw.cafune_cover)
        ));

        artistList.addAll(Arrays.asList(
                new User("0", "","","Benson", "Benson Booner", "", R.raw.benson_boone_artist_image),
                new User("1", "","","d4vd", "d4vd", "", R.raw.d4vd_artist_image),
                new User("2", "","","Good Kid", "Good Kid", "", R.raw.good_kid_artist_image),
                new User("3", "","","Hard Life", "Hard Life", "", R.raw.hard_life_artist_image),
                new User("4", "","","Laufey", "Laufey", "", R.raw.laufey_artist_image),
                new User("5", "","","Oliver", "Oliver Tree", "", R.raw.oliver_tree_artist_image),
                new User("6", "","","Still Woozy", "Still Woozy", "", R.raw.still_woozy_artist_image)

                ));
        genreList.addAll(Arrays.asList(
                new Genre("0","US-UK","",R.raw.us_uk_genre_thumbnail),
                new Genre("1","Indie","",R.raw.indie_genre_thumbnail),
                new Genre("2","Pop","",R.raw.pop_genre_thumbnail),
                new Genre("3","Hip-Hop","",R.raw.hiphop_genre_thumbnail),
                new Genre("4","Rock","",R.raw.rock_genre_thumbnail),
                new Genre("5","J-POP","",R.raw.jpop_genre_thumbnail),
                new Genre("6","Jazz","",R.raw.jazz_genre_thumbnail),
                new Genre("7","Lofi","",R.raw.lofi_genre_thumbnail),
                new Genre("8","Country","",R.raw.country_genre_thumbnail),
                new Genre("9","Electronic","",R.raw.electronic_genre_thumbnail)

                ));
    }
}
