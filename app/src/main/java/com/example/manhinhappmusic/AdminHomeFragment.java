package com.example.manhinhappmusic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Arrays;
import java.util.List;

public class AdminHomeFragment extends BaseFragment {

    private TextView totalUsers, totalArtists, totalGenres, totalSongs;
    private RecyclerView recyclerSongRequests, recyclerArtistRequests;
    private ImageView imageAvatar;

    public static AdminHomeFragment newInstance() {
        return new AdminHomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        totalUsers = view.findViewById(R.id.textad_total_users);
        totalArtists = view.findViewById(R.id.textad_total_artists);
        totalGenres = view.findViewById(R.id.textad_total_genres);
        totalSongs = view.findViewById(R.id.textad_total_songs);
        recyclerSongRequests = view.findViewById(R.id.recycler_song_requests);
        recyclerArtistRequests = view.findViewById(R.id.recycler_artist_requests);
        imageAvatar = view.findViewById(R.id.image_adavatar);


        totalUsers.setText("156");
        totalArtists.setText("42");
        totalGenres.setText("18");
        totalSongs.setText("432");


        Glide.with(this)
                .load(R.drawable.exampleavatar)
                .into(imageAvatar);


        imageAvatar.setOnClickListener(v -> {
            if (callback != null) {
                callback.onRequestChangeFragment(FragmentTag.ADMIN_PROFILE);
            }
        });


        List<String> songRequests = Arrays.asList("Song A - User1", "Song B - User2", "Song C - User3");
        List<String> artistRequests = Arrays.asList("Artist X - User4", "Artist Y - User5");


        recyclerSongRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerSongRequests.setAdapter(new SimpleTextAdapter(songRequests));

        recyclerArtistRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerArtistRequests.setAdapter(new SimpleTextAdapter(artistRequests));
    }


    public static class SimpleTextAdapter extends RecyclerView.Adapter<SimpleTextAdapter.ViewHolder> {

        private final List<String> items;

        public SimpleTextAdapter(List<String> items) {
            this.items = items;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView text;

            public ViewHolder(View itemView) {
                super(itemView);
                text = itemView.findViewById(android.R.id.text1);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.text.setText(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}
