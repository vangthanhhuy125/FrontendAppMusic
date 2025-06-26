package com.example.manhinhappmusic.fragment.user;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.fragment.ConfirmDiscardingChangesFragment;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.network.ApiService;
import com.example.manhinhappmusic.repository.PlaylistRepository;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.decoration.VerticalLinearSpacingItemDecoration;
import com.example.manhinhappmusic.adapter.PlaylistSongEditAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    boolean isModified = false;
    boolean isSongListModified = false;
    boolean isTitleModified = false;
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
        bottomNavVisibility = View.GONE;
        playlist = PlaylistRepository.getInstance().getCurrentPlaylist();

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

        Glide.with(this.getContext())
                .load(ApiService.BASE_URL + playlist.getThumbnailUrl())
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                .into(playlistCoverImage);
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
                    isTitleModified = true;
                }
                else
                {
                    setModified(false);
                    isTitleModified = false;

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
                    isSongListModified = true;

                }
                else
                {
                    setModified(false);
                    isSongListModified = false;

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
                Bundle result = new Bundle();
//                result.putString("playlist_name", playlistNameEditText.getText().toString());
                //playlist.setSongs(playlistSongEditAdapter.getSongList());
                if(isTitleModified)
                {
                    Map<String, Object> changes = new HashMap<>();
                    changes.put("name", playlistNameEditText.getText().toString());
                    playlist.setName(playlistNameEditText.getText().toString());

                    PlaylistRepository.getInstance().edit(playlist.getId(), changes).observe(getViewLifecycleOwner(), new Observer<Playlist>() {
                        @Override
                        public void onChanged(Playlist modifiedPlaylist) {
                        }

                    });
                }


                if(isSongListModified)
                {

                    List<String> deletedSongs = new ArrayList<>();
                    List<Song> original = new ArrayList<>(playlist.getSongsList());
                    for(Song song: original)
                    {
                        boolean isFound = false;
                        for(Song editSong: playlistSongEditAdapter.getSongList())
                        {

                            if(song.getId().equals(editSong.getId()))
                            {
                                isFound = true;
                                break;
                            }

                        }
                        if(!isFound)
                        {
                            playlist.getSongsList().remove(song);
                            deletedSongs.add(song.getId());
                        }

                    }

                    PlaylistRepository.getInstance().removeSongs(playlist.getId(), deletedSongs).observe(getViewLifecycleOwner(), new Observer<Playlist>() {
                        @Override
                        public void onChanged(Playlist playlist) {
                            Toast.makeText(getContext(), "Playlist has been changed", Toast.LENGTH_SHORT).show();
                            getParentFragmentManager().setFragmentResult("update_playlist", result);
                            callback.onRequestGoBackPreviousFragment();
                        }
                    });
                }
                else
                {
                    Toast.makeText(getContext(), "Playlist has been changed", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().setFragmentResult("update_playlist", result);
                    callback.onRequestGoBackPreviousFragment();
                }

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