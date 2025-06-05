package com.example.manhinhappmusic.ui.fragment.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.adapter.SongAdapter;
import com.example.manhinhappmusic.decoration.VerticalLinearSpacingItemDecoration;
import com.example.manhinhappmusic.ui.fragment.BaseFragment;
import com.example.manhinhappmusic.model.MediaPlayerManager;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.repository.PlaylistRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserPlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserPlaylistFragment extends BaseFragment {

    private static final String ARG_ID = "ID";
    private String id;

    private ImageView playlistsCoverImage;
    private TextView playlistsTitle;
    private  TextView playlistsCount;
    private ImageButton backButton;
    private RecyclerView songsView;
    private SongAdapter songAdapter;
    private Playlist playlist;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
        {
            id = getArguments().getString(ARG_ID);
        }

        playlist = PlaylistRepository.getInstance().getItemById(id).getValue();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playlistsCoverImage = view.findViewById(R.id.playlists_cover_image);
        Glide.with(this.getContext())
                .load(playlist.getThumbnailUrl())
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                .into(playlistsCoverImage);
        playlistsTitle = view.findViewById(R.id.playlists_title);
        playlistsTitle.setText(playlist.getName());
        playlistsCount = view.findViewById(R.id.playlists_count);
        playlistsCount.setText(String.valueOf(playlist.getSongsList().size()) + " songs");
        backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRequestGoBackPreviousFragment();
            }
        });
        songAdapter = new SongAdapter(playlist.getSongsList(), new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance(null);
                mediaPlayerManager.setCurrentSong(position);
                mediaPlayerManager.play();
                callback.onRequestLoadMiniPlayer();

            }
        });
        songsView = view.findViewById(R.id.songs_view);
        songsView.setAdapter(songAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        songsView.setLayoutManager(linearLayoutManager);
        songsView.addItemDecoration(new VerticalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_playlist, container, false);
    }


}