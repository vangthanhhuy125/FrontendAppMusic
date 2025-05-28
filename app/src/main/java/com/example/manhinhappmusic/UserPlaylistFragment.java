package com.example.manhinhappmusic;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserPlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserPlaylistFragment extends BaseFragment {

    private ImageView playlistsCoverImage;
    private TextView playlistsTitle;
    private  TextView playlistsCount;
    private ImageButton backButton;
    private RecyclerView songsView;
    private SongAdapter songAdapter;
    private Playlist playlist;

    public UserPlaylistFragment() {
        // Required empty public constructor
    }

    public UserPlaylistFragment(Playlist playlist)
    {
        this.playlist = playlist;
    }


    public static UserPlaylistFragment newInstance(Playlist playlist) {
        UserPlaylistFragment fragment = new UserPlaylistFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playlistsCoverImage = view.findViewById(R.id.playlists_cover_image);
        Glide.with(this.getContext())
                .load(playlist.getThumnailResID())
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                .into(playlistsCoverImage);
        playlistsTitle = view.findViewById(R.id.playlists_title);
        playlistsTitle.setText(playlist.getName());
        playlistsCount = view.findViewById(R.id.playlists_count);
        playlistsCount.setText(String.valueOf(playlist.getSongsList().size()) + " songs");
        backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRequestGoBackPreviousFragment();
            }
        });
        songAdapter = new SongAdapter(playlist.getSongsList(), new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                callback.onRequestLoadMiniPlayer(playlist, position);
            }
        });
        songsView = view.findViewById(R.id.songs_view);
        songsView.setAdapter(songAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        songsView.setLayoutManager(linearLayoutManager);
        songsView.addItemDecoration(new VerticalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_playlist, container, false);
    }


}