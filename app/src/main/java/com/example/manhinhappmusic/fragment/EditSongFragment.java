package com.example.manhinhappmusic.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.model.Song;

public class EditSongFragment extends Fragment {

    // Callback interface để trả kết quả sau khi chỉnh sửa
    public interface OnSongEditedListener {
        void onSongEdited(Song updatedSong);
    }

    private OnSongEditedListener listener;

    // Views
    private ImageView songImage;
    private ImageButton editImageButton;
    private EditText songNameInput, artistInput, descriptionInput;
    private Button saveButton;

    private Song currentSong;

    // Nếu muốn mở chọn ảnh, cần ActivityResultLauncher (cái này có thể mở rộng)
    private ActivityResultLauncher<String> pickImageLauncher;

    public EditSongFragment() {
        // Required empty public constructor
    }

    // Factory method để truyền Song vào fragment
//    public static EditSongFragment newInstance(Song song) {
//        EditSongFragment fragment = new EditSongFragment();
//        Bundle args = new Bundle();
//        args.putSerializable("song", song);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public void setOnSongEditedListener(OnSongEditedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // Lấy Song được truyền vào
            currentSong = (Song) getArguments().getSerializable("song");
        }

        // Khởi tạo launcher nếu muốn chọn ảnh (nếu bạn muốn mở rộng chức năng chọn ảnh)
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        songImage.setImageURI(uri);
                        // Cập nhật đường dẫn ảnh cho Song, bạn cần lưu ý xử lý thật sự nếu cần upload, lưu file,...
                        currentSong.setCoverImageUrl(uri.toString());
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_song, container, false);

        songImage = view.findViewById(R.id.songImage);
        editImageButton = view.findViewById(R.id.editImageButton);
        songNameInput = view.findViewById(R.id.songNameInput);
        artistInput = view.findViewById(R.id.artistInput);
        descriptionInput = view.findViewById(R.id.descriptionInput);
        saveButton = view.findViewById(R.id.saveButton);

//        if (currentSong != null) {
//            if (currentSong.getCoverImageResID() != 0) {
//                songImage.setImageResource(currentSong.getCoverImageResID());
//            } else if (currentSong.getCoverImageUrl() != null) {
//                songImage.setImageURI(Uri.parse(currentSong.getCoverImageUrl()));
//            }
//            songNameInput.setText(currentSong.getTitle());
//            artistInput.setText(currentSong.getArtistId());
//            descriptionInput.setText(currentSong.getDescription());
//        }

        editImageButton.setOnClickListener(v -> {
            pickImageLauncher.launch("image/*");
        });

        saveButton.setOnClickListener(v -> {
            String newTitle = songNameInput.getText().toString().trim();
            String newArtist = artistInput.getText().toString().trim();
            String newDesc = descriptionInput.getText().toString().trim();

            if (newTitle.isEmpty()) {
                Toast.makeText(getContext(), "Please enter song name", Toast.LENGTH_SHORT).show();
                return;
            }

            currentSong.setTitle(newTitle);
//            currentSong.setArtistId(newArtist);
            currentSong.setDescription(newDesc);

            if (listener != null) {
                listener.onSongEdited(currentSong);
            }

            Toast.makeText(getContext(), "Song saved", Toast.LENGTH_SHORT).show();

            getParentFragmentManager().popBackStack();
        });

        return view;
    }
}
