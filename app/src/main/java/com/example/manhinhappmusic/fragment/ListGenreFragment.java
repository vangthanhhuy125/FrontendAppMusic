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

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.model.Genre;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListGenreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListGenreFragment extends Fragment implements ListGenreAdapter.OnItemRemoveListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListGenreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListGenreFragment.
     */
    // TODO: Rename and change types and number of parameters


    private List<Genre> genreList;
    private List<Genre> filteredList;
    private EditText searchBox;
    private RecyclerView genreRecyclerView;
    private ListGenreAdapter adapter;

    public static ListGenreFragment newInstance(String param1, String param2) {
        ListGenreFragment fragment = new ListGenreFragment();
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
        return inflater.inflate(R.layout.fragment_list_genre, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        genreRecyclerView = view.findViewById(R.id.genreRecyclerView);
        searchBox = view.findViewById(R.id.searchBox);

        genreList = new ArrayList<>();
        genreList.add(new Genre("1", "Pop", "Popular music", null));
        genreList.add(new Genre("2", "Rock", "Rock music", null));
        genreList.add(new Genre("3", "Jazz", "Jazz music", null));
        genreList.add(new Genre("4", "Hip Hop", "Hip Hop music", null));

        filteredList = new ArrayList<>(genreList);

        ListGenreAdapter adapter = new ListGenreAdapter(filteredList, this);
        genreRecyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        genreRecyclerView.setLayoutManager(linearLayoutManager);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterGenres(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


    }

    private void filterGenres(String keyword) {
        filteredList.clear();
        for (Genre genre : genreList) {
            if (genre.getName().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(genre);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRemove(int position) {
        if (filteredList != null && position >= 0 && position < filteredList.size()) {
            filteredList.remove(position);
            adapter.notifyItemRemoved(position);
        }
    }
}