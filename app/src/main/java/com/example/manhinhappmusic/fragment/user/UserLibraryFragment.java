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

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.databinding.FragmentUserLibraryBinding;
import com.example.manhinhappmusic.decoration.VerticalLinearSpacingItemDecoration;
import com.example.manhinhappmusic.adapter.PlaylistAdapter;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.repository.PlaylistRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class UserLibraryFragment extends BaseFragment {


    public UserLibraryFragment() {
        // Required empty public constructor
    }

    private FragmentUserLibraryBinding binding;
    private NavController navController;
    private ShapeableImageView userAvatar;
    private ImageButton addButton;
    private ImageButton searchButton;
    private MaterialButton sortButton;
    private RecyclerView playlistsView;
    private PlaylistAdapter playlistAdapter;
    private List<Playlist> playlistList;
    private int currentPlaylistPosition = -1;

    public static UserLibraryFragment newInstance(List<Playlist> playlistList) {
        UserLibraryFragment fragment = new UserLibraryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserLibraryBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("frag", "resume");
        PlaylistRepository.getInstance().getAll().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                playlistAdapter.setPlaylistList(playlists);
                playlistAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        userAvatar = binding.userAvatarImage;
        addButton = binding.addPlaylistButton;
        searchButton = binding.searchPlaylistButton;
        sortButton = binding.sortButton;

        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.userProfileFragment);

            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPlaylistFragment addPlaylistFragment = new AddPlaylistFragment();
                addPlaylistFragment.show(getParentFragmentManager(), null);
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.userLibrarySearchFragment);
            }
        });
        playlistAdapter = new PlaylistAdapter(new ArrayList<>(), new PlaylistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                currentPlaylistPosition = position;
                Playlist playlist = playlistAdapter.getPlaylistList().get(position);
                playlist.setSongsList(new ArrayList<>());
                PlaylistRepository.getInstance().setCurrentPlaylist(playlist);
                navController.navigate(R.id.userPlaylistFragment);
            }
        });



        playlistsView = view.findViewById(R.id.playlists_view);
        playlistsView.setAdapter(playlistAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        playlistsView.setLayoutManager(layoutManager);
        playlistsView.addItemDecoration(new VerticalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        getParentFragmentManager().setFragmentResultListener("request_add_playlist", getViewLifecycleOwner(), (requestKey, result) ->{
            //playlistList.add(new Playlist("dfd", result.getString("playlist_name"), "Fdfd", new ArrayList<>(),"",0,"",new ArrayList<>()));
            String playlistName = result.getString("playlist_name", "null");

            PlaylistRepository.getInstance().getAll().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
                @Override
                public void onChanged(List<Playlist> playlists) {
                    Toast.makeText(getContext(), "Added new playlist", Toast.LENGTH_SHORT).show();
                    playlistAdapter.setPlaylistList(playlists);
                    playlistAdapter.notifyDataSetChanged();
                }
            });

        });

//        getParentFragmentManager().setFragmentResultListener("update_library_when_playlist_got_deleted", getViewLifecycleOwner(), (requestKey, result) ->{
//            if(currentPlaylistPosition != -1)
//            {
//                playlistAdapter.getPlaylistList().remove(currentPlaylistPosition);
//                playlistAdapter.notifyItemRemoved(currentPlaylistPosition);
//
//            }
//        });
//
//        getParentFragmentManager().setFragmentResultListener("update_library_when_playlist_got_modified", getViewLifecycleOwner(), (requestKey, result) ->{
//            if(currentPlaylistPosition != -1)
//                playlistAdapter.notifyItemChanged(currentPlaylistPosition);
//        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }




}