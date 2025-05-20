package com.example.manhinhappmusic;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

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
    private RecyclerView songView;
    private LibraryAdapter songAdapter;
    private List<Playlist> songItems;
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

        List<String> songs1 = new ArrayList<>();
        songs1.add("ABC");
        songs1.add("DEF");
        songs1.add("EEEE");
        songs1.add("123456789");
        songs1.add("HHH");
        List<String> songs2 = new ArrayList<>();
        songs2.add("XXXXX");
        songs2.add("ZZZZ");

        songItems = new ArrayList<>();
        songItems.add(new Playlist("1","Cai luong", "xxx", songs1, "1", "url"));
        songItems.add(new Playlist("2","Tan co", "xxx", songs2, "1", "url"));
        songItems.add(new Playlist("3","Son Tung MTP", "xxx", songs2, "1", "url"));
        songItems.add(new Playlist("4","Nhac Remix", "xxx", songs1, "1", "url"));
        songItems.add(new Playlist("5","Nhac Thieu Nhi", "xxx", songs1, "1", "url"));

        songAdapter = new LibraryAdapter(songItems);
        songView = view.findViewById(R.id.playlist_list);
        songView.setAdapter(songAdapter);
        Context context = getContext();
        if (context != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            songView.setLayoutManager(layoutManager);
        }


    }

    private void onUserAvatarClick(View view){
        callback.onRequestChangeFragment("UserProfile");
    }

    private void onAddPlaylistButtonClick(View view){

        AddPlaylistFragment addPlaylistFragment = new AddPlaylistFragment();
        addPlaylistFragment.show(getParentFragmentManager(), null);


    }
}