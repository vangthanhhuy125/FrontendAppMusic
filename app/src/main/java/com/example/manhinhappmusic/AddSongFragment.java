package com.example.manhinhappmusic;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddSongFragment extends Fragment {
    private ImageView imgAddImage;
    private Button btnChooseFile, btnSave;
    private EditText edtSongName, edtArtist, edtDescription;
    private Spinner spinnerGenre;
    private ChipGroup chipGroup;

    private SongViewModel songViewModel;

    private Uri selectedImageUri = null;
    private Uri selectedAudioUri = null;

    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ActivityResultLauncher<Intent> audioPickerLauncher;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddSongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddSongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddSongFragment newInstance(String param1, String param2) {
        AddSongFragment fragment = new AddSongFragment();
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
        return inflater.inflate(R.layout.fragment_add_song, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgAddImage = view.findViewById(R.id.imgAddImage);
        btnChooseFile = view.findViewById(R.id.btnChooseFile);
        btnSave = view.findViewById(R.id.btnSave);
        edtSongName = view.findViewById(R.id.edtSongName);
        edtArtist = view.findViewById(R.id.edtArtist);
        edtDescription = view.findViewById(R.id.edtDescription);
        spinnerGenre = view.findViewById(R.id.spinnerGenre);
        chipGroup = view.findViewById(R.id.chipGroup);
        songViewModel = new ViewModelProvider(requireActivity()).get(SongViewModel.class);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        imgAddImage.setImageURI(selectedImageUri);
                    }
                }
        );

        audioPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedAudioUri = result.getData().getData();
                        String fileName = getFileNameFromUri(selectedAudioUri);
                        btnChooseFile.setText(fileName);
                    }
                }
        );


        String[] genres = {"Pop", "Rock", "Jazz", "EDM", "Ballad", "Hip-Hop"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, genres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenre.setAdapter(adapter);

        spinnerGenre.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selectedGenre = genres[position];
                addChipIfNotExists(selectedGenre);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        btnSave.setOnClickListener(v -> {
            String songName = edtSongName.getText().toString().trim();
            String artist = edtArtist.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();

            // TODO: Có thể validate dữ liệu (kiểm tra trống, định dạng,...)

            // Lấy danh sách thể loại từ chipGroup
            List<String> listgenre = new ArrayList<>();
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                listgenre.add(chip.getText().toString());
            }

            Song newSong = new Song(
                    "0",
                    songName,
                    artist,
                    description,
                    selectedAudioUri != null ? selectedAudioUri.toString() : "",
                    selectedImageUri != null ? selectedImageUri.toString() : "",
                    listgenre
            );


            songViewModel.addSong(newSong);

            edtSongName.setText("");
            edtArtist.setText("");
            edtDescription.setText("");
            chipGroup.removeAllViews();
            imgAddImage.setImageResource(R.drawable.exampleavatar);
            btnChooseFile.setText("Choose File");
            selectedAudioUri = null;
            selectedImageUri = null;
        });

        btnChooseFile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("audio/mpeg");
            audioPickerLauncher.launch(intent);
        });

        imgAddImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

    }

    private void addChipIfNotExists(String text) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.getText().toString().equalsIgnoreCase(text)) {
                return;
            }
        }

        Chip chip = new Chip(requireContext());
        chip.setText(text);
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> chipGroup.removeView(chip));
        chipGroup.addView(chip);
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) cursor.close();
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }


}