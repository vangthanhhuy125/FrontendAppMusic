package com.example.manhinhappmusic;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListGenreFragment extends BaseFragment implements ListGenreAdapter.OnItemRemoveListener {

    private List<Genre> genreList;
    private List<Genre> filteredList;
    private EditText searchBox;
    private ImageView imgSort;
    private Button btnNewGenre;
    private RecyclerView genreRecyclerView;
    private ListGenreAdapter adapter;
    private GenreViewModel genreViewModel;
    private boolean isSortAscending = true;

    public ListGenreFragment() {}

    public static ListGenreFragment newInstance(String param1, String param2) {
        ListGenreFragment fragment = new ListGenreFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_genre, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        genreRecyclerView = view.findViewById(R.id.genreRecyclerView);
        searchBox = view.findViewById(R.id.searchBox);
        imgSort = view.findViewById(R.id.imgSort);
        btnNewGenre = view.findViewById(R.id.btnNewGenre);

        genreList = new ArrayList<>();
        genreList.add(new Genre("1", "Pop", "Popular music", null));
        genreList.add(new Genre("2", "Rock", "Rock music", null));
        genreList.add(new Genre("3", "Jazz", "Jazz music", null));
        genreList.add(new Genre("4", "Hip Hop", "Hip Hop music", null));

        filteredList = new ArrayList<>(genreList);

        adapter = new ListGenreAdapter(filteredList, this);


        adapter.setOnItemClickListener(genre -> {
            if (callback != null) {
                callback.onRequestChangeFragment(FragmentTag.EDIT_GENRE, genre.getId(), genre.getName());
            }
        });

        genreRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        genreRecyclerView.setAdapter(adapter);
        genreRecyclerView.setVisibility(View.VISIBLE);

        searchBox.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                filteredList.clear();
                filteredList.addAll(genreList);
                adapter.notifyDataSetChanged();
                genreRecyclerView.setVisibility(View.VISIBLE);
            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterGenres(s.toString());
                genreRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override public void afterTextChanged(Editable s) {}
        });

        imgSort.setOnClickListener(v -> sortGenres());

        btnNewGenre.setOnClickListener(v -> {
            if (callback != null) {
                callback.onRequestChangeFragment(FragmentTag.ADD_GENRE);
            }
        });

        genreViewModel = new ViewModelProvider(requireActivity()).get(GenreViewModel.class);
        genreViewModel.getGenreList().observe(getViewLifecycleOwner(), genres -> {
            if (genres != null && !genres.isEmpty()) {
                genreList.clear();
                genreList.addAll(genres);
                filteredList.clear();
                filteredList.addAll(genres);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle args = getArguments();
        if (args != null && args.containsKey("genre_result")) {
            Genre updatedGenre = args.getParcelable("genre_result");
            updateOrAddGenre(updatedGenre);
            args.remove("genre_result");
        }
    }

    private void updateOrAddGenre(Genre genre) {
        int index = -1;
        for (int i = 0; i < genreList.size(); i++) {
            if (genreList.get(i).getId().equals(genre.getId())) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            genreList.set(index, genre);
            adapter.notifyItemChanged(index);
        } else {
            genreList.add(genre);
            adapter.notifyItemInserted(genreList.size() - 1);
        }
    }

    private void filterGenres(String keyword) {
        filteredList.clear();
        if (keyword.isEmpty()) {
            filteredList.addAll(genreList);
        } else {
            for (Genre genre : genreList) {
                if (genre.getName().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredList.add(genre);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRemove(int position, Genre genre) {
        if (position >= 0 && position < filteredList.size()) {
            filteredList.remove(position);
            genreList.removeIf(g -> g.getId().equals(genre.getId()));
            adapter.notifyItemRemoved(position);
            genreViewModel.deleteGenre(genre);
        }
    }

    private void sortGenres() {
        if (isSortAscending) {
            filteredList.sort((g1, g2) -> g2.getName().compareToIgnoreCase(g1.getName())); // Z → A
        } else {
            filteredList.sort((g1, g2) -> g1.getName().compareToIgnoreCase(g2.getName())); // A → Z
        }

        isSortAscending = !isSortAscending;
        adapter.notifyDataSetChanged();
    }
}
