package com.example.manhinhappmusic;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicDisplayFragment extends BaseFragment {


    private TextView titleText;
    private RecyclerView itemView;
    private MusicDisplayItem homeDisplayItem;
    private static final String ARG_ID = "id";

    private String id;
    public static MusicDisplayFragment newInstance(String id) {
        MusicDisplayFragment fragment = new MusicDisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music_display, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleText = view.findViewById(R.id.title_text);
        itemView = view.findViewById(R.id.item_view);


        homeDisplayItem = MusicDisplayRepository.getInstance().getItemById(id).getValue();

        titleText.setText(homeDisplayItem.getTitle());
        MusicDisplayAdapter musicDisplayAdapter = new MusicDisplayAdapter(homeDisplayItem.getItems(), new MusicDisplayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance(null);

                if(homeDisplayItem.getHomeDisplayType() == HomeDisplayType.MIX_PLAYLIST || homeDisplayItem.getHomeDisplayType() == HomeDisplayType.RELEASE_PLAYLIST)
                {
                    Playlist playlist = (Playlist) homeDisplayItem.getItems().get(position);
                    callback.onRequestChangeFragment(FragmentTag.USER_PLAYLIST, playlist.getId());
                }
                else if (homeDisplayItem.getHomeDisplayType() == HomeDisplayType.SONG)
                {
                    Song song = (Song) homeDisplayItem.getItems().get(position);
                    mediaPlayerManager.setPlaylist(new ArrayList<>(Arrays.asList(song)));
                    mediaPlayerManager.setCurrentSong(0);
                    callback.onRequestLoadMiniPlayer();
                    mediaPlayerManager.play();
                }
                else if(homeDisplayItem.getHomeDisplayType() == HomeDisplayType.ARTIST)
                {
                    User artist = (User) homeDisplayItem.getItems().get(position);
                    callback.onRequestChangeFragment(FragmentTag.USER_ARTIST, artist.getId());
                }
            }
        }, homeDisplayItem.getHomeDisplayType());
        itemView.setAdapter(musicDisplayAdapter);

//        if(homeDisplayItem.getHomeDisplayLayout() == HomeDisplayLayout.HORIZONTAL_LINEAR)
//        {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            itemView.setLayoutManager(layoutManager);
            itemView.addItemDecoration(new HorizontalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics())));
//        }
//        else
//        {
//            GridLayoutManager layoutManager = new GridLayoutManager(this.getContext(), 2);
//            itemView.setLayoutManager(layoutManager);
//            itemView.addItemDecoration(new GridSpacingItemDecoration(2, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()), true));
//        }

    }

//    public enum HomeDisplayLayout{
//        HORIZONTAL_LINEAR,
//        GRID;
//    }

    public enum HomeDisplayType
    {
        RELEASE_PLAYLIST,
        MIX_PLAYLIST,
        SONG,
        ARTIST,
    }
}

