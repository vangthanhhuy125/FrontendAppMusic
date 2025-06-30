package com.example.manhinhappmusic;

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

import java.util.ArrayList;
import java.util.List;

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

        if (getArguments() != null) {
            User user = getArguments().getParcelable("user_data");
            if (user != null) {
                tvNameArtist.setText(user.getFullName());
                tvEmailArtist.setText(user.getEmail());
                tvRoleArtist.setText(user.getRole());
                tvJoinedArtist.setText("Joined at: " + user.getCreatedAt());
                tvTitle.setText(user.getFullName());

                if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                    Glide.with(this)
                            .load(user.getAvatarUrl())
                            .placeholder(R.drawable.exampleavatar)
                            .into(imgAvatarArtist);
                } else {
                    imgAvatarArtist.setImageResource(user.getAvatarResID() != 0 ? user.getAvatarResID() : R.drawable.exampleavatar);
                }

                             List<Song> artistSongs = getSongsByArtistId(user.getId());
                rvSongsArtist.setLayoutManager(new LinearLayoutManager(getContext()));
                rvSongsArtist.setAdapter(new ArtistSongAdapter(getContext(), artistSongs));
            }
        }
    }

    private List<Song> getSongsByArtistId(String artistId) {
        List<Song> songs = new ArrayList<>();
        songs.add(new Song("1", "Song A", artistId, "", "", "", null));
        songs.add(new Song("2", "Song B", artistId, "", "", "", null));
        return songs;
    }

}
