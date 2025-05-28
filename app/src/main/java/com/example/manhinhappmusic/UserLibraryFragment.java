package com.example.manhinhappmusic;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserLibraryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserLibraryFragment extends BaseFragment {


    public UserLibraryFragment() {
        // Required empty public constructor
    }

    public UserLibraryFragment(List<Playlist> playlistList)
    {
        this.playlistList = playlistList;
    }

    private ShapeableImageView userAvatar;
    private ImageButton addButton;
    private RecyclerView playlistsView;
    private PlaylistAdapter playlistAdapter;
    private List<Playlist> playlistList;
    private UserLibraryViewModel viewModel;

    public static UserLibraryFragment newInstance(List<Playlist> playlistList) {
        UserLibraryFragment fragment = new UserLibraryFragment();
        fragment.setPlaylistList(playlistList);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(UserLibraryViewModel.class);
        if(viewModel.getPlaylistList() == null && playlistList != null)
        {
            viewModel.setPlaylistList(playlistList);
        }
        else {
            playlistList = viewModel.getPlaylistList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userAvatar = view.findViewById(R.id.user_avatar_image);
        userAvatar.setOnClickListener(this::onUserAvatarClick);
        addButton = view.findViewById(R.id.add_playlist_button);
        addButton.setOnClickListener(this::onAddPlaylistButtonClick);

        playlistAdapter = new PlaylistAdapter(playlistList, new PlaylistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                callback.onRequestChangeFragment(FragmentTag.USER_PLAYLIST, playlistAdapter.getPlaylistList().get(position));
            }
        });
        playlistsView = view.findViewById(R.id.playlists_view);
        playlistsView.setAdapter(playlistAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        playlistsView.setLayoutManager(layoutManager);
        playlistsView.addItemDecoration(new VerticalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));



    }

    private void onUserAvatarClick(View view){
        callback.onRequestChangeFragment(FragmentTag.USER_PROFILE);
    }

    private void onAddPlaylistButtonClick(View view){

        AddPlaylistFragment addPlaylistFragment = new AddPlaylistFragment();
        addPlaylistFragment.setOnCreateButtonClickListener(new AddPlaylistFragment.OnCreateButtonClickListener() {
            @Override
            public void onCreateButtonClick(String playlistsName) {
                playlistList.add(new Playlist("dfd", playlistsName, "Fdfd", new ArrayList<>(),"",0));
                playlistAdapter.notifyDataSetChanged();
            }
        });
        addPlaylistFragment.show(getParentFragmentManager(), null);


    }

    public void setPlaylistList(List<Playlist> playlistList) {
        this.playlistList = playlistList;
    }
}