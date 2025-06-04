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
import android.widget.Button;
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

    private static final String ARG_ID = "ID";
    private static final String ARG_IS_THIS_PLAYING = "IS_THIS_PLAYING";
    private String id;

    private ImageView playlistsCoverImage;
    private TextView playlistsTitle;
    private TextView playlistsCount;
    private ImageButton backButton;
    private Button addButton;
    private Button editButton;
    private RecyclerView songsView;
    private SongAdapter songAdapter;
    private Playlist playlist;
    private boolean isPlaying =  false;


    public UserPlaylistFragment() {
        // Required empty public constructor
    }

    public static UserPlaylistFragment newInstance(String id) {
        UserPlaylistFragment fragment = new UserPlaylistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_IS_THIS_PLAYING, isPlaying);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
        {
            id = getArguments().getString(ARG_ID);
            isPlaying = getArguments().getBoolean(ARG_IS_THIS_PLAYING);
        }

        playlist = PlaylistRepository.getInstance().getItemById(id).getValue();

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
        addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRequestChangeFragment(FragmentTag.PLAYLIST_ADD_SONG, playlist.getId());
            }
        });
        editButton = view.findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRequestChangeFrontFragment(FragmentTag.PLAYLIST_EDIT, playlist.getId());
            }
        });
        songAdapter = new SongAdapter(playlist.getSongsList(), new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance(null);
                if(!isPlaying)
                {
                    mediaPlayerManager.setPlaylist(playlist.getSongsList());
                    isPlaying = true;
                }
                mediaPlayerManager.setCurrentSong(position);
                mediaPlayerManager.play();
                callback.onRequestLoadMiniPlayer();

            }
        });
        songsView = view.findViewById(R.id.songs_view);
        songsView.setAdapter(songAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        songsView.setLayoutManager(linearLayoutManager);
        songsView.addItemDecoration(new VerticalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));

        getParentFragmentManager().setFragmentResultListener("update_playlist", getViewLifecycleOwner(), (requestKey, result)->{
            songAdapter.notifyDataSetChanged();
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_playlist, container, false);
    }


}