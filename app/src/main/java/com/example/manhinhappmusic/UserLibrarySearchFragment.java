package com.example.manhinhappmusic;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserLibrarySearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserLibrarySearchFragment extends BaseFragment {

    private EditText searchText;
    private RecyclerView searchResultsView;
    private ImageButton backButton;
    private ImageButton deleteSearchTextButton;
    private List<Playlist> sourcePlaylistList;

    private static final String ARG_ID = "id";

    private String id;

    public UserLibrarySearchFragment() {
        // Required empty public constructor
    }


    public static UserLibrarySearchFragment newInstance(String id) {
        UserLibrarySearchFragment fragment = new UserLibrarySearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_playlist_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchText = view.findViewById(R.id.search_text);
        searchResultsView = view.findViewById(R.id.search_result_view);
        backButton = view.findViewById(R.id.back_button);
        deleteSearchTextButton = view.findViewById(R.id.delete_button);

        sourcePlaylistList = LibraryRepository.getInstance().getItemById("").getValue();

        PlaylistAdapter adapter = new PlaylistAdapter(sourcePlaylistList, new PlaylistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                    callback.onRequestChangeFragment(FragmentTag.USER_PLAYLIST, sourcePlaylistList.get(position).getId());
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        searchResultsView.setAdapter(adapter);
        searchResultsView.setLayoutManager(layoutManager);
        searchResultsView.addItemDecoration(new VerticalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRequestGoBackPreviousFragment();
            }
        });
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Playlist> searchResults = search(s.toString(), sourcePlaylistList);
                adapter.setPlaylistList(searchResults);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private List<Playlist> search(String keyWord, List<Playlist> playlists)
    {
        if(!keyWord.isBlank())
        {
            return playlists.stream()
                            .filter(playlist -> Pattern
                                    .compile("\\b" + keyWord + ".*", Pattern.CASE_INSENSITIVE)
                                    .matcher(playlist.getName())
                                    .find())
                            .collect(Collectors.toList());

        }
        return playlists;

    }



}