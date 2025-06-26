package com.example.manhinhappmusic.fragment.user;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.manhinhappmusic.databinding.FragmentUserPlaylistBinding;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.helper.CoverImageScrollHelper;
import com.example.manhinhappmusic.network.ApiService;
import com.example.manhinhappmusic.repository.SongRepository;
import com.example.manhinhappmusic.view.ClearableEditText;
import com.example.manhinhappmusic.repository.PlaylistRepository;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.adapter.SongAdapter;
import com.example.manhinhappmusic.decoration.VerticalLinearSpacingItemDecoration;
import com.example.manhinhappmusic.model.MediaPlayerManager;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.model.Song;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;
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

    private FragmentUserPlaylistBinding binding;
    private NavController navController;
    private ImageView playlistsCoverImage;
    private TextView playlistsTitle;
    private TextView playlistsCount;
    private ImageButton backButton;
    private ImageButton searchButton;
    private Button addButton;
    private Button editButton;
    private ImageButton addToLibraryButton;
    private ImageButton moreOptionsButton;
    private AppBarLayout appBarLayout;
    private NestedScrollView nestedScrollView;
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

        playlist = PlaylistRepository.getInstance().getCurrentPlaylist();

    }

    @Override
    public void onResume() {
        super.onResume();
        playlistsTitle.setText(playlist.getName());
        playlistsCount.setText(String.valueOf(playlist.getSongs().size()) + " songs");
        if(playlist.getThumbnailUrl() != null && !playlist.getThumbnailUrl().isEmpty())
            Glide.with(this.getContext())
                    .asBitmap()
                    .load(ApiService.BASE_URL + playlist.getThumbnailUrl())
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(20))))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        playlistsCoverImage.setImageBitmap(resource);
                        Palette.from(resource).generate(palette -> {
                            int vibrant = palette.getVibrantColor(Color.GRAY);
                            GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                    new int[]{vibrant,
                                            Color.parseColor("#121212"),
                                            Color.parseColor("#121212"),
                                            Color.parseColor("#121212"),
                                            });
                            binding.mainLayout.setBackground( gradientDrawable);
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        else
            Glide.with(this.getContext())
                    .load(R.drawable.music_default_cover)
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(20))))
                    .into(playlistsCoverImage);
        PlaylistRepository.getInstance().getAllSongs(playlist.getId()).observe(getViewLifecycleOwner(), new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                playlist.setSongsList(songs);
                songAdapter.setSongList(songs);
                songAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        nestedScrollView = binding.scrollView;
        playlistsCoverImage = binding.playlistsCoverImage;
        playlistsTitle = binding.playlistsTitle;
        playlistsCount = binding.playlistsCount;
        backButton = binding.backButton;
        searchButton = binding.searchButton;
        addButton = binding.addButton;
        editButton = binding.editButton;
        addToLibraryButton = binding.addToLibraryButton;
        moreOptionsButton = binding.moreOptionsButton;
//        shuffleButton = view.findViewById(R.id.shuffle_button);
        playButton = binding.playButton;
        searchEditText = binding.searchEditText;
        inforLinearContainer = binding.inforLinearContainer;
        songsView = binding.songsView;


       new CoverImageScrollHelper(playlistsCoverImage, nestedScrollView, 300f, 0.5f, 0.5f);

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
        songAdapter = new SongAdapter(new ArrayList<>(), new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Song song) {
                MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance(null);
                if(!isPlaying)
                {
                    mediaPlayerManager.setPlaylist(playlist.getSongsList());
                    isPlaying = true;
                }
                mediaPlayerManager.setCurrentSong(position);
                callback.onRequestLoadMiniPlayer();

            }
        });
        songAdapter.setOnItemMoreOptionsClickListener(new SongAdapter.OnItemMoreOptionsClickListener() {
            @Override
            public void onItemMoreOptionsCLick(int position, Song song) {
                modifiedPosition = position;
                SongRepository.getInstance().setCurrentSong(song);
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
                navController.popBackStack();
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
                Bundle bundle = new Bundle();
                bundle.putString("playlistId", playlist.getId());
                navController.navigate(R.id.userPlaylistAddSongFragment,bundle );
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("playlistId", playlist.getId());
                navController.navigate(R.id.editPlaylistFragment, bundle);
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
                if(!isPlaying)
                {
                    mediaPlayerManager.setPlaylist(playlist.getSongsList());
                    isPlaying = true;
                }
                mediaPlayerManager.setCurrentSong(0);
                callback.onRequestLoadMiniPlayer();
            }
        });


        getParentFragmentManager().setFragmentResultListener("update_playlist", getViewLifecycleOwner(), (requestKey, result)->{

//           playlist = PlaylistRepository.getInstance().getCurrentPlaylist();
            Glide.with(this.getContext())
                    .asBitmap()
                    .load(ApiService.BASE_URL + playlist.getThumbnailUrl())
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(20))))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            playlistsCoverImage.setImageBitmap(resource);
                            Palette.from(resource).generate(palette -> {
                                int vibrant = palette.getVibrantColor(Color.GRAY);
                                GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                                        new int[]{vibrant,
                                                Color.parseColor("#121212"),
                                                Color.parseColor("#121212"),
                                                Color.parseColor("#121212"),
                                        });
                                binding.mainLayout.setBackground( gradientDrawable);
                            });
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
            playlistsTitle.setText(playlist.getName());
            playlistsCount.setText(String.valueOf(playlist.getSongsList().size()) + " songs");
            songAdapter.setSongList(playlist.getSongsList());
            songAdapter.notifyDataSetChanged();
            //            getParentFragmentManager().setFragmentResult("update_library_when_playlist_got_modified", null);

        });

        getParentFragmentManager().setFragmentResultListener("add_song_to_other_playlist", getViewLifecycleOwner(),(requestKey, result) -> {

            callback.onRequestChangeFrontFragment(FragmentTag.USER_SEARCH_ADD_SONG, songAdapter.getSongList().get(modifiedPosition).getId());
        });

        getParentFragmentManager().setFragmentResultListener("remove_song_from_this_playlist", getViewLifecycleOwner(),(requestKey, result) -> {
            if(modifiedPosition != -1)
            {
                Toast.makeText(getContext(), "Song has been removed", Toast.LENGTH_SHORT).show();
                playlist.getSongsList().remove(modifiedPosition);
                songAdapter.notifyItemRemoved(modifiedPosition);

            }
        });

        getParentFragmentManager().setFragmentResultListener("add_song_to_this_playlist", getViewLifecycleOwner(),(requestKey, result) -> {

            Bundle bundle = new Bundle();
            bundle.putString("playlistId", playlist.getId());
            navController.navigate(R.id.userPlaylistAddSongFragment,bundle );
        });
        getParentFragmentManager().setFragmentResultListener("edit_playlist", getViewLifecycleOwner(),(requestKey, result) -> {

            Bundle bundle = new Bundle();
            bundle.putString("playlistId", playlist.getId());
            navController.navigate(R.id.editPlaylistFragment,bundle );
        });
        getParentFragmentManager().setFragmentResultListener("delete_playlist", getViewLifecycleOwner(),(requestKey, result) -> {

            PlaylistRepository.getInstance().delete(playlist.getId()).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if(aBoolean)
                    {
//                        getParentFragmentManager().setFragmentResult("update_library_when_playlist_got_deleted", null);
                        Toast.makeText(getContext(), "Playlist has been deleted", Toast.LENGTH_SHORT).show();
                        callback.onRequestGoBackPreviousFragment();
                    }

                }
            });

        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserPlaylistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        binding = null;
    }

    private List<Song> search(String keyWord, List<Song> items)
    {
        if(!keyWord.isBlank())
        {
//            return items.stream()
//                    .filter(song -> {
//                        for(String itemKeyWord: song.getSearchKeyWord())
//                        {
//                            if(Pattern.compile("\\b" + keyWord + ".*", Pattern.CASE_INSENSITIVE)
//                                    .matcher(itemKeyWord)
//                                    .find())
//                            {
//                                return true;
//                            }
//                        }
//                        return false;
//                    })
//                    .collect(Collectors.toList());

            return items.stream()
                    .filter(song -> Pattern.compile("\\b" + keyWord + ".*", Pattern.CASE_INSENSITIVE)
                                    .matcher(song.getTitle())
                                    .find())
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