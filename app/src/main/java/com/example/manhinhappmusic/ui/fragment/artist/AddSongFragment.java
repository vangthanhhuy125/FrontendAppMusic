package com.example.manhinhappmusic.ui.fragment.artist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.manhinhappmusic.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddSongFragment extends Fragment {
    private ImageView imgAddImage;
    private Button btnChooseVoice, btnChooseFile, btnSave;
    private EditText edtSongName, edtArtist, edtDescription;

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
        btnChooseVoice = view.findViewById(R.id.btnChooseVoice);
        btnChooseFile = view.findViewById(R.id.btnChooseFile);
        btnSave = view.findViewById(R.id.btnSave);
        edtSongName = view.findViewById(R.id.edtSongName);
        edtArtist = view.findViewById(R.id.edtArtist);
        edtDescription = view.findViewById(R.id.edtDescription);

        btnSave.setOnClickListener(v -> {
            String songName = edtSongName.getText().toString().trim();
            String artist = edtArtist.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();

            // TODO: validate dữ liệu và lưu bài hát
        });

        btnChooseVoice.setOnClickListener(v -> {
            // file voice
        });

        btnChooseFile.setOnClickListener(v -> {
            // file khác
        });

        imgAddImage.setOnClickListener(v -> {
            // chọn ảnh
        });
    }
}