package com.example.manhinhappmusic.fragment.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.TestData;
import com.example.manhinhappmusic.adapter.HomePlaylistAdapter;
import com.example.manhinhappmusic.adapter.MusicDisplayAdapter;
import com.example.manhinhappmusic.databinding.FragmentUserHomeBinding;
import com.example.manhinhappmusic.decoration.GridSpacingItemDecoration;
import com.example.manhinhappmusic.decoration.VerticalLinearSpacingItemDecoration;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.model.ListItem;
import com.example.manhinhappmusic.model.ListItemType;
import com.example.manhinhappmusic.model.MediaPlayerManager;
import com.example.manhinhappmusic.model.MusicDisplayItem;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.model.User;
import com.example.manhinhappmusic.repository.PlaylistRepository;
import com.example.manhinhappmusic.repository.SongRepository;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UserHomeFragment extends BaseFragment {

    private FragmentUserHomeBinding binding;
    private ShapeableImageView userAvatarImage;
    private NavController navController;
    private RecyclerView playlistView;
    private RecyclerView musicDisplayView;

    private MusicDisplayAdapter musicDisplayAdapter;
    private HomePlaylistAdapter playlistAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding = FragmentUserHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
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
                playlistAdapter.setPlaylistList(playlists);
                playlistAdapter.notifyDataSetChanged();
                musicDisplayItems.add(new MusicDisplayItem("aaa", "New release", new ArrayList<>(playlists), MusicDisplayItem.HomeDisplayType.RELEASE_PLAYLIST));
                musicDisplayItems.add(new MusicDisplayItem("aaa", "Featuring", new ArrayList<>(playlists), MusicDisplayItem.HomeDisplayType.MIX_PLAYLIST));
                musicDisplayItems.add(new MusicDisplayItem("aaa", "Trending artist", new ArrayList<>(TestData.artistList), MusicDisplayItem.HomeDisplayType.ARTIST));
                musicDisplayAdapter.notifyDataSetChanged();
            }

        });



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        userAvatarImage = binding.userAvatarImage;
        musicDisplayView = binding.musicDisplayView;
        playlistView = binding.playlistView;

        userAvatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.userProfileFragment);
            }
        });
        musicDisplayAdapter = new MusicDisplayAdapter(new ArrayList<>(), new MusicDisplayAdapter.OnDisplayItemCLickListener() {
            @Override
            public void onDisplayItemClick(int position, ListItem item) {
                if(item.getItemType() == ListItemType.PLAYLIST)
                {
                    Playlist playlist = (Playlist) item;
                    PlaylistRepository.getInstance().setCurrentPlaylist(playlist);
                    navController.navigate(R.id.userPlaylistFragment);

                }
                else if (item.getItemType() == ListItemType.SONG)
                {
                    MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance(null);
                    Song song = (Song) item;
                    mediaPlayerManager.setPlaylist(new ArrayList<>(Arrays.asList(song)));
                    mediaPlayerManager.setCurrentSong(0);
                    callback.onRequestLoadMiniPlayer();
                }
                else if(item.getItemType() == ListItemType.ARTIST)
                {
                    User artist = (User) item;
                    navController.navigate(R.id.userArtistFragment);
                }
            }
        });
        musicDisplayView.setAdapter(musicDisplayAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        musicDisplayView.setLayoutManager(layoutManager);
        musicDisplayView.addItemDecoration(new VerticalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics())));


        GridLayoutManager playlistLayoutManager = new GridLayoutManager(this.getContext(), 2);
        playlistAdapter = new HomePlaylistAdapter(new ArrayList<>(), new HomePlaylistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Playlist playlist) {
                PlaylistRepository.getInstance().setCurrentPlaylist(playlist);
                navController.navigate(R.id.userPlaylistFragment);
            }
        });
        playlistView.setAdapter(playlistAdapter);
        playlistView.setLayoutManager(playlistLayoutManager);
        playlistView.addItemDecoration(new GridSpacingItemDecoration(2, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()), true));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}