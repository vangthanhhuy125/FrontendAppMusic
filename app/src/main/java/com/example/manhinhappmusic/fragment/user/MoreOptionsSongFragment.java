package com.example.manhinhappmusic.fragment.user;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.repository.PlaylistRepository;
import com.example.manhinhappmusic.repository.SongRepository;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreOptionsSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreOptionsSongFragment extends BottomSheetDialogFragment {


    private static final String ARG_SONG_ID = "song_id";

    private String songId;

    private ImageView songCoverImage;
    private TextView songTitleText;
    private TextView artistNameText;
    private MaterialButton addButton;
    private MaterialButton removeButton;

    private Song song;

    public static MoreOptionsSongFragment newInstance(String songId) {
        MoreOptionsSongFragment fragment = new MoreOptionsSongFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SONG_ID, songId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            songId = getArguments().getString(ARG_SONG_ID);
        }
        song = SongRepository.getInstance().getCurrentSong();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_options_song, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        songCoverImage = view.findViewById(R.id.song_cover_image);
        songTitleText = view.findViewById(R.id.song_title_text);
        artistNameText = view.findViewById(R.id.artist_name_text);
        addButton = view.findViewById(R.id.add_to_other_playlist_button);
        removeButton = view.findViewById(R.id.remove_from_this_playlist_button);
        songTitleText.setText(song.getTitle());
        artistNameText.setText(song.getArtistId());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().setFragmentResult("add_song_to_other_playlist", null);
                dismiss();
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaylistRepository.getInstance().removeSongs(PlaylistRepository.getInstance().getCurrentPlaylist().getId()
                        , new ArrayList<>(Arrays.asList(songId))).observe(getViewLifecycleOwner(), new Observer<Playlist>() {
                    @Override
                    public void onChanged(Playlist playlist) {
                        getParentFragmentManager().setFragmentResult("remove_song_from_this_playlist", null);
                        dismiss();

                    }
                });

            }
        });

    }
}