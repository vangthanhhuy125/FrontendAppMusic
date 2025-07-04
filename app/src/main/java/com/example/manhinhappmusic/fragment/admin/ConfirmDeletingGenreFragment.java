package com.example.manhinhappmusic.fragment.admin;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.network.ApiService;
import com.example.manhinhappmusic.network.ApiClient;
import com.example.manhinhappmusic.model.Genre;
import com.example.manhinhappmusic.viewmodel.GenreViewModel;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmDeletingGenreFragment extends DialogFragment {

    private static final String ARG_GENRE = "genre";
    private static final String ARG_POSITION = "position";

    private Genre genre;
    private int position;
    private ApiService apiService;
    private GenreViewModel genreViewModel;

    public interface ConfirmGenreDeleteListener {
        void onConfirmGenreDelete(String genreId, int position);
    }

    private ConfirmGenreDeleteListener listener;

    public void setConfirmGenreDeleteListener(ConfirmGenreDeleteListener listener) {
        this.listener = listener;
    }

    public static ConfirmDeletingGenreFragment newInstance(Genre genre, int position) {
        ConfirmDeletingGenreFragment fragment = new ConfirmDeletingGenreFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GENRE, (Serializable) genre);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_NoActionBar);

        if (getArguments() != null) {
            genre = (Genre) getArguments().getSerializable(ARG_GENRE);
            position = getArguments().getInt(ARG_POSITION);
        }

        apiService = ApiClient.getApiService();
        genreViewModel = new ViewModelProvider(requireActivity()).get(GenreViewModel.class);
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
            if (genre == null) {
                Toast.makeText(getContext(), "Thể loại không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            apiService.deleteGenre(genre.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        genreViewModel.deleteGenre(genre);

                        if (listener != null) {
                            listener.onConfirmGenreDelete(genre.getId(), position);
                        }

                        Toast.makeText(getContext(), "Đã xoá thể loại", Toast.LENGTH_SHORT).show();
                        closeSelf();
                    } else {
                        Toast.makeText(getContext(), "Xoá thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
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
