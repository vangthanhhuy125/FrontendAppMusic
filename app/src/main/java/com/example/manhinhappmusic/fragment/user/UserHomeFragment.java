package com.example.manhinhappmusic.fragment.user;

import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.example.manhinhappmusic.network.ApiService;
import com.example.manhinhappmusic.repository.ArtistRepository;
import com.example.manhinhappmusic.repository.GlobalVars;
import com.example.manhinhappmusic.repository.PlaylistRepository;
import com.example.manhinhappmusic.repository.SongRepository;
import com.example.manhinhappmusic.repository.UserRepository;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UserHomeFragment extends BaseFragment {

    private static final String ARG_USER_ID = "userId";

    private static final String ARG_USER_NAME = "userName";
    private static final String ARG_USER_AVATAR_URL = "userAvatarUrl";
    private FragmentUserHomeBinding binding;
    private NavController navController;
    private RecyclerView playlistView;
    private RecyclerView musicDisplayView;

    private MusicDisplayAdapter musicDisplayAdapter;
    private HomePlaylistAdapter playlistAdapter;

    private ImageView userAvatarImage;
    private TextView userNameText;

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
        String userName =  GlobalVars.getVars().get(ARG_USER_NAME);
        String userAvatarUrl = GlobalVars.getVars().get(ARG_USER_AVATAR_URL);
        if(userName != null && userAvatarUrl != null)
        {
            userNameText.setText("Hi \uD83D\uDC4B " + userName);
            Glide.with(getContext())
                    .load(ApiService.BASE_URL + userAvatarUrl)
                    .circleCrop()
                    .centerCrop()
                    .into(userAvatarImage);
        }
        else
        {
            SharedPreferences preferences = getContext().getSharedPreferences("AppPreferences", getContext().MODE_PRIVATE);
            String token = preferences.getString("token", "");
            UserRepository.getInstance().getUserProfile(token).observe(getViewLifecycleOwner(), new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    GlobalVars.getVars().put(ARG_USER_ID, user.getId());
                    userNameText.setText("Hi \uD83D\uDC4B " + user.getFullName());
                    GlobalVars.getVars().put(ARG_USER_NAME, user.getFullName());
                    if(user.getAvatarUrl() != null && !user.getAvatarUrl().isBlank())
                    {
                        Glide.with(getContext())
                                .load(ApiService.BASE_URL + user.getAvatarUrl())
                                .circleCrop()
                                .into(userAvatarImage);
                        GlobalVars.getVars().put(ARG_USER_AVATAR_URL, user.getAvatarUrl());
                    }

                }
            });
        }

        List<MusicDisplayItem> musicDisplayItems = musicDisplayAdapter.getItems();
        musicDisplayItems.clear();
        PlaylistRepository.getInstance().getAll().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                playlistAdapter.setPlaylistList(playlists);
                playlistAdapter.notifyDataSetChanged();

            }

        });
        SongRepository.getInstance().getRecentlySongs().observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                musicDisplayItems.add(new MusicDisplayItem("aaa", "Recently", new ArrayList<>(songs), MusicDisplayItem.HomeDisplayType.SONG));
                musicDisplayAdapter.notifyItemInserted(musicDisplayAdapter.getItemCount() - 1);

            }
        });
        PlaylistRepository.getInstance().getNewReleasePlaylists().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                musicDisplayItems.add(new MusicDisplayItem("aaa", "New release", new ArrayList<>(playlists), MusicDisplayItem.HomeDisplayType.RELEASE_PLAYLIST));
                musicDisplayAdapter.notifyItemInserted(musicDisplayAdapter.getItemCount() - 1);

            }

        });
        PlaylistRepository.getInstance().getFeaturedPlaylists().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {

                musicDisplayItems.add(new MusicDisplayItem("aaa", "Featuring", new ArrayList<>(playlists), MusicDisplayItem.HomeDisplayType.RELEASE_PLAYLIST));
                musicDisplayAdapter.notifyItemInserted(musicDisplayAdapter.getItemCount() - 1);

            }

        });

        ArtistRepository.getInstance().getTrendingArtist().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                musicDisplayItems.add(new MusicDisplayItem("aaa", "Trending artist", new ArrayList<>(users), MusicDisplayItem.HomeDisplayType.ARTIST));                musicDisplayAdapter.notifyItemInserted(musicDisplayAdapter.getItemCount() - 1);
                musicDisplayAdapter.notifyItemInserted(musicDisplayAdapter.getItemCount() - 1);
                callback.setIsProcessing(false);
            }
        });


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        navController = Navigation.findNavController(view);
        userAvatarImage = binding.userAvatarImage;
        userNameText = binding.userNameText;
        musicDisplayView = binding.musicDisplayView;
        playlistView = binding.playlistView;

        Glide.with(getContext())
                .load(R.drawable.person_default_cover)
                .circleCrop()
                .into(userAvatarImage);
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
                    Bundle bundle = new Bundle();
                    bundle.putString("artistId", artist.getId());
                    bundle.putString("artistName", artist.getFullName());
                    bundle.putString("artistImageUrl", artist.getAvatarUrl());
                    navController.navigate(R.id.userArtistFragment, bundle);
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