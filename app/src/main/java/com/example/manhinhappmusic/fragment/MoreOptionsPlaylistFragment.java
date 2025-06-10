package com.example.manhinhappmusic.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.manhinhappmusic.model.Playlist;
import com.example.manhinhappmusic.network.ApiService;
import com.example.manhinhappmusic.repository.PlaylistRepository;
import com.example.manhinhappmusic.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreOptionsPlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreOptionsPlaylistFragment extends BottomSheetDialogFragment {


    private static final String ARG_ID = "id";

    private String id;

    private ImageView playlistCoverImage;
    private TextView playlistTitleText;
    private TextView descriptionText;
    private MaterialButton addButton;
    private MaterialButton editButton;
    private MaterialButton deleteButton;
    private MaterialButton changePublicityButton;
    private Playlist playlist;

    public MoreOptionsPlaylistFragment() {
        // Required empty public constructor
    }


    public static MoreOptionsPlaylistFragment newInstance(String id) {
        MoreOptionsPlaylistFragment fragment = new MoreOptionsPlaylistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
        }
        playlist = PlaylistRepository.getInstance().getCurrentPlaylist();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_options_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playlistCoverImage = view.findViewById(R.id.playlist_cover_image);
        playlistTitleText = view.findViewById(R.id.playlist_title_text);
        descriptionText = view.findViewById(R.id.description_text);
        addButton = view.findViewById(R.id.add_to_playlist_button);
        editButton = view.findViewById(R.id.edit_playlist_button);
        deleteButton = view.findViewById(R.id.delete_playlist_button);
        changePublicityButton = view.findViewById(R.id.change_playlist_publicity_button);

//        Glide.with(this.getContext())
//                .load(ApiService.BASE_URL + playlist.getThumbnailUrl())
//                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
//                .into(playlistCoverImage);

        playlistTitleText.setText(playlist.getName());
        descriptionText.setText(playlist.getUserId() + "•" + (playlist.getPublic() ? "Public":"Private"));
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().setFragmentResult("add_song_to_this_playlist", null);
                dismiss();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().setFragmentResult("edit_playlist", null);
                dismiss();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().setFragmentResult("delete_playlist", null);
                dismiss();
            }
        });
        changePublicityButton.setText(playlist.getPublic() ? "Set to private" : "Set to public");
        changePublicityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlist.setPublic(!playlist.getPublic());
                dismiss();
            }
        });
    }
}