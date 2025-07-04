package com.example.manhinhappmusic.fragment.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.adapter.ArtistSongAdapter;
import com.example.manhinhappmusic.databinding.FragmentUserArtistBinding;
import com.example.manhinhappmusic.dto.SongResponse;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.model.User;
import com.example.manhinhappmusic.decoration.VerticalLinearSpacingItemDecoration;
import com.example.manhinhappmusic.adapter.SearchResultAdapter;
import com.example.manhinhappmusic.network.ApiService;
import com.example.manhinhappmusic.repository.SongRepository;
import com.example.manhinhappmusic.model.ListItem;
import com.example.manhinhappmusic.model.ListItemType;
import com.example.manhinhappmusic.model.MediaPlayerManager;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.repository.ArtistRepository;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserArtistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserArtistFragment extends BaseFragment {

    private NavController navController;
    private FragmentUserArtistBinding binding;
    private ImageView artistImage;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView songsView;
    private ImageButton backButton;
    private User artist;

    private ArtistSongAdapter songsAdapter;


    private static final String ARG_ID = "artistId";
    private static final String ARG_NAME = "artistName";
    private static final String ARG_URL = "artistImageUrl";

    private String id;
    private String name;
    private String imageUrl;

    public UserArtistFragment() {
        // Required empty public constructor
    }

    public static UserArtistFragment newInstance(String id) {
        UserArtistFragment fragment = new UserArtistFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
            name = getArguments().getString(ARG_NAME);
            imageUrl = getArguments().getString(ARG_URL);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserArtistBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        collapsingToolbarLayout.setTitle(name);
        Glide.with(getContext())
                .load(ApiService.BASE_URL + imageUrl)
                .into(artistImage);
        SongRepository.getInstance().getSongByArtistId(id).observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                songsAdapter.setSongList(songs);
                songsAdapter.notifyDataSetChanged();

                callback.setIsProcessing(false);

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        backButton = binding.backButton;
        artistImage = binding.artistImage;
        collapsingToolbarLayout =  binding.collapsingLayout;
//        artistNameText = binding.artistNameText;
        songsView = binding.songsView;

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });








        songsAdapter = new ArtistSongAdapter(new ArrayList<>(), new ArtistSongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Song song) {
                MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance(null);
                mediaPlayerManager.setPlaylist(new ArrayList<>(Arrays.asList(song)));
                mediaPlayerManager.setCurrentSong(0);
                callback.onRequestLoadMiniPlayer();
            }
        });
        LinearLayoutManager songLayoutManager = new LinearLayoutManager(this.getContext());
        songsView.setAdapter(songsAdapter);
        songsView.setLayoutManager(songLayoutManager);
        songsView.addItemDecoration(new VerticalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}