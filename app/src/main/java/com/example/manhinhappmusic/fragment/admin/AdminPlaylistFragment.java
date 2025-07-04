package com.example.manhinhappmusic.fragment.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.activity.AdminActivity;
import com.example.manhinhappmusic.adapter.PlaylistAdapter;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.fragment.user.UserPlaylistFragment;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.network.ApiClient;
import com.example.manhinhappmusic.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPlaylistFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private PlaylistAdapter playlistAdapter;
    private final List<Playlist> playlistList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        playlistAdapter = new PlaylistAdapter(playlistList, position -> {
            Playlist selectedPlaylist = playlistList.get(position);

            Bundle bundle = new Bundle();
            bundle.putSerializable("playlist", selectedPlaylist); // đảm bảo Playlist implement Serializable

            PlaylistDetailFragment fragment = new PlaylistDetailFragment();
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerView.setAdapter(playlistAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    ((AdminActivity) requireActivity()).hideBottomNav();
                } else if (dy < 0) {
                    ((AdminActivity) requireActivity()).showBottomNav();
                }
            }
        });

        loadPublicPlaylists();
    }

    private void loadPublicPlaylists() {
        ApiService apiService = ApiClient.getApiService();

        apiService.getAllPublicPlaylists().enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    playlistList.clear();
                    playlistList.addAll(response.body());
                    playlistAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
