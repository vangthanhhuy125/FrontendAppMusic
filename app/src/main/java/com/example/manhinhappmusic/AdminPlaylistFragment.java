package com.example.manhinhappmusic;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminPlaylistFragment extends BaseFragment {

    private EditText searchBox;
    private RecyclerView recyclerView;
    private Button btnNewSong;
    private ImageView btnSort;
    private ListSongsAdapter adapter;
    private final List<Song> fullSongList = new ArrayList<>();
    private final List<Song> filteredSongList = new ArrayList<>();
    private boolean isSortedAscending = true;
    private SongViewModel songViewModel;

    public AdminPlaylistFragment() {}

    public static AdminPlaylistFragment newInstance(Song song) {
        AdminPlaylistFragment fragment = new AdminPlaylistFragment();
        Bundle args = new Bundle();
        args.putParcelable("SONG_DATA", song);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchBox = view.findViewById(R.id.searchBox);
        recyclerView = view.findViewById(R.id.recyclerView);
        btnSort = view.findViewById(R.id.btn_sort);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ListSongsAdapter(filteredSongList, new ListSongsAdapter.OnSongClickListener() {
            @Override
            public void onEditClick(Song song, int position) {
                callback.onRequestChangeFragment(FragmentTag.EDIT_SONG, song);
            }

            @Override
            public void onDeleteClick(Song song, int position) {
                ConfirmDeletingSongFragment fragment = ConfirmDeletingSongFragment.newInstance(song, position);
                fragment.setConfirmDeleteListener((songId, pos) -> {
                    Song removed = fullSongList.remove(pos);
                    filterSongs(searchBox.getText().toString().trim());
                });

                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.dialog_container, fragment, "ConfirmDeleteOverlay")
                        .addToBackStack(null)
                        .commit();

                View container = requireView().findViewById(R.id.dialog_container);
                container.setVisibility(View.VISIBLE);
            }
        });

        recyclerView.setAdapter(adapter);


        view.setOnTouchListener((v, event) -> {
            if (searchBox.isFocused()) {
                searchBox.clearFocus();
                hideKeyboard(searchBox);
            }
            return false;
        });


        searchBox.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSongs(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });


        btnSort.setOnClickListener(v -> sortSongList());


        songViewModel = new ViewModelProvider(requireActivity()).get(SongViewModel.class);
        songViewModel.getSongList().observe(getViewLifecycleOwner(), songs -> {
            if (songs == null || songs.isEmpty()) {

                fakeData();
            } else {
                fullSongList.clear();
                fullSongList.addAll(songs);
                filterSongs(searchBox.getText().toString().trim());
            }
        });


        if (getArguments() != null && getArguments().containsKey("SONG_DATA")) {
            Song receivedSong = getArguments().getParcelable("SONG_DATA");
            if (receivedSong != null) {
                fullSongList.clear();
                fullSongList.add(receivedSong);
                filterSongs("");
            }
        }
    }

    private void fakeData() {
        fullSongList.clear();
        List<String> genre1 = new ArrayList<>();
        genre1.add("pop");

        fullSongList.add(new Song("1", "Nắng ấm xa dần", "Sơn Tùng M-TP", "Hit đầu tiên", R.raw.again, R.drawable.exampleavatar, genre1));
        fullSongList.add(new Song("2", "Em của ngày hôm qua", "Sơn Tùng M-TP", "Quốc dân", R.raw.again, R.drawable.exampleavatar, genre1));
        fullSongList.add(new Song("3", "Bài này chill phết", "Đen Vâu", "Chill", R.raw.again, R.drawable.exampleavatar, genre1));

        filterSongs("");
    }

    private void filterSongs(String keyword) {
        filteredSongList.clear();
        if (keyword == null || keyword.trim().isEmpty()) {
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
        adapter.notifyDataSetChanged();
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void sortSongList() {
        Collections.sort(filteredSongList, (s1, s2) -> {
            if (isSortedAscending) {
                return s1.getTitle().compareToIgnoreCase(s2.getTitle());
            } else {
                return s2.getTitle().compareToIgnoreCase(s1.getTitle());
            }
        });
        adapter.notifyDataSetChanged();
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
