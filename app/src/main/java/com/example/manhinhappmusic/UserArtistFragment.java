package com.example.manhinhappmusic;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserArtistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserArtistFragment extends BaseFragment {

    private ImageView artistImage;
    private TextView artistNameText;
    private RecyclerView songsView;
    private ImageButton backButton;
    private User artist;


    private static final String ARG_ID = "id";

    private String id;

    public UserArtistFragment() {
        // Required empty public constructor
    }

    public static UserArtistFragment newInstance(String id) {
        UserArtistFragment fragment = new UserArtistFragment();
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
        return inflater.inflate(R.layout.fragment_user_artist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backButton = view.findViewById(R.id.back_button);
        artistImage = view.findViewById(R.id.artist_image);
        artistNameText = view.findViewById(R.id.artist_name_text);
        songsView = view.findViewById(R.id.songs_view);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRequestGoBackPreviousFragment();
            }
        });

        artist = ArtistRepository.getInstance().getItemById(id).getValue();

        artistImage.setImageResource(artist.getAvatarResID());
        artistNameText.setText(artist.getFullName());
        List<Song> songs = SongRepository.getInstance().getAll().getValue();

        SearchResultAdapter songAdapter = new SearchResultAdapter(new ArrayList<>(songs), new SparseBooleanArray(),new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ListItem item) {
                if(item.getType() == ListItemType.SONG)
                {
                    Song song = (Song) item;
                    MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance(null);
                    mediaPlayerManager.setPlaylist(new ArrayList<>(Arrays.asList(song)));
                    mediaPlayerManager.setCurrentSong(0);
                    callback.onRequestLoadMiniPlayer();
                    mediaPlayerManager.play();
                }
            }
        });
        LinearLayoutManager songLayoutManager = new LinearLayoutManager(this.getContext());
        songsView.setAdapter(songAdapter);
        songsView.setLayoutManager(songLayoutManager);
        songsView.addItemDecoration(new VerticalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));

    }
}