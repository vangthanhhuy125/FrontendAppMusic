package com.example.manhinhappmusic.fragment.admin;
import static java.security.AccessController.getContext;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.adapter.AdminSongAdapter;
import com.example.manhinhappmusic.dto.SongResponse;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.network.ApiClient;
import com.example.manhinhappmusic.network.ApiService;
import com.example.manhinhappmusic.view.ClearableEditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistDetailFragment extends BaseFragment {
    private Playlist playlist;
    private RecyclerView recyclerView;
    private AdminSongAdapter adminSongAdapter;
    private ImageButton backButton, searchButton;
    private ImageView playlistsCoverImage;
    private TextView playlistsTitle, playlistsCount;
    private List<Song> fullSongList = new ArrayList<>();
    private List<Song> filteredSongList = new ArrayList<>();
    private ApiService apiService;

    private ClearableEditText searchEditText;
    private LinearLayout inforLinearContainer;
    private boolean isSearching = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_detail_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiService = ApiClient.getApiService();
        playlist = (Playlist) getArguments().getSerializable("playlist");

        // Ánh xạ view
        recyclerView = view.findViewById(R.id.songs_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        playlistsCoverImage = view.findViewById(R.id.playlists_cover_image);
        playlistsTitle = view.findViewById(R.id.playlists_title);
        playlistsCount = view.findViewById(R.id.playlists_count);
        backButton = view.findViewById(R.id.back_button);
        searchButton = view.findViewById(R.id.search_button);
        searchEditText = view.findViewById(R.id.search_edit_text);
        inforLinearContainer = view.findViewById(R.id.infor_linear_container);

        adminSongAdapter = new AdminSongAdapter(filteredSongList, this::removeSongFromPlaylist);
        recyclerView.setAdapter(adminSongAdapter);

        backButton.setOnClickListener(v -> {
            if (callback != null) {
                callback.onRequestGoBackPreviousFragment();
            }
        });

        searchButton.setOnClickListener(v -> {
            if (!isSearching) {
                searchButton.setImageResource(R.drawable.baseline_close_30);
                searchEditText.setVisibility(View.VISIBLE);
                inforLinearContainer.setVisibility(View.GONE);
            } else {
                searchButton.setImageResource(R.drawable.baseline_search_30);
                searchEditText.setText(""); // clear text
                searchEditText.setVisibility(View.GONE);
                inforLinearContainer.setVisibility(View.VISIBLE);
                filteredSongList.clear();
                filteredSongList.addAll(fullSongList);
                adminSongAdapter.notifyDataSetChanged();
            }
            isSearching = !isSearching;
        });

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<Song> results = search(s.toString(), playlist.getSongsList());
                adminSongAdapter.setSongList(results);
                adminSongAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loadSongsInPlaylist();
        updatePlaylistInfo();
    }

    private void updatePlaylistInfo() {
        if (playlist == null) return;

        playlistsTitle.setText(playlist.getName());

        if (playlist.getThumbnailUrl() != null && !playlist.getThumbnailUrl().isEmpty()) {
            Glide.with(this)
                    .asBitmap()
                    .load(ApiService.BASE_URL + playlist.getThumbnailUrl())
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(20))))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            playlistsCoverImage.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {}
                    });
        } else {
            Glide.with(this)
                    .load(R.drawable.music_default_cover)
                    .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(20))))
                    .into(playlistsCoverImage);
        }
    }

    private void loadSongsInPlaylist() {
        apiService.getAllSongs(playlist.getId()).enqueue(new Callback<List<SongResponse>>() {
            @Override
            public void onResponse(Call<List<SongResponse>> call, Response<List<SongResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullSongList.clear();
                    for (SongResponse songResponse : response.body()) {
                        fullSongList.add(new Song(songResponse));
                        filteredSongList.add(new Song(songResponse));
                    }

                    filteredSongList.clear();
                    filteredSongList.addAll(fullSongList);

                    playlistsCount.setText(fullSongList.size() + " songs");
                    adminSongAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<SongResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải bài hát: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterSongs(String keyword) {
        filteredSongList.clear();
        if (keyword == null || keyword.trim().isEmpty()) {
            filteredSongList.addAll(fullSongList);
        } else {
            String lower = keyword.toLowerCase().trim();
            for (Song song : fullSongList) {
                if (song.getTitle().toLowerCase().contains(lower)) {
                    filteredSongList.add(song);
                }
            }
        }
        adminSongAdapter.notifyDataSetChanged();
    }

    private void removeSongFromPlaylist(String songId) {
        Map<String, List<String>> requestBody = new HashMap<>();
        requestBody.put("songIds", Collections.singletonList(songId));

        apiService.removeSongs(playlist.getId(), requestBody).enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Đã xoá bài hát", Toast.LENGTH_SHORT).show();
                    loadSongsInPlaylist();
                } else {
                    Toast.makeText(getContext(), "Xoá thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Song> search(String keyWord, List<Song> items)
    {
        if(!keyWord.isBlank())
        {
            return items.stream()
                    .filter(song -> Pattern.compile("\\b" + keyWord + ".*", Pattern.CASE_INSENSITIVE)
                            .matcher(song.getTitle())
                            .find())
                    .collect(Collectors.toList());

        }
        return items;
    }
}
