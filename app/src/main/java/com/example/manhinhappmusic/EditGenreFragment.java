package com.example.manhinhappmusic;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditGenreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditGenreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditGenreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditGenreFragment.
     */
    // TODO: Rename and change types and number of parameters

    private ImageView imgThumbnail;
    private ImageButton btnEditImage, btnAddSong;
    private EditText editName, editDescription;
    private RecyclerView recyclerView;
    private Button btnSave;

    private Genre genre;
    private List<Playlist> playlistList = new ArrayList<>();
    private PlaylistAdapter playlistAdapter;

//    private ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
//                    Uri selectedImageUri = result.getData().getData();
//                    if (selectedImageUri != null) {
//                        imgThumbnail.setImageURI(selectedImageUri);
//                        // Lưu URI ảnh vào genre tạm thời
//                        genre.setUrlCoverImage(selectedImageUri.toString());
//                    }
//                }
//            });

    public static EditGenreFragment newInstance(String param1, String param2) {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_genre, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgThumbnail = view.findViewById(R.id.img_thumbnail);
        btnEditImage = view.findViewById(R.id.btn_edit_image);
        btnAddSong = view.findViewById(R.id.btn_add_song);
        editName = view.findViewById(R.id.edit_name);
        editDescription = view.findViewById(R.id.edit_description);
        btnSave = view.findViewById(R.id.btn_save);
        recyclerView = view.findViewById(R.id.playlist_recycler_view);


//        if (genre == null) {
//            genre = new Genre("g1", "Pop", "Thể loại nhạc Pop", null);
//        }
//        if (playlistList.isEmpty()) {
//            playlistList.add(new Playlist("p1", "Top Hits 2024", "Những bài hát hay nhất 2024", null, "user1", R.drawable.exampleavatar));
//            playlistList.add(new Playlist("p2", "Relaxing Music", "Nhạc thư giãn", null, "user1", R.drawable.exampleavatar));
//        }

        editName.setText(genre.getName());
        editDescription.setText(genre.getDescription());
        if (genre.getUrlCoverImage() != null) {
            imgThumbnail.setImageURI(Uri.parse(genre.getUrlCoverImage()));
        } else {
            imgThumbnail.setImageResource(R.drawable.exampleavatar);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        playlistAdapter = new PlaylistAdapter(playlistList, position -> {
            Toast.makeText(getContext(), "Clicked playlist: " + playlistList.get(position).getName(), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(playlistAdapter);

        btnEditImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //pickImageLauncher.launch(intent);
        });

        btnAddSong.setOnClickListener(v -> {
            Playlist newPlaylist = new Playlist(
                    "p" + (playlistList.size() + 1),
                    "New Playlist " + (playlistList.size() + 1),
                    "Mô tả mới",
                    new ArrayList<>(),
                    "user1",
                    R.drawable.exampleavatar,"", null);
            playlistList.add(newPlaylist);
            playlistAdapter.notifyItemInserted(playlistList.size() - 1);
        });

        btnSave.setOnClickListener(v -> {
            String newName = editName.getText().toString().trim();
            String newDescription = editDescription.getText().toString().trim();

            if (newName.isEmpty()) {
                editName.setError("");
                editName.requestFocus();
                return;
            }

            genre.setName(newName);
            genre.setDescription(newDescription);

            // DB

            Toast.makeText(getContext(), "Saved changes", Toast.LENGTH_SHORT).show();
        });
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setPlaylistList(List<Playlist> playlistList) {
        this.playlistList = playlistList;
    }

}