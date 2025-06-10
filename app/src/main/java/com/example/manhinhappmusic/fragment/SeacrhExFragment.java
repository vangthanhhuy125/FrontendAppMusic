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

import com.example.manhinhappmusic.repository.SongRepository;
import com.example.manhinhappmusic.view.ClearableEditText;
import com.example.manhinhappmusic.model.MediaPlayerManager;
import com.example.manhinhappmusic.repository.PlaylistRepository;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.adapter.SearchResultAdapter;
import com.example.manhinhappmusic.TestData;
import com.example.manhinhappmusic.decoration.VerticalLinearSpacingItemDecoration;
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
 * Use the {@link SeacrhExFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeacrhExFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SeacrhExFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeacrhExFragment.
     */
    // TODO: Rename and change types and number of parameters

    ImageButton backButton;
    RecyclerView searchResultView;
    List<ListItem> sourceList = new ArrayList<>();
    ClearableEditText searchEditText;
    private int modifiedPosition = -1;

    public static SeacrhExFragment newInstance(String param1, String param2) {
        SeacrhExFragment fragment = new SeacrhExFragment();
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
        return inflater.inflate(R.layout.fragment_seacrh_ex, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(this::onBackButtonClick);
        searchResultView = view.findViewById(R.id.search_result_view);
        searchEditText = view.findViewById(R.id.search_edit_text);

        sourceList.addAll(TestData.songList);
        sourceList.addAll(TestData.artistList);
        sourceList.addAll(TestData.userPlaylistList);
        sourceList.addAll(TestData.nonUserPlaylistList);

        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(new ArrayList<>(), new SparseBooleanArray(),
        new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ListItem item) {

                if(item.getType() == ListItemType.SONG)
                {
                    MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance(null);
                    mediaPlayerManager.setPlaylist(new ArrayList<>(Arrays.asList((Song) item)));
                    mediaPlayerManager.setCurrentSong(0);
                    callback.onRequestLoadMiniPlayer();
                    mediaPlayerManager.play();
                }
                else if(item.getType() == ListItemType.PLAYLIST)
                {
                    callback.onRequestChangeFragment(FragmentTag.USER_PLAYLIST, ((Playlist)item).getId());
                }
            }
        });
        searchResultAdapter.setOnItemCheckBoxClickListener(new SearchResultAdapter.OnItemCheckBoxClickListener() {
            @Override
            public void onItemCheckBoxClick(int position, ListItem item, CheckBox checkBox) {
                if(item.getType() == ListItemType.SONG)
                {
                    checkBox.setChecked(true);
                    searchResultAdapter.getCheckStates().put(position, true);
                    modifiedPosition = position;
                    callback.onRequestChangeFrontFragment(FragmentTag.USER_SEARCH_ADD_SONG, ((Song)item).getId());
                }
                else if(item.getType() == ListItemType.PLAYLIST)
                {
                    Snackbar snackbar =  Snackbar.make(view,"Removed playlist from library", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    TestData.userPlaylistList.remove((Playlist) item);
                }

            }
        }, false);

        searchResultAdapter.setOnItemCheckBoxClickListener(new SearchResultAdapter.OnItemCheckBoxClickListener() {
            @Override
            public void onItemCheckBoxClick(int position, ListItem item, CheckBox checkBox) {
                if(item.getType() == ListItemType.SONG)
                {
                    PlaylistRepository.getInstance().addSongs("684594f8ee9e612d30043517", new ArrayList<>(Arrays.asList(((Song)item).getId()))).observe(getViewLifecycleOwner(), new Observer<Playlist>() {
                        @Override
                        public void onChanged(Playlist playlist) {
                            Snackbar snackbar = Snackbar.make(view,"", Snackbar.LENGTH_LONG);
                            View snackBarCustomLayout = LayoutInflater
                                    .from(view.getContext())
                                    .inflate(R.layout.snackbar_add_song_to_favorites, null);
                            snackBarCustomLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    modifiedPosition = position;
                                    callback.onRequestChangeFrontFragment(FragmentTag.USER_SEARCH_ADD_SONG, ((Song)item).getId());
                                    snackbar.dismiss();
                                }
                            });

                            ViewGroup snackBarLayout = (ViewGroup) snackbar.getView();
                            snackBarLayout.removeAllViews();
                            snackBarLayout.addView(snackBarCustomLayout);
                            snackbar.show();
                        }
                    });

                }
                else if(item.getType() == ListItemType.PLAYLIST)
                {
                    Snackbar snackbar =  Snackbar.make(view,"Add playlist to library", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                    TestData.userPlaylistList.add((Playlist) item);
                }


            }
        }, true);



        LinearLayoutManager searchResultLayoutManager = new LinearLayoutManager(this.getContext());
        searchResultView.setAdapter(searchResultAdapter);
        searchResultView.setLayoutManager(searchResultLayoutManager);
        searchResultView.addItemDecoration(new VerticalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));

        searchEditText.setHint("Search");
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SongRepository.getInstance().searchSongs(s.toString()).observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
                    @Override
                    public void onChanged(List<Song> songs) {
                        List<Song> searchResults = songs;
                        SparseBooleanArray checkStates = checkItemInLibrary(new ArrayList<>(searchResults));
                        searchResultAdapter.setNewData(new ArrayList<>(searchResults), checkStates);
                        searchResultAdapter.notifyDataSetChanged();
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getParentFragmentManager().setFragmentResultListener("remove_song", getViewLifecycleOwner(), (requestKey, result) ->{

            if(modifiedPosition != -1)
            {
                searchResultAdapter.getCheckStates().put(modifiedPosition, false);
                searchResultAdapter.notifyItemChanged(modifiedPosition);

            }
            Snackbar snackbar =  Snackbar.make(view,"Removed song from library", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
        });
        getParentFragmentManager().setFragmentResultListener("change", getViewLifecycleOwner(), (requestKey, result) ->{
            Snackbar snackbar =  Snackbar.make(view,"Saved changes", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();

        });

    }

    private void onBackButtonClick(View view){
        callback.onRequestGoBackPreviousFragment();
    }

    private List<ListItem> search(String keyWord, List<ListItem> items)
    {
        if(!keyWord.isBlank())
        {
            return items.stream()
                    .filter(listItem -> {
                        for(String itemKeyWord: listItem.getSearchKeyWord())
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

    private SparseBooleanArray checkItemInLibrary(List<ListItem> items)
    {
        SparseBooleanArray checkStates = new SparseBooleanArray();
        for(int i = 0 ; i < items.size(); i++)
        {
            if(items.get(i).getType() == ListItemType.PLAYLIST)
            {
                Playlist playlist = (Playlist) items.get(i);
                for(Playlist userPlaylist: TestData.userPlaylistList){
                    if(userPlaylist.getId().equals(playlist.getId())){
                        checkStates.put(i, true);
                        break;
                    }
                }
            }
            else if(items.get(i).getType() == ListItemType.SONG)
            {
                Song song  = (Song) items.get(i);
                for(Playlist playlist: TestData.userPlaylistList)
                {
                    boolean isFound = false;
                    for(Song userSong: playlist.getSongsList())
                    {
                        if(song.getId().equals(userSong.getId()))
                        {
                            checkStates.put(i, true);
                            isFound = true;
                            break;
                        }
                    }
                    if(isFound)
                        break;
                }
            }
        }

        return checkStates;
    }
}