package com.example.manhinhappmusic;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminFeedbackFragment extends BaseFragment {

    private EditText searchBox;
    private ImageView imgFilter;
    private RecyclerView recyclerFeedback;
    private FeedbackAdapter adapter;

    private List<Feedback> allFeedbackList;
    private List<Feedback> filteredList;

    public AdminFeedbackFragment() {}

    public static AdminFeedbackFragment newInstance() {
        return new AdminFeedbackFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_feedback, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchBox = view.findViewById(R.id.searchBoxFB);
        imgFilter = view.findViewById(R.id.imgFilterFB);
        recyclerFeedback = view.findViewById(R.id.recyclerFeedback);


        allFeedbackList = new ArrayList<>();
        filteredList = new ArrayList<>();
        Feedback fb1 = new Feedback();
        fb1.setId("1");
        fb1.setUser_id("user001");
        fb1.setContent("Ứng dụng tốt");
        fb1.setRating(5);

        Feedback fb2 = new Feedback();
        fb2.setId("2");
        fb2.setUser_id("user002");
        fb2.setContent("Cần cải thiện tốc độ");
        fb2.setRating(3);



        allFeedbackList.add(fb1);
        allFeedbackList.add(fb2);
        filteredList.addAll(allFeedbackList);



        filteredList = new ArrayList<>(allFeedbackList);

        adapter = new FeedbackAdapter(getContext(), filteredList);
        recyclerFeedback.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerFeedback.setAdapter(adapter);


        searchBox.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFeedback(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });


        imgFilter.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), imgFilter);
            popupMenu.getMenuInflater().inflate(R.menu.popup_star_filter, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.filter_all) {
                    updateFeedbackList(allFeedbackList);
                } else {
                    int selectedStar = -1;
                    if (id == R.id.filter_5_star) selectedStar = 5;
                    else if (id == R.id.filter_4_star) selectedStar = 4;
                    else if (id == R.id.filter_3_star) selectedStar = 3;
                    else if (id == R.id.filter_2_star) selectedStar = 2;
                    else if (id == R.id.filter_1_star) selectedStar = 1;

                    List<Feedback> filtered = new ArrayList<>();
                    for (Feedback f : allFeedbackList) {
                        if (f.getRating() == selectedStar) {
                            filtered.add(f);
                        }
                    }
                    updateFeedbackList(filtered);
                }
                return true;
            });

            popupMenu.show();
        });
    }

    private void filterFeedback(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(allFeedbackList);
        } else {
            for (Feedback fb : allFeedbackList) {
                if (fb.getContent().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(fb);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void reverseList() {
        List<Feedback> reversed = new ArrayList<>(filteredList);
        Collections.reverse(reversed);
        filteredList.clear();
        filteredList.addAll(reversed);
        adapter.notifyDataSetChanged();
    }

    private void updateFeedbackList(List<Feedback> newList) {
        filteredList.clear();
        filteredList.addAll(newList);
        adapter.notifyDataSetChanged();
    }
}
