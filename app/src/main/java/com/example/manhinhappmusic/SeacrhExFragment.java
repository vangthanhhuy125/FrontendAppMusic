package com.example.manhinhappmusic;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeacrhExFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeacrhExFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SeacrhExFragment() {
        // Required empty public constructor
        searchResultList = new ArrayList<>();
        searchResultList.addAll(TestData.artistList);
        searchResultList.addAll(TestData.songList);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeacrhExFragment.
     */
    // TODO: Rename and change types and number of parameters

    ImageButton backButton;
    RecyclerView searchResultView;
    List<ListItem> searchResultList;
    public static SeacrhExFragment newInstance(String param1, String param2) {
        SeacrhExFragment fragment = new SeacrhExFragment();
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
        return inflater.inflate(R.layout.fragment_seacrh_ex, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(this::onBackButtonClick);
        searchResultView = view.findViewById(R.id.search_result_view);
        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(searchResultList, new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        LinearLayoutManager searchResultLayoutManager = new LinearLayoutManager(this.getContext());
        searchResultView.setAdapter(searchResultAdapter);
        searchResultView.setLayoutManager(searchResultLayoutManager);
        searchResultView.addItemDecoration(new VerticalLinearSpacingItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));

    }

    private void onBackButtonClick(View view){
        callback.onRequestGoBackPreviousFragment();
    }
}