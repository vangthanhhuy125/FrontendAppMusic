package com.example.manhinhappmusic;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditPlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditPlaylistFragment extends BaseFragment {

    private ImageButton cancelButton;
    private Button saveButton;
    private ImageView playlistCoverImage;
    private Button changePlaylistCoverButton;
    private EditText playlistNameEditText;
    private RecyclerView songsView;
    private Playlist playlist;
    PlaylistSongEditAdapter playlistSongEditAdapter;
    boolean isModified;
    private static final String ARG_ID = "id";
    private static final String ARG_IS_MODIFIED = "is_modified";



    private String id;

    public EditPlaylistFragment() {
        // Required empty public constructor
    }


    public static EditPlaylistFragment newInstance(String id) {
        EditPlaylistFragment fragment = new EditPlaylistFragment();
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
            isModified = getArguments().getBoolean(ARG_IS_MODIFIED);
        }
        playlist = PlaylistRepository.getInstance().getItemById(id).getValue();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_IS_MODIFIED, isModified);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cancelButton = view.findViewById(R.id.cancel_button);
        saveButton = view.findViewById(R.id.save_button);
        playlistCoverImage = view.findViewById(R.id.playlist_cover_image);
        changePlaylistCoverButton = view.findViewById(R.id.change_cover_button);
        playlistNameEditText = view.findViewById(R.id.playlist_name_edittext);
        songsView = view.findViewById(R.id.songs_view);

        playlistCoverImage.setImageResource(playlist.getThumnailResID());
        changePlaylistCoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        playlistNameEditText.setText(playlist.getName());
        playlistNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(playlist.getName()))
                {
                    setModified(true);
                }
                else
                {
                    setModified(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        List<Song> songList = new ArrayList<>(playlist.getSongsList());
        playlistSongEditAdapter = new PlaylistSongEditAdapter(songList, new PlaylistSongEditAdapter.OnItemChangeListener() {
            @Override
            public void onItemChange() {

                if(!playlist.getSongsList().equals(playlistSongEditAdapter.getSongList()))
                {
                    setModified(true);
                }
                else
                {
                    setModified(false);
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        songsView.setAdapter(playlistSongEditAdapter);
        playlistSongEditAdapter.itemTouchHelper.attachToRecyclerView(songsView);
        songsView.setLayoutManager(linearLayoutManager);
        songsView.addItemDecoration(new VerticalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics())));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isModified)
                {
                    getParentFragmentManager().setFragmentResultListener("discard_changes", getViewLifecycleOwner(), (requestKey, result) -> {
                        callback.onRequestGoBackPreviousFragment();
                    });
                    ConfirmDiscardingChangesFragment confirmDiscardingChangesFragment = new ConfirmDiscardingChangesFragment();
                    confirmDiscardingChangesFragment.show(getParentFragmentManager(), "");
                }
                else
                    callback.onRequestGoBackPreviousFragment();


            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlist.setName(playlistNameEditText.getText().toString());
                playlist.setSongs(playlistSongEditAdapter.getSongList());
                getParentFragmentManager().setFragmentResult("update_playlist", null);
                callback.onRequestGoBackPreviousFragment();
            }
        });
    }

    private void setModified(boolean modified) {
        isModified = modified;
        if(isModified)
        {
            saveButton.setClickable(true);
            saveButton.setTextColor(Color.WHITE);
        }
        else
        {
            saveButton.setClickable(false);
            saveButton.setTextColor(Color.parseColor("#AAAAAA"));
        }
    }
}