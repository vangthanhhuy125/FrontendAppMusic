package com.example.manhinhappmusic.fragment.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.manhinhappmusic.adapter.ArtistSongAdapter;
import com.example.manhinhappmusic.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.model.User;

public class ArtistDetailFragment extends BaseFragment {

    private TextView tvNameArtist, tvEmailArtist, tvRoleArtist, tvJoinedArtist, tvTitle;
    private ImageView imgAvatarArtist;
    private ImageButton backButton;
    private RecyclerView rvSongsArtist;

    public ArtistDetailFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_artist_detail, container, false);
    }

    public static ArtistDetailFragment newInstance(User user) {
        ArtistDetailFragment fragment = new ArtistDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("user_data", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNameArtist = view.findViewById(R.id.tvNameArtist);
        tvEmailArtist = view.findViewById(R.id.tvEmailArtist);
        tvRoleArtist = view.findViewById(R.id.tvRoleArtist);
        tvJoinedArtist = view.findViewById(R.id.tvJoinedArtist);
        tvTitle = view.findViewById(R.id.tvTitle);
        imgAvatarArtist = view.findViewById(R.id.imgAvatarArtist);
        backButton = view.findViewById(R.id.back_button);
        rvSongsArtist = view.findViewById(R.id.rvSongsArtist);

        backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        User user = user = (User) getArguments().getSerializable("user_data");
        if (user != null) {
            tvNameArtist.setText(user.getFullName());
            tvEmailArtist.setText(user.getEmail());
            tvRoleArtist.setText(user.getRole());
            tvTitle.setText(user.getFullName());

            Glide.with(this)
                    .load(user.getAvatarUrl())
                    .placeholder(R.drawable.exampleavatar)
                    .into(imgAvatarArtist);

            List<Song> artistSongs = getSongsByArtistId(user.getId());
            rvSongsArtist.setLayoutManager(new LinearLayoutManager(getContext()));
            rvSongsArtist.setAdapter(new ArtistSongAdapter(artistSongs, null));
        }
    }

    private List<Song> getSongsByArtistId(String artistId) {
        List<Song> songs = new ArrayList<>();
        return songs;
    }

}