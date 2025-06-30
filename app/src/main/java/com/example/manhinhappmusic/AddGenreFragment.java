package com.example.manhinhappmusic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.UUID;

public class AddGenreFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private EditText editName, editDescription;
    private ImageView imageView;
    private Button btnSave;
    private String selectedImagePath = "";

    private Uri selectedImageUri = null;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    private GenreViewModel genreViewModel;

    public AddGenreFragment() {

    }

    public static AddGenreFragment newInstance(String param1, String param2) {
        AddGenreFragment fragment = new AddGenreFragment();
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
        return inflater.inflate(R.layout.fragment_add_genre, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editName = view.findViewById(R.id.editName);
        editDescription = view.findViewById(R.id.editDescription);
        imageView = view.findViewById(R.id.imageView);
        btnSave = view.findViewById(R.id.btnSave);

        genreViewModel = new ViewModelProvider(requireActivity()).get(GenreViewModel.class);

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            imageView.setImageURI(selectedImageUri);
                            selectedImagePath = selectedImageUri.toString();
                        }
                    }
                }
        );

        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });

        btnSave.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String description = editDescription.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(getContext(), "Tên thể loại không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            String id = UUID.randomUUID().toString();
            Genre genre = new Genre(id, name, description, selectedImagePath);

            genreViewModel.addGenre(genre);

            Toast.makeText(getContext(), "Đã tạo thể loại mới: " + genre.getName(), Toast.LENGTH_SHORT).show();

            if (callback != null) {
                callback.onRequestChangeFragment(FragmentTag.LIST_GENRE);
            }
        });
    }
}
