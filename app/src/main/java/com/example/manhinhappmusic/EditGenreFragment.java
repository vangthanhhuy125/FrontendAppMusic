package com.example.manhinhappmusic;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class EditGenreFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageView imgThumbnail;
    private ImageButton btnEditImage;
    private EditText editName, editDescription;
    private RecyclerView recyclerView;
    private Button btnSave;

    private Genre genre;
    private List<Song> songList = new ArrayList<>();
    private EditSongAdapter songAdapter;

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        imgThumbnail.setImageURI(selectedImageUri);
                        genre.setUrlCoverImage(selectedImageUri.toString());
                    }
                }
            });

    public EditGenreFragment() {}

    public static EditGenreFragment createInstance(String param1, String param2) {
        EditGenreFragment fragment = new EditGenreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_genre, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgThumbnail = view.findViewById(R.id.img_thumbnail);
        btnEditImage = view.findViewById(R.id.btn_edit_image);
        editName = view.findViewById(R.id.edit_name);
        editDescription = view.findViewById(R.id.edit_description);
        btnSave = view.findViewById(R.id.btn_save);
        recyclerView = view.findViewById(R.id.list_song_view);

        editName.setText(genre.getName());
        editDescription.setText(genre.getDescription());

        if (genre.getUrlCoverImage() != null) {
            imgThumbnail.setImageURI(Uri.parse(genre.getUrlCoverImage()));
        } else {
            imgThumbnail.setImageResource(R.drawable.exampleavatar);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        songAdapter = new EditSongAdapter(songList, (song, position) -> {
            ConfirmDeletingSongFragment confirmFragment = ConfirmDeletingSongFragment.newInstance(song, position);

            confirmFragment.setConfirmDeleteListener((songId, pos) -> {
                deleteSongFromDatabase(songId);
                songList.remove(pos);
                songAdapter.notifyItemRemoved(pos);
            });

            confirmFragment.show(getParentFragmentManager(), "CONFIRM_DELETE_SONG");
        });


        recyclerView.setAdapter(songAdapter);

        btnEditImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });


        btnSave.setOnClickListener(v -> {
            String newName = editName.getText().toString().trim();
            String newDescription = editDescription.getText().toString().trim();

            if (newName.isEmpty()) {
                editName.setError("Vui lòng nhập tên thể loại");
                editName.requestFocus();
                return;
            }

            genre.setName(newName);
            genre.setDescription(newDescription);
            // genre.setSongs(songList); // nếu Genre có danh sách bài hát

            Toast.makeText(getContext(), "Đã lưu thay đổi", Toast.LENGTH_SHORT).show();

            if (callback != null) {
                callback.onRequestChangeFragment(FragmentTag.LIST_GENRE, genre);
            }
        });
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public void onSongDeleteConfirmed(int position) {
        if (position >= 0 && position < songList.size()) {
            songList.remove(position);
            songAdapter.notifyItemRemoved(position);
        }
    }

    private void deleteSongFromDatabase(String songId) {

    }


}
