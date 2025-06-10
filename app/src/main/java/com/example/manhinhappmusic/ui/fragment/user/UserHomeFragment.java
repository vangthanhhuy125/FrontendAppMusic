package com.example.manhinhappmusic.ui.fragment.user;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.adapter.HomeFeatureAdapter;
import com.example.manhinhappmusic.adapter.HomePlaylistAdapter;
import com.example.manhinhappmusic.adapter.HomeSongAdapter;
import com.example.manhinhappmusic.api.ApiClient;
import com.example.manhinhappmusic.api.common.CommonPlaylistApi;
import com.example.manhinhappmusic.api.common.CommonSongApi;
import com.example.manhinhappmusic.decoration.GridSpacingItemDecoration;
import com.example.manhinhappmusic.decoration.HorizontalLinearSpacingItemDecoration;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.ui.activity.MainActivity;
import com.example.manhinhappmusic.ui.fragment.BaseFragment;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHomeFragment extends BaseFragment {

    private RecyclerView playlistView, recentlyPlayView, featureView, newReleaseView;
    private ShapeableImageView userAvatarImage;

    private List<Playlist> playlistList = new ArrayList<>();
    private List<Playlist> featurePlaylistList = new ArrayList<>();
    private List<Song> newReleaseSongList = new ArrayList<>();
    private List<Song> recentlyPlaySongList = new ArrayList<>();
    private HomePlaylistAdapter playlistAdapter;
    private HomeFeatureAdapter featureAdapter;
    private HomeSongAdapter recentlySongAdapter;
    private HomeSongAdapter newReleaseSongAdapter;
    private String token;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        token = ((MainActivity) requireActivity()).getToken();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playlistView = view.findViewById(R.id.playlist_view);
        featureView = view.findViewById(R.id.featuring_view);
        recentlyPlayView = view.findViewById(R.id.recently_play_view);
        newReleaseView = view.findViewById(R.id.new_release_view);
        userAvatarImage = view.findViewById(R.id.user_avatar_image);

        userAvatarImage.setOnClickListener(this::onClickUserAvatarImage);

        // Playlist view
        playlistAdapter = new HomePlaylistAdapter(playlistList, position -> {}, token);
        GridLayoutManager playlistLayout = new GridLayoutManager(getContext(),2);
        playlistView.setLayoutManager(playlistLayout);
        playlistView.setAdapter(playlistAdapter);
        playlistView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(12), true));

        // Recently Played
        recentlySongAdapter = new HomeSongAdapter(recentlyPlaySongList, position -> {}, token);
        LinearLayoutManager recentlyLayout = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recentlyPlayView.setLayoutManager(recentlyLayout);
        recentlyPlayView.setAdapter(recentlySongAdapter);
        recentlyPlayView.addItemDecoration(new HorizontalLinearSpacingItemDecoration(dpToPx(12)));

        // New Release
        newReleaseSongAdapter = new HomeSongAdapter(newReleaseSongList, position -> {}, token);
        LinearLayoutManager newReleaseLayout = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        newReleaseView.setLayoutManager(newReleaseLayout);
        newReleaseView.setAdapter(newReleaseSongAdapter);
        newReleaseView.addItemDecoration(new HorizontalLinearSpacingItemDecoration(dpToPx(12)));

        // Featuring playlists
        featureAdapter = new HomeFeatureAdapter(token, featurePlaylistList, position ->{});
        LinearLayoutManager featureLayout = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        featureView.setLayoutManager(featureLayout);
        featureView.setAdapter(featureAdapter);
        featureView.addItemDecoration(new HorizontalLinearSpacingItemDecoration(dpToPx(12)));

        loadPlaylistsFromApi();
        loadSongFromApi();
    }

    private void loadPlaylistsFromApi() {
        CommonPlaylistApi playlistApi = ApiClient.getCommonPlaylistApi(requireContext());
        Call<List<Playlist>> call = playlistApi.getAllPlaylists();

        call.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    featurePlaylistList.clear();

                    playlistList.addAll(response.body());
                    featurePlaylistList.addAll(playlistList.subList(0, Math.min(playlistList.size(), 6)));

                    playlistAdapter.notifyDataSetChanged();
                    featureAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Không thể tải playlist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối khi tải playlist", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSongFromApi() {
        CommonSongApi songApi = ApiClient.getCommonSongApi(requireContext());

//        // Load Recently Played Songs
//        songApi.getRecentlySongs().enqueue(new Callback<List<Song>>() {
//            @Override
//            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    recentlyPlaySongList.clear();
//                    recentlyPlaySongList.addAll(response.body());
//                    recentlySongAdapter.notifyDataSetChanged();
//                } else {
//                    Toast.makeText(getContext(), "Không thể tải bài hát vừa nghe", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Song>> call, Throwable t) {
//                Toast.makeText(getContext(), "Lỗi kết nối khi tải bài hát vừa nghe", Toast.LENGTH_SHORT).show();
//            }
//        });

        // Load New Release Songs
        songApi.getNewReleaseSongs().enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    newReleaseSongList.clear();
                    newReleaseSongList.addAll(response.body());
                    newReleaseSongAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Không thể tải bài hát mới phát hành", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối khi tải bài hát mới phát hành", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onClickUserAvatarImage(View view) {
        try {
            callback.onRequestChangeFragment(FragmentTag.USER_PROFILE, null);
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
