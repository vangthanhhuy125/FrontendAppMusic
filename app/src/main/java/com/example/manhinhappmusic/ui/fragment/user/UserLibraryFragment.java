package com.example.manhinhappmusic.ui.fragment.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.ui.fragment.BaseFragment;
import com.example.manhinhappmusic.ui.fragment.playlist.AddPlaylistFragment;
import com.google.android.material.imageview.ShapeableImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserLibraryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserLibraryFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserLibraryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserLibraryFragment.
     */
    // TODO: Rename and change types and number of parameters
    private ShapeableImageView userAvatar;
    private ImageButton addButton;
    public static UserLibraryFragment newInstance(String param1, String param2) {
        UserLibraryFragment fragment = new UserLibraryFragment();
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
        return inflater.inflate(R.layout.fragment_user_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userAvatar = view.findViewById(R.id.user_avatar_image);
        userAvatar.setOnClickListener(this::onUserAvatarClick);
        addButton = view.findViewById(R.id.add_playlist_button);
        addButton.setOnClickListener(this::onAddPlaylistButtonClick);

    }

    private void onUserAvatarClick(View view){
        callback.onRequestChangeFragment(FragmentTag.valueOf("UserProfile"));
    }

    private void onAddPlaylistButtonClick(View view){

        AddPlaylistFragment addPlaylistFragment = new AddPlaylistFragment();
        addPlaylistFragment.show(getParentFragmentManager(), null);


    }
}