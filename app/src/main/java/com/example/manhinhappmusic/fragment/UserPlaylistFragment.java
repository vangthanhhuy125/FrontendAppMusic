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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.manhinhappmusic.view.ClearableEditText;
import com.example.manhinhappmusic.repository.PlaylistRepository;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.adapter.SongAdapter;
import com.example.manhinhappmusic.TestData;
import com.example.manhinhappmusic.decoration.VerticalLinearSpacingItemDecoration;
import com.example.manhinhappmusic.model.MediaPlayerManager;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.model.Song;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserPlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserPlaylistFragment extends BaseFragment {

    private static final String ARG_ID = "ID";
    private static final String ARG_IS_THIS_PLAYING = "IS_THIS_PLAYING";
    private String id;

    private ImageView playlistsCoverImage;
    private TextView playlistsTitle;
    private TextView playlistsCount;
    private ImageButton backButton;
    private ImageButton searchButton;
    private Button addButton;
    private Button editButton;
    private ImageButton addToLibraryButton;
    private ImageButton moreOptionsButton;
//    private ImageButton shuffleButton;
    private ImageButton playButton;
    private RecyclerView songsView;
    private LinearLayout inforLinearContainer;
    private ClearableEditText searchEditText;
    private SongAdapter songAdapter;
    private Playlist playlist;
    private boolean isPlaying =  false;

    private int modifiedPosition = -1;

    private boolean isModified = false;
    private boolean isSearching = false;

    public UserPlaylistFragment() {
        // Required empty public constructor
    }

    public static UserPlaylistFragment newInstance(String id) {
        UserPlaylistFragment fragment = new UserPlaylistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_IS_THIS_PLAYING, isPlaying);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
        {
            id = getArguments().getString(ARG_ID);
            isPlaying = getArguments().getBoolean(ARG_IS_THIS_PLAYING);
        }

        playlist = PlaylistRepository.getInstance().getItemById(id).getValue();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playlistsCoverImage = view.findViewById(R.id.playlists_cover_image);
        playlistsTitle = view.findViewById(R.id.playlists_title);
        playlistsCount = view.findViewById(R.id.playlists_count);
        backButton = view.findViewById(R.id.back_button);
        searchButton = view.findViewById(R.id.search_button);
        addButton = view.findViewById(R.id.add_button);
        editButton = view.findViewById(R.id.edit_button);
        addToLibraryButton = view.findViewById(R.id.add_to_library_button);
        moreOptionsButton = view.findViewById(R.id.more_options_button);
//        shuffleButton = view.findViewById(R.id.shuffle_button);
        playButton = view.findViewById(R.id.play_button);
        searchEditText = view.findViewById(R.id.search_edit_text);
        inforLinearContainer = view.findViewById(R.id.infor_linear_container);
        songsView = view.findViewById(R.id.songs_view);


        Glide.with(this.getContext())
                .load(playlist.getThumnailResID())
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                .into(playlistsCoverImage);
        playlistsTitle.setText(playlist.getName());
        playlistsCount.setText(String.valueOf(playlist.getSongsList().size()) + " songs");
        searchEditText.setHint("Search in playlist");
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Song> results = search(s.toString(), playlist.getSongsList());
                songAdapter.setSongList(results);
                songAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        songAdapter = new SongAdapter(playlist.getSongsList(), new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Song song) {
                MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance(null);
                if(!isPlaying)
                {
                    mediaPlayerManager.setPlaylist(playlist.getSongsList());
                    isPlaying = true;
                }
                mediaPlayerManager.setCurrentSong(position);
                mediaPlayerManager.play();
                callback.onRequestLoadMiniPlayer();

            }
        });
        songAdapter.setOnItemMoreOptionsClickListener(new SongAdapter.OnItemMoreOptionsClickListener() {
            @Override
            public void onItemMoreOptionsCLick(int position, Song song) {
                modifiedPosition = position;
                MoreOptionsSongFragment moreOptionsSongFragment = MoreOptionsSongFragment.newInstance(song.getId());
                moreOptionsSongFragment.show(getParentFragmentManager(), "");
            }
        });
        songsView.setAdapter(songAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        songsView.setLayoutManager(linearLayoutManager);
        songsView.addItemDecoration(new VerticalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRequestGoBackPreviousFragment();
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isSearching)
                {
                    searchButton.setImageResource(R.drawable.baseline_close_30);
                    searchEditText.setVisibility(View.VISIBLE);
                    inforLinearContainer.setVisibility(View.GONE);

                }
                else {
                    searchButton.setImageResource(R.drawable.baseline_search_30);
                    searchEditText.setVisibility(View.GONE);
                    inforLinearContainer.setVisibility(View.VISIBLE);
                }
                isSearching = !isSearching;
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRequestChangeFragment(FragmentTag.PLAYLIST_ADD_SONG, playlist.getId());
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRequestChangeFrontFragment(FragmentTag.PLAYLIST_EDIT, playlist.getId());
            }
        });

        addToLibraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        moreOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreOptionsPlaylistFragment moreOptionsPlaylistFragment = MoreOptionsPlaylistFragment.newInstance(playlist.getId());
                moreOptionsPlaylistFragment.show(getParentFragmentManager(), "");
            }
        });

//        shuffleButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance(null);
//                boolean isShuffle = mediaPlayerManager.isShuffle();
//                if(!isShuffle)
//                {
//                    mediaPlayerManager.setPlaylist(playlist.getSongsList());
//                    shuffleButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#3be477")));
//                }
//                else
//                {
//                    shuffleButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#CCFFFFFF")));
//                }
//                mediaPlayerManager.setShuffle(!isShuffle);
//            }
//        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance(null);
                isPlaying = true;
                mediaPlayerManager.setPlaylist(playlist.getSongList());
                mediaPlayerManager.setCurrentSong(0);
                mediaPlayerManager.play();
                callback.onRequestLoadMiniPlayer();
            }
        });


        getParentFragmentManager().setFragmentResultListener("update_playlist", getViewLifecycleOwner(), (requestKey, result)->{
            Glide.with(this.getContext())
                    .load(playlist.getThumnailResID())
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                    .into(playlistsCoverImage);
            playlistsTitle.setText(playlist.getName());
            playlistsCount.setText(String.valueOf(playlist.getSongsList().size()) + " songs");
            isModified = true;
            songAdapter.notifyDataSetChanged();
        });

        getParentFragmentManager().setFragmentResultListener("add_song_to_other_playlist", getViewLifecycleOwner(),(requestKey, result) -> {

            callback.onRequestChangeFrontFragment(FragmentTag.USER_SEARCH_ADD_SONG, songAdapter.getSongList().get(modifiedPosition).getId());
        });

        getParentFragmentManager().setFragmentResultListener("remove_song_from_this_playlist", getViewLifecycleOwner(),(requestKey, result) -> {
            if(modifiedPosition != -1)
            {
                //playlist.getModifiableSongsList().remove(modifiedPosition);
                songAdapter.notifyItemRemoved(modifiedPosition);
            }
        });

        getParentFragmentManager().setFragmentResultListener("add_song_to_this_playlist", getViewLifecycleOwner(),(requestKey, result) -> {

            callback.onRequestChangeFragment(FragmentTag.PLAYLIST_ADD_SONG, playlist.getId());
        });
        getParentFragmentManager().setFragmentResultListener("edit_playlist", getViewLifecycleOwner(),(requestKey, result) -> {

            callback.onRequestChangeFrontFragment(FragmentTag.PLAYLIST_EDIT, playlist.getId());
        });
        getParentFragmentManager().setFragmentResultListener("delete_playlist", getViewLifecycleOwner(),(requestKey, result) -> {

            TestData.playlistList.remove(playlist);
            getParentFragmentManager().setFragmentResult("update_library_when_playlist_got_deleted", null);
            callback.onRequestGoBackPreviousFragment();
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_playlist, container, false);
    }

    private List<Song> search(String keyWord, List<Song> items)
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
        return items;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(isModified)
            getParentFragmentManager().setFragmentResult("update_library_when_playlist_got_modified", null);

    }
}