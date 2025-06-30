package com.example.manhinhappmusic;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminPlaylistFragment extends BaseFragment {

    private EditText searchBox;
    private RecyclerView recyclerView;
    private Button btnNewSong;
    private ListSongsAdapter adapter;
    private List<Song> fullSongList = new ArrayList<>();
    private List<Song> filteredSongList = new ArrayList<>();
    private boolean isSortedAscending = true;
    private SongViewModel songViewModel;

    public AdminPlaylistFragment() {
    }

    public static AdminPlaylistFragment newInstance(Song song) {
        AdminPlaylistFragment fragment = new AdminPlaylistFragment();
        Bundle args = new Bundle();
        args.putParcelable("SONG_DATA", song);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnNewSong = view.findViewById(R.id.btnNewSong);
        searchBox = view.findViewById(R.id.searchBox);
        recyclerView = view.findViewById(R.id.recyclerView);
        ImageView btnSort = view.findViewById(R.id.btn_sort);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ListSongsAdapter(filteredSongList, new ListSongsAdapter.OnSongClickListener() {
            @Override
            public void onEditClick(Song song) {
                callback.onRequestChangeFragment(FragmentTag.EDIT_SONG, song);
            }

            @Override
            public void onDeleteClick(Song song) {
                callback.onRequestChangeFragment(FragmentTag.CONFIRM_DELETING_SONG, song);
            }
        });

        recyclerView.setAdapter(adapter);

        Song receivedSong = null;
        if (getArguments() != null) {
            receivedSong = getArguments().getParcelable("SONG_DATA");
        }

        if (receivedSong != null) {
            filteredSongList.clear();
            filteredSongList.add(receivedSong);
            adapter.notifyDataSetChanged();
        } else {
            fakeData();
        }

        view.setOnTouchListener((v, event) -> {
            if (searchBox.isFocused()) {
                searchBox.clearFocus();
                hideKeyboard(searchBox);
            }
            return false;
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSongs(s.toString());
            }

            @Override public void afterTextChanged(Editable s) { }
        });

        btnSort.setOnClickListener(v -> sortSongList());

        btnNewSong.setOnClickListener(v -> callback.onRequestChangeFragment(FragmentTag.ADD_SONG, null));

        songViewModel = new ViewModelProvider(requireActivity()).get(SongViewModel.class);

        songViewModel.getSongList().observe(getViewLifecycleOwner(), songs -> {
            fullSongList.clear();
            fullSongList.addAll(songs);
            filterSongs(searchBox.getText().toString().trim());
        });
    }

    private void fakeData() {
        fullSongList.clear();
        List<String> genre1 = new ArrayList<>();
        genre1.add("pop");

        fullSongList.add(new Song("1", "Nắng ấm xa dần", "Sơn Tùng M-TP", "Hit đầu tiên", R.raw.again, R.drawable.exampleavatar, genre1));
        fullSongList.add(new Song("2", "Em của ngày hôm qua", "Sơn Tùng M-TP", "Quốc dân", R.raw.again, R.drawable.exampleavatar, genre1));
        fullSongList.add(new Song("3", "Bài này chill phết", "Đen Vâu", "Chill", R.raw.again, R.drawable.exampleavatar, genre1));

        filteredSongList.clear();
        filteredSongList.addAll(fullSongList);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void filterSongs(String keyword) {
        filteredSongList.clear();
        if (keyword.isEmpty()) {
            filteredSongList.addAll(fullSongList);
        } else {
            String lowerKeyword = keyword.toLowerCase();
            for (Song song : fullSongList) {
                if ((song.getTitle() != null && song.getTitle().toLowerCase().contains(lowerKeyword)) ||
                        (song.getArtistId() != null && song.getArtistId().toLowerCase().contains(lowerKeyword))) {
                    filteredSongList.add(song);
                }
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void sortSongList() {
        if (isSortedAscending) {
            Collections.sort(filteredSongList, (s1, s2) -> s1.getTitle().compareToIgnoreCase(s2.getTitle()));
        } else {
            Collections.sort(filteredSongList, (s1, s2) -> s2.getTitle().compareToIgnoreCase(s1.getTitle()));
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        isSortedAscending = !isSortedAscending;
    }

    public void updateSong(Song updatedSong) {
        for (int i = 0; i < fullSongList.size(); i++) {
            if (fullSongList.get(i).getId().equals(updatedSong.getId())) {
                fullSongList.set(i, updatedSong);
                break;
            }
        }

        filterSongs(searchBox.getText().toString().trim());
    }
}
