package com.example.manhinhappmusic.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.example.manhinhappmusic.view.ClearableEditText;
import com.example.manhinhappmusic.repository.PlaylistRepository;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.decoration.VerticalLinearSpacingItemDecoration;
import com.example.manhinhappmusic.adapter.SearchResultAdapter;
import com.example.manhinhappmusic.repository.SongRepository;
import com.example.manhinhappmusic.model.ListItem;
import com.example.manhinhappmusic.model.ListItemType;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.model.Song;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserPlaylistAddSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserPlaylistAddSongFragment extends BaseFragment {

    private ClearableEditText searchText;
    private RecyclerView searchResultsView;
    private ImageButton backButton;
    private List<Song> sourceSongList;
    private SearchResultAdapter adapter;

    private static final String ARG_ID = "id";

    private String id;


    private Playlist playlist;
    public UserPlaylistAddSongFragment() {
        // Required empty public constructor
    }


    public static UserPlaylistAddSongFragment newInstance(String id) {
        UserPlaylistAddSongFragment fragment = new UserPlaylistAddSongFragment();
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

        playlist = PlaylistRepository.getInstance().getCurrentPlaylist();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_playlist_add_song, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchText = view.findViewById(R.id.search_text);
        searchResultsView = view.findViewById(R.id.search_result_view);
        backButton = view.findViewById(R.id.back_button);






        adapter = new SearchResultAdapter(new ArrayList<>(),new SparseBooleanArray(), new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ListItem item) {
                if(adapter.getListItemList().get(position).getType() == ListItemType.SONG)
                {
                    if(!adapter.getCheckStates().get(position))
                    {
                        PlaylistRepository.getInstance().addSongs(playlist.getId(), new ArrayList<>(Arrays.asList(((Song)item).getId()))).observe(getViewLifecycleOwner(), new Observer<Playlist>() {
                            @Override
                            public void onChanged(Playlist modifiedPlaylist) {
                                Song song = (Song) item;
                                playlist.getSongsList().add(song);
                                playlist.getSongs().add(song.getId());
                                adapter.getCheckStates().put(position, true);
                                adapter.notifyItemChanged(position);
                                Snackbar snackbar =  Snackbar.make(view,"Song has been added to playlist", Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(Color.WHITE);
                                snackbar.setTextColor(Color.BLACK);
                                snackbar.show();
                            }
                        });

                    }
                    else {
                        PlaylistRepository.getInstance().removeSongs(playlist.getId(), new ArrayList<>(Arrays.asList(((Song)item).getId()))).observe(getViewLifecycleOwner(), new Observer<Playlist>() {
                            @Override
                            public void onChanged(Playlist modifiedPlaylist) {
                                Song song = (Song) item;
                                playlist.getSongsList().remove(song);
                                playlist.getSongs().remove(song.getId());
                                adapter.getCheckStates().put(position, false);
                                adapter.notifyItemChanged(position);
                                Snackbar snackbar =  Snackbar.make(view,"Song has been removed to playlist", Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(Color.WHITE);
                                snackbar.setTextColor(Color.BLACK);
                                snackbar.show();
                            }
                        });

                    }

                }
            }
        });
        adapter.setOnItemCheckBoxClickListener(new SearchResultAdapter.OnItemCheckBoxClickListener() {
            @Override
            public void onItemCheckBoxClick(int position, ListItem item, CheckBox checkBox) {
                PlaylistRepository.getInstance().addSongs(playlist.getId(), new ArrayList<>(Arrays.asList(((Song)item).getId()))).observe(getViewLifecycleOwner(), new Observer<Playlist>() {
                    @Override
                    public void onChanged(Playlist modifiedPlaylist) {
                        Song song = (Song) item;
                        playlist.getSongsList().add(song);
                        playlist.getSongs().add(song.getId());
                        adapter.getCheckStates().put(position, true);
                        adapter.notifyItemChanged(position);
                        Snackbar snackbar =  Snackbar.make(view,"Song has been added to playlist", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }
                });
            }
        }, true);
        adapter.setOnItemCheckBoxClickListener(new SearchResultAdapter.OnItemCheckBoxClickListener() {
            @Override
            public void onItemCheckBoxClick(int position, ListItem item, CheckBox checkBox) {
                PlaylistRepository.getInstance().removeSongs(playlist.getId(), new ArrayList<>(Arrays.asList(((Song)item).getId()))).observe(getViewLifecycleOwner(), new Observer<Playlist>() {
                    @Override
                    public void onChanged(Playlist modifiedPlaylist) {
                        Song song = (Song) item;
                        playlist.getSongsList().remove(song);
                        playlist.getSongs().remove(song.getId());
                        adapter.getCheckStates().put(position, false);
                        adapter.notifyItemChanged(position);
                        Snackbar snackbar =  Snackbar.make(view,"Song has been removed to playlist", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }
                });
            }
        }, false);

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
        searchText.setHint("Search");
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                SongRepository.getInstance().searchSongs(s.toString()).observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
                    @Override
                    public void onChanged(List<Song> songs) {
                        List<Song> searchResults = songs;
                        SparseBooleanArray checkStates = checkSongInPlaylist(searchResults);
                        adapter.setNewData(new ArrayList<>(searchResults), checkStates);
                        adapter.notifyDataSetChanged();
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private List<Song> search(String keyWord, List<Song> songs)
    {
        if(!keyWord.isBlank())
        {
            return songs.stream()
                            .filter(song -> {
                                for(String itemKeyWord: song.getSearchKeyWord())
                                {
                                    if(Pattern.compile("\\b" + keyWord + ".*", Pattern.CASE_INSENSITIVE)
                                            .matcher(itemKeyWord)
                                            .find())
                                    {
                                        return true;
                                    }
                                }
                                return false;
                            })
                            .collect(Collectors.toList());

        }
        return new ArrayList<>();

    }

    private SparseBooleanArray checkSongInPlaylist(List<Song> songs)
    {
        SparseBooleanArray checkStates = new SparseBooleanArray();
        for(int i = 0; i < songs.size(); i++)
        {
            for(Song song: playlist.getSongsList())
            {
                if(song.getId().equals(songs.get(i).getId()))
                {
                    checkStates.put(i,true);
                    break;
                }
            }

        }
        return  checkStates;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getParentFragmentManager().setFragmentResult("update_playlist", null);

    }
}