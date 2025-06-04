package com.example.manhinhappmusic;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserGenreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserGenreFragment extends BaseFragment {


    private static final String ARG_ID = "id";

    private String id;
    private TextView genreTitleText;
    private LinearLayout displayLinearContainer;
    private ImageButton backButton;
    private Genre genre;

    public UserGenreFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static UserGenreFragment newInstance(String id) {
        UserGenreFragment fragment = new UserGenreFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_genre, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        genreTitleText = view.findViewById(R.id.genre_title_text);
        displayLinearContainer = view.findViewById(R.id.display_linear_container);
        backButton = view.findViewById(R.id.back_button);
        genre = GenreRepository.getInstance().getItemById(id).getValue();

        genreTitleText.setText(genre.getName());
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onRequestGoBackPreviousFragment();
            }
        });

        for(MusicDisplayItem musicDisplayItem: MusicDisplayRepository.getInstance().getAll().getValue())
        {
            int viewId = View.generateViewId();
            FrameLayout frameLayout = new FrameLayout(requireContext());
            frameLayout.setId(viewId);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,0,15);
            frameLayout.setLayoutParams(params);
            displayLinearContainer.addView(frameLayout);
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(viewId, MusicDisplayFragment.newInstance(musicDisplayItem.getId()))
                    .commit();
        }

    }
}