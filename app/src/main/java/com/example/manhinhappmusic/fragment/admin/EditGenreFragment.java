package com.example.manhinhappmusic.fragment.admin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.adapter.EditSongAdapter;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.fragment.artist.ConfirmDeletingSongFragment;
import com.example.manhinhappmusic.model.Genre;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.model.User;
import com.example.manhinhappmusic.network.ApiClient;
import com.example.manhinhappmusic.network.ApiService;
import com.example.manhinhappmusic.util.RealPathUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Data;
import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditGenreFragment extends BaseFragment {

    private ImageView imgThumbnail;
    private ImageButton btnEditImage;
    private EditText editName, editDescription;
    private RecyclerView recyclerView;
    private Button btnSave;

    private Genre genre;
    @Setter
    private List<Song> songList = new ArrayList<>();
    private EditSongAdapter songAdapter;

    private String genreId;
    private String genreName;
    private String genreDescription;
    private String genreImageUrl;
    private Uri selectedImageUri;

    private final ApiService apiService = ApiClient.getApiService();
    private static final String ARG_GENRE = "genre";

    public EditGenreFragment() {}

    public static EditGenreFragment newInstance(Genre genre) {
        EditGenreFragment fragment = new EditGenreFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_GENRE, genre);
        fragment.setArguments(args);
        return fragment;
    }

    private final ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    imgThumbnail.setImageURI(selectedImageUri);
                }
            }
    );

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            genre = (Genre) getArguments().getSerializable("genre");
            if (genre != null) {
                genreId = genre.getId(); // ✅ Gán id tại đây
            }
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

        if (genre != null) {
            editName.setText(genre.getName());
            editDescription.setText(genre.getDescription());
            genreId = genre.getId();

            if (genre.getThumbnailUrl() != null && !genre.getThumbnailUrl().isEmpty()) {
                Glide.with(this).load(genre.getThumbnailUrl()).into(imgThumbnail);
            }
        }

        btnEditImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });

        btnSave.setOnClickListener(v -> updateGenreToServer());
    }

    private void updateGenreToServer() {
        String newName = editName.getText().toString().trim();
        String newDescription = editDescription.getText().toString().trim();

        if (newName.isEmpty()) {
            editName.setError("Vui lòng nhập tên thể loại");
            editName.requestFocus();
            return;
        }

        RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), newName);
        RequestBody descPart = RequestBody.create(MediaType.parse("text/plain"), newDescription);

        MultipartBody.Part thumbnailPart = null;
        if (selectedImageUri != null) {
            File file = new File(Objects.requireNonNull(RealPathUtil.getRealPathFromURI(requireContext(), selectedImageUri)));
            RequestBody filePart = RequestBody.create(MediaType.parse("image/*"), file);
            thumbnailPart = MultipartBody.Part.createFormData("thumbnail", file.getName(), filePart);
        }

        Call<Genre> call = apiService.updateGenre(genreId, thumbnailPart, namePart, descPart);
        call.enqueue(new Callback<Genre>() {
            @Override
            public void onResponse(@NonNull Call<Genre> call, @NonNull Response<Genre> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "Cập nhật thể loại thành công", Toast.LENGTH_SHORT).show();
                    if (callback != null) {
                        Bundle result = new Bundle();
                        result.putSerializable("genre_result", response.body());
                        callback.onRequestChangeFragment(FragmentTag.LIST_GENRE, response.body());
                    }
                } else {
                    Log.e("EditGenreFragment", "Lỗi khi cập nhật: " + response.message());
                    Toast.makeText(getContext(), "Lỗi khi cập nhật: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Genre> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
