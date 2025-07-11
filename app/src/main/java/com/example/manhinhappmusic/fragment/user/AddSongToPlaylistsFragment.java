package com.example.manhinhappmusic.fragment.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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

import com.example.manhinhappmusic.databinding.FragmentAddSongToPlaylistsBinding;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.repository.PlaylistRepository;
import com.example.manhinhappmusic.repository.SongRepository;
import com.example.manhinhappmusic.view.ClearableEditText;
import com.example.manhinhappmusic.decoration.VerticalLinearSpacingItemDecoration;
import com.example.manhinhappmusic.adapter.SearchPlaylistAddSongAdapter;
import com.example.manhinhappmusic.model.Playlist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddSongToPlaylistsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddSongToPlaylistsFragment extends BaseFragment {

    private NavController navController;
    private FragmentAddSongToPlaylistsBinding binding;
    private ImageButton backButton;
    private Button newPlaylistButton;
    private ClearableEditText searchEditText;
    private TextView deleteAllText;
    private RecyclerView savedPlaylistView;
    private RecyclerView relativePlaylistView;
    private Button saveButton;
    private LinearLayout savedPlaylistContainer;
    private LinearLayout relevantPlaylistContainer;
    private  List<Playlist> savedPlaylistList = new ArrayList<>();
    private List <Playlist> relevantPlaylistList = new ArrayList<>();
    private SearchPlaylistAddSongAdapter savedPlaylistAdapter;
    private SearchPlaylistAddSongAdapter relevantPlaylistAdapter;


    private static final String ARG_ID = "songId";


    private String songId;

    public AddSongToPlaylistsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddSongToPlaylistsFragment newInstance(String id) {
        AddSongToPlaylistsFragment fragment = new AddSongToPlaylistsFragment();
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
            songId = getArguments().getString(ARG_ID);
        }
        bottomNavVisibility = View.GONE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddSongToPlaylistsBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        PlaylistRepository.getInstance().getAll().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                SongRepository.getInstance().getPlaylistSongsBelongs(songId).observe(getViewLifecycleOwner(), new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> playlistIds) {
                        for(Playlist playlist : playlists)
                        {
                            if(playlistIds.stream().anyMatch(s -> s.equals(playlist.getId())))
                                savedPlaylistList.add(playlist);
                            else
                                relevantPlaylistList.add(playlist);
                        }
                        savedPlaylistAdapter.setPlaylistList(savedPlaylistList);
                        savedPlaylistAdapter.checkAll();
                        savedPlaylistAdapter.notifyDataSetChanged();
                        relevantPlaylistAdapter.setPlaylistList(relevantPlaylistList);
                        relevantPlaylistAdapter.notifyDataSetChanged();
                        callback.setIsProcessing(false);

                    }
                });

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        backButton = binding.backButton;
        newPlaylistButton = binding.newPlaylistButton;
        searchEditText = binding.searchEditText;
        deleteAllText = binding.deleteAllText;
        savedPlaylistView = binding.savedPlaylistView;
        relativePlaylistView = binding.relevantPlaylistView;
        saveButton = binding.saveButton;
        savedPlaylistContainer = binding.savedPlaylistContainer;
        relevantPlaylistContainer = binding.relevantPlaylistContainer;

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.popBackStack();
            }
        });
        newPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddPlaylistFragment addPlaylistFragment = new AddPlaylistFragment();
                addPlaylistFragment.show(getParentFragmentManager(), null);
            }
        });



        savedPlaylistAdapter = new SearchPlaylistAddSongAdapter(new ArrayList<>(), new SearchPlaylistAddSongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Playlist item) {

            }
        }, true);
        LinearLayoutManager savedPlaylistLayoutManager = new LinearLayoutManager(this.getContext());
        savedPlaylistView.setAdapter(savedPlaylistAdapter);
        savedPlaylistView.setLayoutManager(savedPlaylistLayoutManager);
        savedPlaylistView.addItemDecoration(new VerticalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));

        relevantPlaylistAdapter = new SearchPlaylistAddSongAdapter(new ArrayList<>(), new SearchPlaylistAddSongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Playlist item) {

            }
        }, false);
        LinearLayoutManager relevantPlaylistLayoutManager = new LinearLayoutManager(this.getContext());
        relativePlaylistView.setAdapter(relevantPlaylistAdapter);
        relativePlaylistView.setLayoutManager(relevantPlaylistLayoutManager);
        relativePlaylistView.addItemDecoration(new VerticalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));

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
                        removeSong(entry.getKey(), songId);
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
                        addSong(entry.getKey(), songId);
                        relevantCount ++;
                    }
                }

                Bundle result = new Bundle();
                if(savedCount + relevantCount == 0)
                {
                    result.putString(ARG_ID, songId);
                    getParentFragmentManager().setFragmentResult("remove_song", result);
                }
                else if(savedCount < savedPlaylistList.size() || relevantCount > 0)
                {
                    getParentFragmentManager().setFragmentResult("change",null);
                }

                navController.popBackStack();
            }
        });

        getParentFragmentManager().setFragmentResultListener("request_add_playlist", getViewLifecycleOwner(), (requestKey, result) ->{
            String playlistName = result.getString("playlist_name", "null");
            onResume();

        });
    }

    private void addSong(String playlistId, String songId)
    {
        PlaylistRepository.getInstance().addSongs(playlistId, new ArrayList<>(Arrays.asList(songId))).observe(getViewLifecycleOwner(), new Observer<ResponseBody>() {
            @Override
            public void onChanged(ResponseBody s) {

            }
        });

    }
    private void removeSong(String playlistId, String songId)
    {
        PlaylistRepository.getInstance().removeSongs(playlistId, new ArrayList<>(Arrays.asList(songId))).observe(getViewLifecycleOwner(), new Observer<Playlist>() {
            @Override
            public void onChanged(Playlist playlist) {


            }
        });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding =  null;
    }
}