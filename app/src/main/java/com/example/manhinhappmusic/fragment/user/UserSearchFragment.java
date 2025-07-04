package com.example.manhinhappmusic.fragment.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.TestData;
import com.example.manhinhappmusic.databinding.FragmentUserSearchBinding;
import com.example.manhinhappmusic.decoration.AppItemDecoration;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.model.User;
import com.example.manhinhappmusic.adapter.ArtistAdapter;
import com.example.manhinhappmusic.adapter.GenreAdapter;
import com.example.manhinhappmusic.decoration.GridSpacingItemDecoration;
import com.example.manhinhappmusic.decoration.HorizontalLinearSpacingItemDecoration;
import com.example.manhinhappmusic.model.Genre;
import com.example.manhinhappmusic.network.ApiService;
import com.example.manhinhappmusic.repository.ArtistRepository;
import com.example.manhinhappmusic.repository.GenreRepository;
import com.example.manhinhappmusic.repository.GlobalVars;

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
    private static final String ARG_USER_AVATAR_URL = "userAvatarUrl";

    private NavController navController;
    private FragmentUserSearchBinding binding;
    private ImageView userAvatar;
    private AppCompatButton searchButton;
    private RecyclerView browseView;
    private GenreAdapter genreAdapter;
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
    public void onResume() {
        super.onResume();
        String userAvatarUrl = GlobalVars.getVars().get(ARG_USER_AVATAR_URL);
        if(userAvatarUrl != null)
            Glide.with(getContext())
                    .load(ApiService.BASE_URL + userAvatarUrl)
                    .circleCrop()
                    .into(userAvatar);

        ArtistRepository.getInstance().getTrendingArtist().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                artistAdapter.setArtistList(users);
                artistAdapter.notifyDataSetChanged();
            }
        });
        GenreRepository.getInstance().getAllGenres().observe(getViewLifecycleOwner(), new Observer<List<Genre>>() {
            @Override
            public void onChanged(List<Genre> genres) {
                genreAdapter.setGenreList(genres);
                callback.setIsProcessing(false);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserSearchBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        userAvatar = binding.userAvatarImage;
        userAvatar.setOnClickListener(this::onUserAvatarClick);

        searchButton = binding.searchButton;
        searchButton.setOnClickListener(this::onSearchButtonClick);
        Glide.with(getContext())
                .load(R.drawable.person_default_cover)
                .circleCrop()
                .into(userAvatar);

        genreAdapter = new GenreAdapter(new ArrayList<>(), new GenreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Genre item) {
                Bundle bundle = new Bundle();
                bundle.putString("genreId", item.getId());
                bundle.putString("genreName", item.getName());
                navController.navigate(R.id.userGenreFragment, bundle);
            }
        });
        browseView = view.findViewById(R.id.browse_view);
        browseView.setAdapter(genreAdapter);
        GridLayoutManager browseLayoutManager = new GridLayoutManager(this.getContext(), 2);
        browseView.setLayoutManager(browseLayoutManager);
        browseView.addItemDecoration(new GridSpacingItemDecoration(2, AppItemDecoration.convertDpToPx(10, getResources()), true));


        artistAdapter = new ArtistAdapter(new ArrayList<>(), new ArtistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, User artist) {
                Bundle bundle = new Bundle();
                bundle.putString("artistId", artist.getId());
                bundle.putString("artistName", artist.getFullName());
                bundle.putString("artistImageUrl", artist.getAvatarUrl());

                navController.navigate(R.id.userArtistFragment, bundle);
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
        navController.navigate(R.id.userProfileFragment);
    }

    private void onSearchButtonClick(View view){
        navController.navigate(R.id.seacrhExFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}