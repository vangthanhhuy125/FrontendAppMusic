package com.example.manhinhappmusic.fragment.user;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.repository.PlaylistRepository;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPlaylistFragment extends DialogFragment {

    public AddPlaylistFragment() {
        // Required empty public constructor
    }

    public static AddPlaylistFragment newInstance() {
        AddPlaylistFragment fragment = new AddPlaylistFragment();
        return fragment;
    }


    Button createButton;
    Button cancelButton;
    EditText playlistsNameEditText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_playlist, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        Window window = dialog.getWindow();
        window.setLayout(
                (int)(getResources().getDisplayMetrics().widthPixels * 0.9),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createButton = view.findViewById(R.id.create_button);
        createButton.setOnClickListener(this::onCreateButtonClick);
        cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this::onCancelButtonClick);
        playlistsNameEditText = view.findViewById(R.id.playlists_name_edittext);
    }

    private void onCreateButtonClick(View view)
    {
        Bundle result = new Bundle();
        String playlistName = playlistsNameEditText.getText().toString();
        result.putString("playlist_name", playlistName);
        PlaylistRepository.getInstance().create(playlistName).observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                getParentFragmentManager().setFragmentResult("request_add_playlist", result);
                dismiss();
            }
        });

    }

    private void onCancelButtonClick(View view)
    {
        dismiss();
    }


}