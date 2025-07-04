package com.example.manhinhappmusic.fragment.user;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.example.manhinhappmusic.databinding.FragmentSeacrhExBinding;
import com.example.manhinhappmusic.dto.PlaylistResponse;
import com.example.manhinhappmusic.dto.SongResponse;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.repository.LibraryRepository;
import com.example.manhinhappmusic.repository.SearchRepository;
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

import okhttp3.ResponseBody;

public class SearchExFragment extends BaseFragment {

    public SearchExFragment()
    {

    }

    private FragmentSeacrhExBinding binding;
    private NavController navController;
    ImageButton backButton;
    RecyclerView searchResultView;
    ClearableEditText searchEditText;
    List<Playlist> userPlaylists = new ArrayList<>();
    private int modifiedPosition = -1;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        callback.setIsProcessing(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSeacrhExBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        backButton = binding.backButton;
        backButton.setOnClickListener(this::onBackButtonClick);
        searchResultView = binding.searchResultView;
        searchEditText = binding.searchEditText;



        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(new ArrayList<>(), new SparseBooleanArray(),
                (position, item) -> {

                    if(item.getItemType() == ListItemType.SONG)
                    {
                        MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance(null);
                        mediaPlayerManager.setPlaylist(new ArrayList<>(Arrays.asList(((SongResponse) item).toSong())));
                        mediaPlayerManager.setCurrentSong(0);
                        callback.onRequestLoadMiniPlayer();
                        mediaPlayerManager.play();
                    }
                    else if(item.getItemType() == ListItemType.PLAYLIST)
                    {
                        callback.onRequestChangeFragment(FragmentTag.USER_PLAYLIST, ((Playlist)item).getId());
                    }
                });
        searchResultAdapter.setOnItemCheckBoxClickListener(new SearchResultAdapter.OnItemCheckBoxClickListener() {
            @Override
            public void onItemCheckBoxClick(int position, ListItem item, CheckBox checkBox) {
                if(item.getItemType() == ListItemType.SONG)
                {
                    checkBox.setChecked(true);
                    searchResultAdapter.getCheckStates().put(position, true);
                    modifiedPosition = position;
                    Bundle bundle = new Bundle();
                    bundle.putString("songId", ((SongResponse) item).getId());
                    navController.navigate(R.id.addSongToPlaylistFragment, bundle);
                }
                else if(item.getItemType() == ListItemType.PLAYLIST)
                {
                    LibraryRepository.getInstance().removePlaylistFromLibrary(((PlaylistResponse)item).getId()).observe(getViewLifecycleOwner(), new Observer<ResponseBody>() {
                        @Override
                        public void onChanged(ResponseBody responseBody) {
                            Snackbar snackbar =  Snackbar.make(view,"Removed playlist from library", Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
                        }
                    });

                }

            }
        }, false);

        searchResultAdapter.setOnItemCheckBoxClickListener(new SearchResultAdapter.OnItemCheckBoxClickListener() {
            @Override
            public void onItemCheckBoxClick(int position, ListItem item, CheckBox checkBox) {
                if(item.getItemType() == ListItemType.SONG)
                {
//                    callback.setIsProcessing(true);
//                    PlaylistRepository.getInstance().addSongs(null, new ArrayList<>(Arrays.asList(((SongResponse)item).getId()))).observe(getViewLifecycleOwner(), new Observer<ResponseBody>() {
//                        @Override
//                        public void onChanged(ResponseBody result) {
//                            callback.setIsProcessing(false);
//                            Snackbar snackbar = Snackbar.make(view,"", Snackbar.LENGTH_LONG);
//                            View snackBarCustomLayout = LayoutInflater
//                                    .from(view.getContext())
//                                    .inflate(R.layout.snackbar_add_song_to_favorites, null);
//                            snackBarCustomLayout.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    modifiedPosition = position;
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("songId", ((SongResponse) item).getId());
//                                    navController.navigate(R.id.addSongToPlaylistFragment, bundle);
//                                    snackbar.dismiss();
//                                }
//                            });
//
//                            ViewGroup snackBarLayout = (ViewGroup) snackbar.getView();
//                            snackBarLayout.removeAllViews();
//                            snackBarLayout.addView(snackBarCustomLayout);
//                            snackbar.show();
//                        }
//                    }
//                );
                    checkBox.setChecked(false);
                    searchResultAdapter.getCheckStates().put(position, false);
                    modifiedPosition = position;
                    Bundle bundle = new Bundle();
                    bundle.putString("songId", ((SongResponse) item).getId());
                    navController.navigate(R.id.addSongToPlaylistFragment, bundle);

                }
                else if(item.getItemType() == ListItemType.PLAYLIST)
                {
                    LibraryRepository.getInstance().addPlaylistToLibrary(((PlaylistResponse)item).getId()).observe(getViewLifecycleOwner(), new Observer<ResponseBody>() {
                        @Override
                        public void onChanged(ResponseBody responseBody) {
                            Snackbar snackbar =  Snackbar.make(view,"Add playlist to library", Snackbar.LENGTH_SHORT);
                            snackbar.setBackgroundTint(Color.WHITE);
                            snackbar.setTextColor(Color.BLACK);
                            snackbar.show();
                        }
                    });

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
                if(!s.toString().isBlank())
                {
                    callback.setIsProcessing(true);

                    SearchRepository.getInstance().search(s.toString()).observe(getViewLifecycleOwner(), new Observer<List<ListItem>>() {
                        @Override
                        public void onChanged(List<ListItem> listItems) {
                            searchResultAdapter.setNewData(listItems, new SparseBooleanArray());
                            searchResultAdapter.notifyDataSetChanged();
                            callback.setIsProcessing(false);

                        }
                    });
                }
                else
                {
                    searchResultAdapter.setNewData(new ArrayList<>(), new SparseBooleanArray());
                    searchResultAdapter.notifyDataSetChanged();
                }


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
            if(modifiedPosition != -1)
            {
                searchResultAdapter.getCheckStates().put(modifiedPosition, true);
                searchResultAdapter.notifyItemChanged(modifiedPosition);

            }
            Snackbar snackbar =  Snackbar.make(view,"Saved changes", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();

        });

    }

    private void onBackButtonClick(View view){
        navController.popBackStack();
    }

    private SparseBooleanArray checkItemInLibrary(List<ListItem> items)
    {
        SparseBooleanArray checkStates = new SparseBooleanArray();
        for(int i = 0 ; i < items.size(); i++)
        {
            if(items.get(i).getItemType() == ListItemType.PLAYLIST)
            {
                Playlist playlist = (Playlist) items.get(i);
                for(Playlist userPlaylist: userPlaylists){
                    if(userPlaylist.getId().equals(playlist.getId())){
                        checkStates.put(i, true);
                        break;
                    }
                }
            }
            else if(items.get(i).getItemType() == ListItemType.SONG)
            {
                Song song  = (Song) items.get(i);
                for(Playlist playlist: userPlaylists)
                {
                    boolean isFound = false;
                    for(String userSong: playlist.getSongs())
                    {
                        if(song.getId().equals(userSong))
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}