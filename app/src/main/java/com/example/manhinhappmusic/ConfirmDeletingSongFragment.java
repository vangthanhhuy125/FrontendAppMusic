package com.example.manhinhappmusic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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
        args.putParcelable("song", song);
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

        btnCancel.setOnClickListener(v -> dismiss());

        btnDelete.setOnClickListener(v -> {
            if (listener != null && song != null) {
                listener.onConfirmDelete(song.getId(), position);
            }
            dismiss();
        });

    }
}
