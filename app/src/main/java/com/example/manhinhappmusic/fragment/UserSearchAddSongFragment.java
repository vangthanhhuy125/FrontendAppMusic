package com.example.manhinhappmusic.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.manhinhappmusic.view.ClearableEditText;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.TestData;
import com.example.manhinhappmusic.decoration.VerticalLinearSpacingItemDecoration;
import com.example.manhinhappmusic.adapter.SearchPlaylistAddSongAdapter;
import com.example.manhinhappmusic.model.Playlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserSearchAddSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserSearchAddSongFragment extends BaseFragment {

    private ImageButton backButton;
    private Button newPlaylistButton;
    private ClearableEditText searchEditText;
    private TextView deleteAllText;
    private RecyclerView savedPlaylistView;
    private RecyclerView relativePlaylistView;
    private Button saveButton;
    private LinearLayout savedPlaylistContainer;
    private LinearLayout relevantPlaylistContainer;
    private List<Playlist> sourcePlaylistList;
    private  List<Playlist> savedPlaylistList;
    private List <Playlist> relevantPlaylistList;


    private static final String ARG_ID = "id";


    private String id;

    public UserSearchAddSongFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static UserSearchAddSongFragment newInstance(String id) {
        UserSearchAddSongFragment fragment = new UserSearchAddSongFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        View view = getView();
//        if (view != null) {
//            View parent = (View) view.getParent();
//            BottomSheetBehavior<?> behavior = BottomSheetBehavior.from(parent);
//            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//            behavior.setSkipCollapsed(true);
//            parent.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
//            parent.requestLayout();
//        }
//    }

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
        return inflater.inflate(R.layout.fragment_user_search_add_song, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backButton = view.findViewById(R.id.back_button);
        newPlaylistButton = view.findViewById(R.id.new_playlist_button);
        searchEditText = view.findViewById(R.id.search_edit_text);
        deleteAllText = view.findViewById(R.id.delete_all_text);
        savedPlaylistView = view.findViewById(R.id.saved_playlist_view);
        relativePlaylistView = view.findViewById(R.id.relevant_playlist_view);
        saveButton = view.findViewById(R.id.save_button);
        savedPlaylistContainer = view.findViewById(R.id.saved_playlist_container);
        relevantPlaylistContainer = view.findViewById(R.id.relevant_playlist_container);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRequestGoBackPreviousFragment();
            }
        });
        newPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddPlaylistFragment addPlaylistFragment = new AddPlaylistFragment();
                addPlaylistFragment.show(getParentFragmentManager(), null);
            }
        });



        sourcePlaylistList = TestData.userPlaylistList;
        savedPlaylistList = new ArrayList<>(sourcePlaylistList
                .stream()
                .filter(playlist -> playlist.getSongsList()
                        .stream()
                        .anyMatch(song -> song.getId().equals(id))).collect(Collectors.toList())
        );
        relevantPlaylistList = new ArrayList<>(sourcePlaylistList);
        relevantPlaylistList.removeAll(savedPlaylistList);

        SearchPlaylistAddSongAdapter savedPlaylistAdapter = new SearchPlaylistAddSongAdapter(savedPlaylistList, new SearchPlaylistAddSongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Playlist item) {

            }
        }, true);
        LinearLayoutManager savedPlaylistLayoutManager = new LinearLayoutManager(this.getContext());
        savedPlaylistView.setAdapter(savedPlaylistAdapter);
        savedPlaylistView.setLayoutManager(savedPlaylistLayoutManager);
        savedPlaylistView.addItemDecoration(new VerticalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));

        SearchPlaylistAddSongAdapter relevantPlaylistAdapter = new SearchPlaylistAddSongAdapter(relevantPlaylistList, new SearchPlaylistAddSongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Playlist item) {

            }
        }, false);
        LinearLayoutManager relevantPlaylistLayoutManager = new LinearLayoutManager(this.getContext());
        relativePlaylistView.setAdapter(relevantPlaylistAdapter);
        relativePlaylistView.setLayoutManager(relevantPlaylistLayoutManager);
        relativePlaylistView.addItemDecoration(new VerticalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));

//        getParentFragmentManager().setFragmentResultListener("request_add_playlist", getViewLifecycleOwner(), (requestKey, result) ->{
//            relevantPlaylistAdapter.addPlaylist(new Playlist("dfd", result.getString("playlist_name"), "Fdfd", new ArrayList<>(),"",0,"",new ArrayList<>()));
//            relevantPlaylistAdapter.notifyDataSetChanged();
//        });

        searchEditText.setHint("Search for your playlist");
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Playlist> savedPlaylistResults = search(s.toString(), savedPlaylistList);
                List<Playlist> relevantPlaylistResults = (search(s.toString(), relevantPlaylistList));

                savedPlaylistAdapter.setPlaylistList(savedPlaylistList);
                savedPlaylistAdapter.notifyDataSetChanged();
                relevantPlaylistAdapter.setPlaylistList(relevantPlaylistResults);
                relevantPlaylistAdapter.notifyDataSetChanged();

                if(savedPlaylistResults.isEmpty())
                    savedPlaylistContainer.setVisibility(View.GONE);
                else
                    savedPlaylistContainer.setVisibility(View.VISIBLE);
                if(relevantPlaylistResults.isEmpty())
                    relevantPlaylistContainer.setVisibility(View.GONE);
                else
                    relevantPlaylistContainer.setVisibility(View.VISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        deleteAllText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(Map.Entry<String, Boolean> entry: savedPlaylistAdapter.getCheckStates().entrySet())
                {
                    entry.setValue(false);
                }
                savedPlaylistAdapter.notifyDataSetChanged();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int savedCount = 0;

                for(Map.Entry<String, Boolean> entry: savedPlaylistAdapter.getCheckStates().entrySet())
                {
                    if(!entry.getValue())
                    {
                        removeSong(entry.getKey(), id);
                    }
                    else
                    {
                        savedCount ++;
                    }
                }

                int relevantCount = 0;

                for(Map.Entry<String, Boolean> entry: relevantPlaylistAdapter.getCheckStates().entrySet())
                {
                    if(entry.getValue())
                    {
                        addSong(entry.getKey(), id);
                        relevantCount ++;
                    }
                }

                Bundle result = new Bundle();
                if(savedCount + relevantCount == 0)
                {
                    result.putString(ARG_ID, id);
                    getParentFragmentManager().setFragmentResult("remove_song", result);
                }
                else if(savedCount < savedPlaylistList.size() || relevantCount > 0)
                {
                    getParentFragmentManager().setFragmentResult("change",null);
                }

                callback.onRequestGoBackPreviousFragment();
            }
        });
    }

    private void addSong(String playlistId, String songId)
    {
        //TestData.getPlaylistById(playlistId).getModifiableSongsList().add(TestData.getSongById(songId));

    }
    private void removeSong(String playlistId, String songId)
    {
        //TestData.getPlaylistById(playlistId).getModifiableSongsList().removeIf(song -> song.getId().equals(songId));
    }

    private List<Playlist> search(String keyWord, List<Playlist> items)
    {
        if(!keyWord.isBlank())
        {
          return items.stream()
                   .filter(playlist -> Pattern
                                    .compile("\\b" + keyWord + ".*", Pattern.CASE_INSENSITIVE)
                                    .matcher(playlist.getName())
                                    .find())
                   .collect(Collectors.toList());
        }
        else
        {
            return items;
        }
    }
}