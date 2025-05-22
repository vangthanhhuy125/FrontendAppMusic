package com.example.manhinhappmusic;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
    private RecyclerView songsView;
    private SongAdapter songAdapter;
    private Playlist playlist;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserPlaylistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserPlaylistFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static UserPlaylistFragment newInstance(String param1, String param2) {
        UserPlaylistFragment fragment = new UserPlaylistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public UserPlaylistFragment(Playlist playlist){
        if(playlist != null)
            this.playlist = playlist;
        else
            this.playlist = new Playlist("","","",new ArrayList<>(), "dfd", "fd");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playlistsCoverImage = view.findViewById(R.id.playlists_cover_image);
        playlistsCoverImage.setImageResource(playlist.getThumnailResID());
        playlistsTitle = view.findViewById(R.id.playlists_title);
        playlistsTitle.setText(playlist.getName());
        playlistsCount = view.findViewById(R.id.playlists_count);
        playlistsCount.setText(String.valueOf(playlist.getSongsList().size()) + "songs");
        songAdapter = new SongAdapter(playlist.getSongsList(), new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                callback.onRequestChangeFragment(FragmentTag.NOW_PLAYING_SONG, new Pair<Playlist, Integer>(playlist, position));
            }
        });
        songsView = view.findViewById(R.id.songs_view);
        songsView.setAdapter(songAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        songsView.setLayoutManager(linearLayoutManager);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_playlist, container, false);
    }
}