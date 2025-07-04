package com.example.manhinhappmusic.fragment.admin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.network.ApiService;
import com.example.manhinhappmusic.model.Genre;
import com.example.manhinhappmusic.dto.GenreResponse;
import com.example.manhinhappmusic.network.ApiClient;
import com.example.manhinhappmusic.viewmodel.GenreViewModel;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddGenreFragment extends Fragment {

    private EditText editName, editDescription;
    private ImageView imageView;
    private Button btnSave;
    private Uri selectedImageUri = null;
    private ApiService apiService;
    private GenreViewModel genreViewModel;

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    Glide.with(this).load(selectedImageUri).into(imageView);
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_genre, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editName = view.findViewById(R.id.editName);
        editDescription = view.findViewById(R.id.editDescription);
        imageView = view.findViewById(R.id.imageView);
        btnSave = view.findViewById(R.id.btnSave);

        apiService = ApiClient.getApiService();
        genreViewModel = new ViewModelProvider(requireActivity()).get(GenreViewModel.class);

        imageView.setOnClickListener(v -> pickImage());

        btnSave.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String description = editDescription.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(getContext(), "Tên thể loại không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            uploadGenre(name, description);
        });
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        imagePickerLauncher.launch(Intent.createChooser(intent, "Chọn ảnh thể loại"));
    }

    private void uploadGenre(String name, String description) {
        RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), description);

        MultipartBody.Part thumbnailPart = null;

        if (selectedImageUri != null) {
            try {
                File file = createTempFileFromUri(selectedImageUri);
                RequestBody fileBody = RequestBody.create(MediaType.parse(getMimeType(selectedImageUri)), file);
                thumbnailPart = MultipartBody.Part.createFormData("thumbnail", file.getName(), fileBody);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Lỗi ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        apiService.createGenre(thumbnailPart, nameBody, descBody).enqueue(new Callback<Genre>() {
            @Override
            public void onResponse(@NonNull Call<Genre> call, @NonNull Response<Genre> response) {
                if (response.isSuccessful() && response.body() != null) {
                    genreViewModel.addGenre(response.body());
                    Toast.makeText(getContext(), "Thêm thể loại thành công", Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                } else {
                    Toast.makeText(getContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Genre> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private File createTempFileFromUri(Uri uri) throws Exception {
        File tempFile = File.createTempFile("upload", "." + getFileExtension(uri), requireContext().getCacheDir());
        try (InputStream in = requireContext().getContentResolver().openInputStream(uri);
             OutputStream out = requireContext().getContentResolver().openOutputStream(Uri.fromFile(tempFile))) {
            if (in != null && out != null) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
            }
        }
        return tempFile;
    }

    private String getMimeType(Uri uri) {
        return requireContext().getContentResolver().getType(uri);
    }

    private String getFileExtension(Uri uri) {
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = mime.getExtensionFromMimeType(getMimeType(uri));
        return ext != null ? ext : "jpg";
    }
}
