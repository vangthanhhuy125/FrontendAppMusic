package com.example.manhinhappmusic.fragment.artist;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.viewmodel.SongViewModel;

public class ConfirmDeletingSongFragment extends DialogFragment {

    private static final String ARG_SONG_ID = "song_id";
    private static final String ARG_POSITION = "position";

    private String songId;
    private int position;

    // Interface callback để thông báo xóa
    public interface ConfirmDeleteListener {
        void onConfirmDelete(String songId, int position);
    }

    private ConfirmDeleteListener listener;

    public void setConfirmDeleteListener(ConfirmDeleteListener listener) {
        this.listener = listener;
    }

    public ConfirmDeletingSongFragment() {

    }

    public static ConfirmDeletingSongFragment newInstance(Song song, int position) {
        ConfirmDeletingSongFragment fragment = new ConfirmDeletingSongFragment();
        Bundle args = new Bundle();
        args.putParcelable("song", (Parcelable) song);
        args.putInt(ARG_POSITION, position); // thêm dòng này
        fragment.setArguments(args);
        return fragment;
    }


    private Song song;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            song = getArguments().getParcelable("song");
            position = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_confirm_deleting_song, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnCancel = view.findViewById(R.id.cancel_button);
        Button btnDelete = view.findViewById(R.id.delete_button);

        btnCancel.setOnClickListener(v -> {
            closeSelf();
        });

        btnDelete.setOnClickListener(v -> {
            SongViewModel viewModel = new ViewModelProvider(requireActivity()).get(SongViewModel.class);
            viewModel.deleteSong(song);

            if (listener != null) {
                listener.onConfirmDelete(song.getId(), position);
            }

            closeSelf();
        });
    }

    private void closeSelf() {
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .remove(this)
                .commit();

        View container = requireActivity().findViewById(R.id.dialog_container);
        if (container != null) container.setVisibility(View.GONE);
    }

}