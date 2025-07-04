package com.example.manhinhappmusic.fragment.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.activity.AdminActivity;
import com.example.manhinhappmusic.adapter.HomeSongAdapter;
import com.example.manhinhappmusic.dto.GenreResponse;
import com.example.manhinhappmusic.dto.UserResponse;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.model.Genre;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.model.User;
import com.example.manhinhappmusic.network.ApiClient;
import com.example.manhinhappmusic.network.ApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminHomeFragment extends BaseFragment {
    private TextView totalUsers, totalGenres, totalSongs, totalArtists;
    private ImageView imageAvatar;
    private List<View> highlightViews = new ArrayList<>();
    private User currentUser;

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
        imageAvatar = view.findViewById(R.id.image_adavatar);

        // Gán view các item_highlights
        highlightViews.add(view.findViewById(R.id.item_highlight_1));
        highlightViews.add(view.findViewById(R.id.item_highlight_2));
        highlightViews.add(view.findViewById(R.id.item_highlight_3));

        // Lấy user từ SharedPreferences
        if (getActivity() != null) {
            String userJson = getActivity()
                    .getSharedPreferences("AppPreferences", getActivity().MODE_PRIVATE)
                    .getString("current_user", null);
            if (userJson != null) {
                currentUser = new com.google.gson.Gson().fromJson(userJson, User.class);
            }
        }

        imageAvatar.setOnClickListener(v -> {
            if (callback != null && currentUser != null) {
                callback.onRequestChangeFragment(FragmentTag.ADMIN_PROFILE);
            }
        });

        fetchAllGenres();
        getPopularSongs();
        fetchAllUsers();
        getAllArtists();
    }

    private void getAllArtists() {
        ApiService api = ApiClient.getApiService();
        api.getAllArtists().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    totalArtists.setText(String.valueOf(response.body().size()));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                totalArtists.setText("0");
            }
        });
    }

    private void fetchAllGenres() {
        ApiService api = ApiClient.getApiService();
        api.getAllGenres().enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    totalGenres.setText(String.valueOf(response.body().size()));
                }
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {
                totalGenres.setText("0");
            }
        });
    }

    private void getPopularSongs() {
        ApiService apiService = ApiClient.getApiService();
        apiService.getPopularSongs().enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Song> songs = response.body();
                    totalSongs.setText(String.valueOf(songs.size()));

                    List<Song> topSongs = new ArrayList<>(songs);
                    if (topSongs.stream().allMatch(s -> s.getViews() == 0)) {
                        topSongs.sort(Comparator.comparing(Song::getTitle));
                    } else {
                        topSongs.sort((s1, s2) -> Double.compare(s2.getViews(), s1.getViews()));
                    }

                    for (int i = 0; i < 3 && i < highlightViews.size(); i++) {
                        Song song = topSongs.get(i);
                        View itemView = highlightViews.get(i);

                        TextView tvName = itemView.findViewById(R.id.tv_ext_name);
                        TextView tvView = itemView.findViewById(R.id.text_view_count);
                        ImageView img = itemView.findViewById(R.id.img_user);

                        tvName.setText(song.getTitle());
                        tvView.setText(String.valueOf(song.getViews()));

                        Glide.with(requireContext())
                                .load(song.getCoverImageUrl())
                                .placeholder(R.drawable.exampleavatar)
                                .into(img);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                totalSongs.setText("0");
            }
        });
    }

    private void fetchAllUsers() {
        ApiService api = ApiClient.getApiService();

        api.getAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int userCount = response.body().size();

                    api.getAllArtists().enqueue(new Callback<List<User>>() {
                        @Override
                        public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                int artistCount = response.body().size();
                                int totalCount = userCount + artistCount;
                                totalUsers.setText(String.valueOf(totalCount));
                            } else {
                                totalUsers.setText(String.valueOf(userCount));
                            }
                        }

                        @Override
                        public void onFailure(Call<List<User>> call, Throwable t) {
                            totalUsers.setText(String.valueOf(userCount));
                        }
                    });

                } else {
                    totalUsers.setText("0");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                totalUsers.setText("0");
            }
        });
    }

}
