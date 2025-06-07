package com.example.manhinhappmusic.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.adapter.PlaylistTrendAdapter;
import com.example.manhinhappmusic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminPlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminPlaylistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminPlaylistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminPlaylistFragment.
     */

    private EditText searchBox;
    private RecyclerView recyclerView;
    private PlaylistTrendAdapter playlistTrendAdapter;
    private List<Playlist> playlistList = new ArrayList<>();
    private List<Playlist> filteredList = new ArrayList<>();

    // TODO: Rename and change types and number of parameters
    public static AdminPlaylistFragment newInstance(String param1, String param2) {
        AdminPlaylistFragment fragment = new AdminPlaylistFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchBox = view.findViewById(R.id.searchBox);
        recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        playlistTrendAdapter = new PlaylistTrendAdapter(getContext(), filteredList);
        recyclerView.setAdapter(playlistTrendAdapter);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPlaylists(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

    }

    private void filterPlaylists(String keyword) {
        filteredList.clear();
        if (keyword.isEmpty()) {
            filteredList.addAll(playlistList);
        } else {
            for (Playlist p : playlistList) {
//                if (p.getName().toLowerCase().contains(keyword.toLowerCase()) ||
//                        p.getArtist().toLowerCase().contains(keyword.toLowerCase())) {
//                    filteredList.add(p);
//                }
            }
        }
        playlistTrendAdapter.notifyDataSetChanged();
    }
}