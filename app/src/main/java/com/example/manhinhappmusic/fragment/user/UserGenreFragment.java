package com.example.manhinhappmusic.fragment.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.TestData;
import com.example.manhinhappmusic.adapter.MusicDisplayAdapter;
import com.example.manhinhappmusic.decoration.VerticalLinearSpacingItemDecoration;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.model.Genre;
import com.example.manhinhappmusic.model.ListItem;
import com.example.manhinhappmusic.model.ListItemType;
import com.example.manhinhappmusic.model.MediaPlayerManager;
import com.example.manhinhappmusic.model.MusicDisplayItem;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.model.User;
import com.example.manhinhappmusic.repository.GenreRepository;
import com.example.manhinhappmusic.repository.PlaylistRepository;
import com.example.manhinhappmusic.repository.SongRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserGenreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserGenreFragment extends BaseFragment {


    private static final String ARG_ID = "id";

    private String id;
    private TextView genreTitleText;
    private RecyclerView musicDisplayView;
    private MusicDisplayAdapter musicDisplayAdapter;
    private ImageButton backButton;
    private Genre genre;

    public UserGenreFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static UserGenreFragment newInstance(String id) {
        UserGenreFragment fragment = new UserGenreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_genre, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        List<MusicDisplayItem> musicDisplayItems = musicDisplayAdapter.getItems();
        musicDisplayItems.clear();
        SongRepository.getInstance().getRecentlySongs().observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                musicDisplayItems.add(new MusicDisplayItem("aaa", "Recently", new ArrayList<>(songs), MusicDisplayItem.HomeDisplayType.SONG));
                musicDisplayAdapter.notifyDataSetChanged();

            }
        });
        PlaylistRepository.getInstance().getAll().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                musicDisplayItems.add(new MusicDisplayItem("aaa", "New release", new ArrayList<>(playlists), MusicDisplayItem.HomeDisplayType.RELEASE_PLAYLIST));
                musicDisplayItems.add(new MusicDisplayItem("aaa", "Featuring", new ArrayList<>(playlists), MusicDisplayItem.HomeDisplayType.MIX_PLAYLIST));
                musicDisplayItems.add(new MusicDisplayItem("aaa", "Trending artist", new ArrayList<>(TestData.artistList), MusicDisplayItem.HomeDisplayType.ARTIST));
                musicDisplayAdapter.notifyDataSetChanged();
            }

        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        genreTitleText = view.findViewById(R.id.genre_title_text);
        musicDisplayView = view.findViewById(R.id.music_display_view);
        backButton = view.findViewById(R.id.back_button);
        genre = GenreRepository.getInstance().getItemById(id).getValue();

        genreTitleText.setText(genre.getName());
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRequestGoBackPreviousFragment();
            }
        });

        musicDisplayView = view.findViewById(R.id.music_display_view);
        musicDisplayAdapter = new MusicDisplayAdapter(new ArrayList<>(), new MusicDisplayAdapter.OnDisplayItemCLickListener() {
            @Override
            public void onDisplayItemClick(int position, ListItem item) {
                if(item.getType() == ListItemType.PLAYLIST)
                {
                    Playlist playlist = (Playlist) item;
                    PlaylistRepository.getInstance().setCurrentPlaylist(playlist);
                    callback.onRequestChangeFragment(FragmentTag.USER_PLAYLIST, playlist.getId());
                }
                else if (item.getType() == ListItemType.SONG)
                {
                    MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance(null);
                    Song song = (Song) item;
                    mediaPlayerManager.setPlaylist(new ArrayList<>(Arrays.asList(song)));
                    mediaPlayerManager.setCurrentSong(0);
                    callback.onRequestLoadMiniPlayer();
                }
                else if(item.getType() == ListItemType.ARTIST)
                {
                    User artist = (User) item;
                    callback.onRequestChangeFragment(FragmentTag.USER_ARTIST, artist.getId());
                }
            }
        });
        musicDisplayView.setAdapter(musicDisplayAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        musicDisplayView.setLayoutManager(layoutManager);
        musicDisplayView.addItemDecoration(new VerticalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics())));

    }
}