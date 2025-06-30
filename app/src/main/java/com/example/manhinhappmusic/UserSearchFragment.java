package com.example.manhinhappmusic;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserSearchFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserSearchFragment.
     */
    // TODO: Rename and change types and number of parameters

    private ImageView userAvatar;
    private AppCompatButton searchButton;
    private RecyclerView browseView;
    private BrowseAdapter browseAdapter;
    private List<Genre> genreList;

    private RecyclerView artistView;
    private ArtistAdapter artistAdapter;
    private List<User> artistList;

    public static UserSearchFragment newInstance(String param1, String param2) {
        UserSearchFragment fragment = new UserSearchFragment();
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
        return inflater.inflate(R.layout.fragment_user_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userAvatar = view.findViewById(R.id.user_avatar_image);
        userAvatar.setOnClickListener(this::onUserAvatarClick);

        searchButton = view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(this::onSearchButtonClick);

        genreList = TestData.genreList;

        browseAdapter = new BrowseAdapter(genreList, new BrowseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        browseView = view.findViewById(R.id.browse_view);
        browseView.setAdapter(browseAdapter);
        GridLayoutManager browseLayoutManager = new GridLayoutManager(this.getContext(), 2);
        browseView.setLayoutManager(browseLayoutManager);
        browseView.addItemDecoration(new GridSpacingItemDecoration(2, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()), true));

        artistList = TestData.artistList;

        artistAdapter = new ArtistAdapter(artistList, new ArtistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        artistView = view.findViewById(R.id.artist_view);
        artistView.setAdapter(artistAdapter);
        LinearLayoutManager artistLayoutManager = new LinearLayoutManager(this.getContext());
        artistLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        artistView.addItemDecoration(new HorizontalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics())));
        artistView.setLayoutManager(artistLayoutManager);
    }

    private void onUserAvatarClick(View view){
        callback.onRequestChangeFragment(FragmentTag.USER_PROFILE);
    }

    private void onSearchButtonClick(View view){
        callback.onRequestChangeFragment(FragmentTag.SEARCH_EX);
    }
}