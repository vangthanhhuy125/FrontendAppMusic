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

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.TestData;
import com.example.manhinhappmusic.adapter.MusicDisplayAdapter;
import com.example.manhinhappmusic.databinding.FragmentUserGenreBinding;
import com.example.manhinhappmusic.decoration.VerticalLinearSpacingItemDecoration;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.model.Genre;
import com.example.manhinhappmusic.model.ListItem;
import com.example.manhinhappmusic.model.ListItemType;
import com.example.manhinhappmusic.model.MediaPlayerManager;
import com.example.manhinhappmusic.model.MusicDisplayItem;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.model.User;
import com.example.manhinhappmusic.repository.ArtistRepository;
import com.example.manhinhappmusic.repository.GenreRepository;
import com.example.manhinhappmusic.repository.PlaylistRepository;
import com.example.manhinhappmusic.repository.SongRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserGenreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserGenreFragment extends BaseFragment {


    private static final String ARG_ID = "genreId";
    private static final String ARG_NAME = "genreName";

    private NavController navController;
    private FragmentUserGenreBinding binding;
    private String id;
    private String name;
    private TextView genreTitleText;
    private RecyclerView musicDisplayView;
    private MusicDisplayAdapter musicDisplayAdapter;
    private ImageButton backButton;
    private Genre genre;

    public UserGenreFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static UserGenreFragment newInstance(String id) {
        UserGenreFragment fragment = new UserGenreFragment();
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
            name = getArguments().getString(ARG_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserGenreBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        genreTitleText.setText(name);

        List<MusicDisplayItem> musicDisplayItems = musicDisplayAdapter.getItems();
        musicDisplayItems.clear();

        PlaylistRepository.getInstance().getNewReleasePlaylists().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                musicDisplayItems.add(new MusicDisplayItem("aaa", "New release", new ArrayList<>(playlists), MusicDisplayItem.HomeDisplayType.RELEASE_PLAYLIST));
                musicDisplayAdapter.notifyItemInserted(musicDisplayAdapter.getItemCount() - 1);

            }

        });
        PlaylistRepository.getInstance().getFeaturedPlaylists().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {

                musicDisplayItems.add(new MusicDisplayItem("aaa", "Featuring", new ArrayList<>(playlists), MusicDisplayItem.HomeDisplayType.RELEASE_PLAYLIST));
                musicDisplayAdapter.notifyItemInserted(musicDisplayAdapter.getItemCount() - 1);

            }

        });

        ArtistRepository.getInstance().getTrendingArtist().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {

                musicDisplayItems.add(new MusicDisplayItem("aaa", "Trending artist", new ArrayList<>(users), MusicDisplayItem.HomeDisplayType.ARTIST));                musicDisplayAdapter.notifyItemInserted(musicDisplayAdapter.getItemCount() - 1);
                musicDisplayAdapter.notifyItemInserted(musicDisplayAdapter.getItemCount() - 1);
                callback.setIsProcessing(false);

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        genreTitleText = binding.genreTitleText;
        musicDisplayView = binding.musicDisplayView;
        backButton = binding.backButton;

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.popBackStack();
            }
        });

        musicDisplayView = view.findViewById(R.id.music_display_view);
        musicDisplayAdapter = new MusicDisplayAdapter(new ArrayList<>(), new MusicDisplayAdapter.OnDisplayItemCLickListener() {
            @Override
            public void onDisplayItemClick(int position, ListItem item) {
                if(item.getItemType() == ListItemType.PLAYLIST)
                {
                    Playlist playlist = (Playlist) item;
                    PlaylistRepository.getInstance().setCurrentPlaylist(playlist);
                    navController.navigate(R.id.userPlaylistFragment);
                }
                else if (item.getItemType() == ListItemType.SONG)
                {
                    MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance(null);
                    Song song = (Song) item;
                    mediaPlayerManager.setPlaylist(new ArrayList<>(Arrays.asList(song)));
                    mediaPlayerManager.setCurrentSong(0);
                    callback.onRequestLoadMiniPlayer();
                }
                else if(item.getItemType() == ListItemType.ARTIST)
                {
                    User artist = (User) item;
                    Bundle bundle = new Bundle();
                    bundle.putString("artistId", artist.getId());
                    navController.navigate(R.id.userArtistFragment, bundle);
                }
            }
        });
        musicDisplayView.setAdapter(musicDisplayAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        musicDisplayView.setLayoutManager(layoutManager);
        musicDisplayView.addItemDecoration(new VerticalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics())));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}