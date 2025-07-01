package com.example.manhinhappmusic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

public class ConfirmDeletingGenreFragment extends DialogFragment {

    private static final String ARG_GENRE = "genre";
    private static final String ARG_POSITION = "position";

    private Genre genre;
    private int position;

    public interface ConfirmGenreDeleteListener {
        void onConfirmGenreDelete(String genreId, int position);
    }

    private ConfirmGenreDeleteListener listener;

    public void setConfirmGenreDeleteListener(ConfirmGenreDeleteListener listener) {
        this.listener = listener;
    }

    public ConfirmDeletingGenreFragment() {
        // constructor mặc định
    }

    public static ConfirmDeletingGenreFragment newInstance(Genre genre, int position) {
        ConfirmDeletingGenreFragment fragment = new ConfirmDeletingGenreFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_GENRE, genre);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_NoActionBar);

        if (getArguments() != null) {
            genre = getArguments().getParcelable(ARG_GENRE);
            position = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_confirm_deleting_genre, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button cancelButton = view.findViewById(R.id.cancel_button);
        Button deleteButton = view.findViewById(R.id.create_button);

        cancelButton.setOnClickListener(v -> closeSelf());

        deleteButton.setOnClickListener(v -> {
            GenreViewModel viewModel = new ViewModelProvider(requireActivity()).get(GenreViewModel.class);
            viewModel.deleteGenre(genre);

            if (listener != null) {
                listener.onConfirmGenreDelete(genre.getId(), position);
            }

            closeSelf();
        });
    }

    private void closeSelf() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(this)
                .commit();

        View container = requireActivity().findViewById(R.id.dialog_container);
        if (container != null) container.setVisibility(View.GONE);
    }
}
