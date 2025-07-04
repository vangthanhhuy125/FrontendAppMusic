package com.example.manhinhappmusic.fragment.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.activity.AdminActivity;
import com.example.manhinhappmusic.adapter.AdminListGenreAdapter;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.model.Genre;
import com.example.manhinhappmusic.viewmodel.GenreViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListGenreFragment extends BaseFragment implements AdminListGenreAdapter.OnItemRemoveListener {

    private List<Genre> genreList;
    private List<Genre> filteredList;
    private EditText searchBox;
    private ImageView imgSort;
    private Button btnNewGenre;
    private RecyclerView genreRecyclerView;
    private AdminListGenreAdapter adapter;
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
        filteredList = new ArrayList<>();
        adapter = new AdminListGenreAdapter(filteredList, this);

        adapter.setOnItemClickListener(genre -> {
            if (callback != null) {
                callback.onRequestChangeFragment(FragmentTag.EDIT_GENRE, genre);
            }
        });

        genreRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        genreRecyclerView.setAdapter(adapter);
        genreRecyclerView.setVisibility(View.VISIBLE);

        genreRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    ((AdminActivity) requireActivity()).hideBottomNav();
                } else if (dy < 0) {
                    ((AdminActivity) requireActivity()).showBottomNav();
                }
            }
        });

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
            if (genres != null) {
                genreList.clear();
                genreList.addAll(genres);
                filteredList.clear();
                filteredList.addAll(genres);
                adapter.notifyDataSetChanged();
            }
        });

        genreViewModel.fetchGenres(); // ✅ Gọi API thật để load dữ liệu
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle args = getArguments();
        if (args != null && args.containsKey("genre_result")) {
            Object obj = args.get("genre_result");
            if (obj instanceof Genre) {
                Genre updatedGenre = (Genre) obj;
                if (updatedGenre != null && updatedGenre.getId() != null) {
                    updateOrAddGenre(updatedGenre);
                }
            }
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
        filteredList.clear();
        filteredList.addAll(genreList);
        adapter.notifyDataSetChanged();
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
            genreViewModel.deleteGenre(genre); // Gọi API xóa backend
        }
    }

    private void sortGenres() {
        if (isSortAscending) {
            filteredList.sort((g1, g2) -> g2.getName().compareToIgnoreCase(g1.getName()));
        } else {
            filteredList.sort((g1, g2) -> g1.getName().compareToIgnoreCase(g2.getName()));
        }

        isSortAscending = !isSortAscending;
        adapter.notifyDataSetChanged();
    }
}
