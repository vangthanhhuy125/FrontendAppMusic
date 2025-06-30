package com.example.manhinhappmusic;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserHomeFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserHomeFragment() {
        // Required empty public constructor
        recentlyPlaySongList = TestData.songList;
        newReleaseSongList = TestData.songList;
        playlistList = TestData.playlistList;
        featurePlaylistList = TestData.playlistList;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserHomeFragment newInstance(String param1, String param2) {
        UserHomeFragment fragment = new UserHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private ShapeableImageView userAvatarImage;
    private RecyclerView recentlyPlayView;
    private RecyclerView newReleaseView;
    private RecyclerView playlistView;
    private RecyclerView featureView;

    private List<Playlist> playlistList;
    private List<Song> newReleaseSongList;
    private List<Song> recentlyPlaySongList;
    private List<Playlist> featurePlaylistList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        userAvatarImage = view.findViewById(R.id.user_avatar_image);
        userAvatarImage.setOnClickListener(this::onClickUserAvatarImage);

        playlistView = view.findViewById(R.id.playlist_view);
        GridLayoutManager playlistLayoutManager = new GridLayoutManager(this.getContext(), 2);
        HomePlaylistAdapter playlistAdapter = new HomePlaylistAdapter(playlistList, new HomePlaylistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        playlistView.setAdapter(playlistAdapter);
        playlistView.setLayoutManager(playlistLayoutManager);
        playlistView.addItemDecoration(new GridSpacingItemDecoration(2, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()), true));

        recentlyPlayView = view.findViewById(R.id.recently_play_view);
        LinearLayoutManager recentlyPlayViewLayoutManager = new LinearLayoutManager(this.getContext());
        recentlyPlayViewLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        HomeSongAdapter recentlySongAdapter = new HomeSongAdapter(recentlyPlaySongList, new HomeSongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        recentlyPlayView.setAdapter(recentlySongAdapter);
        recentlyPlayView.setLayoutManager(recentlyPlayViewLayoutManager);
        recentlyPlayView.addItemDecoration(new HorizontalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));

        newReleaseView = view.findViewById(R.id.new_release_view);
        LinearLayoutManager newReleaseViewLayoutManager = new LinearLayoutManager(this.getContext());
        newReleaseViewLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        HomeSongAdapter newReleaseSongAdapter = new HomeSongAdapter(newReleaseSongList, new HomeSongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        newReleaseView.setAdapter(newReleaseSongAdapter);
        newReleaseView.setLayoutManager(newReleaseViewLayoutManager);
        newReleaseView.addItemDecoration(new HorizontalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));

        featureView = view.findViewById(R.id.featuring_view);
        LinearLayoutManager featureLayoutManager = new LinearLayoutManager(this.getContext());
        featureLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        HomeFeatureAdapter homeFeatureAdapter = new HomeFeatureAdapter(featurePlaylistList, new HomeFeatureAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        featureView.setAdapter(homeFeatureAdapter);
        featureView.setLayoutManager(featureLayoutManager);
        featureView.addItemDecoration(new HorizontalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));

    }



    private void onClickUserAvatarImage(View view){
        try {
            callback.onRequestChangeFragment(FragmentTag.USER_PROFILE, null);
        }catch (Exception ex)
        {
            Toast.makeText(this.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }
}