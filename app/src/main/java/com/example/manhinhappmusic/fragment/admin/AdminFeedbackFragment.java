package com.example.manhinhappmusic.fragment.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.adapter.FeedbackAdapter;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.model.Feedback;
import com.example.manhinhappmusic.network.ApiClient;
import com.example.manhinhappmusic.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminFeedbackFragment extends BaseFragment {
    private EditText searchBox;
    private ImageView imgFilter;
    private RecyclerView recyclerFeedback;
    private FeedbackAdapter adapter;
    private List<Feedback> allFeedbackList = new ArrayList<>();
    private List<Feedback> filteredList = new ArrayList<>();

    private final ApiService apiService = ApiClient.getApiService();

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

        adapter = new FeedbackAdapter(getContext(), filteredList, true);
        recyclerFeedback.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerFeedback.setAdapter(adapter);

        loadAllFeedbacks();

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

    private void loadAllFeedbacks() {
        apiService.getAllFeedbacks().enqueue(new Callback<List<Feedback>>() {
            @Override
            public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allFeedbackList.clear();
                    allFeedbackList.addAll(response.body());
                    updateFeedbackList(allFeedbackList);
                } else {
                    Toast.makeText(getContext(), "Lỗi khi tải feedback", Toast.LENGTH_SHORT).show();
                    Log.e("AdminFeedback", "Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Feedback>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("AdminFeedback", "Error", t);
            }
        });
    }

    private void filterFeedback(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(allFeedbackList);
        } else {
            for (Feedback fb : allFeedbackList) {
                if (fb.getContent().toLowerCase().contains(query.toLowerCase()) ||
                        fb.getUserId().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(fb);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void updateFeedbackList(List<Feedback> newList) {
        filteredList.clear();
        filteredList.addAll(newList);
        adapter.notifyDataSetChanged();
    }
}