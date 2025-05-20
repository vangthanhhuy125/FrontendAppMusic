package com.example.manhinhappmusic;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserPlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserPlaylistFragment extends Fragment {

    private RecyclerView playlistView;
    private PlaylistAdapter playlistAdapter;
    private List<Song> playlistItems;

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


        playlistItems = new ArrayList<>();

        List<String> genre1 = Arrays.asList("rock", "pop", "jazz", "electronic", "ballad");
        List<String> genre2 = Arrays.asList("dance", "hiphop", "rap");

        playlistItems.add(new Song("1", "SongA", "ABC", "YYYY", "url.mp3", "avatar.jpg", genre1));
        playlistItems.add(new Song("2", "SongB", "HHH", "YYKLYY", "url.mp3", "avatar.jpg", genre2));
        playlistItems.add(new Song("3", "SongC", "DDD", "YACEYYY", "url.mp3", "avatar.jpg", genre2));
        playlistItems.add(new Song("4", "SongD", "HDHDHD", "YDETYYY", "url.mp3", "avatar.jpg", genre1));
        playlistItems.add(new Song("5", "SongE", "OOOOOO", "ADDYYYY", "url.mp3", "avatar.jpg", genre2));

        playlistAdapter = new PlaylistAdapter(playlistItems);
        playlistView = view.findViewById(R.id.playlist);
        playlistView.setAdapter(playlistAdapter);
        Context context = getContext();
        if (context != null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            playlistView.setLayoutManager(linearLayoutManager);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_playlist, container, false);
    }
}