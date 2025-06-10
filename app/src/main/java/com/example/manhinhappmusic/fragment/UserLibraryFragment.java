package com.example.manhinhappmusic.fragment;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import com.example.manhinhappmusic.decoration.VerticalLinearSpacingItemDecoration;
import com.example.manhinhappmusic.adapter.PlaylistAdapter;
import com.example.manhinhappmusic.dto.PlaylistRequest;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.network.ApiClient;
import com.example.manhinhappmusic.repository.LibraryRepository;
import com.example.manhinhappmusic.repository.PlaylistRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserLibraryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserLibraryFragment extends BaseFragment {


    public UserLibraryFragment() {
        // Required empty public constructor
    }

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
        return inflater.inflate(R.layout.fragment_user_library, container, false);
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
        userAvatar = view.findViewById(R.id.user_avatar_image);
        userAvatar.setOnClickListener(this::onUserAvatarClick);
        addButton = view.findViewById(R.id.add_playlist_button);
        addButton.setOnClickListener(this::onAddPlaylistButtonClick);
        searchButton = view.findViewById(R.id.search_playlist_button);
        sortButton = view.findViewById(R.id.sort_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRequestChangeFragment(FragmentTag.LIBRARY_SEARCH);
            }
        });
        playlistAdapter = new PlaylistAdapter(new ArrayList<>(), new PlaylistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                currentPlaylistPosition = position;
                Playlist playlist = playlistAdapter.getPlaylistList().get(position);
                playlist.setSongsList(new ArrayList<>());
                PlaylistRepository.getInstance().setCurrentPlaylist(playlist);
                callback.onRequestChangeFragment(FragmentTag.USER_PLAYLIST,playlist.getId());

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

    private void onUserAvatarClick(View view){
        callback.onRequestChangeFragment(FragmentTag.USER_PROFILE);
    }

    private void onAddPlaylistButtonClick(View view){

        AddPlaylistFragment addPlaylistFragment = new AddPlaylistFragment();
        addPlaylistFragment.show(getParentFragmentManager(), null);


    }



}