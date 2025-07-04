package com.example.manhinhappmusic.fragment.user;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.adapter.FeedbackAdapter;
import com.example.manhinhappmusic.dto.FeedbackResponse;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.model.Feedback;
import com.example.manhinhappmusic.network.ApiClient;
import com.example.manhinhappmusic.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFeedbackFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private FeedbackAdapter adapter;
    private Button btnAddFeedback;
    private ImageButton btnBack;

    private final List<Feedback> myFeedbackList = new ArrayList<>();
    private final ApiService apiService = ApiClient.getApiService();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_feedback, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerFeedback);
        btnAddFeedback = view.findViewById(R.id.btnAddFeedback);
        btnBack = view.findViewById(R.id.back_button);

        adapter = new FeedbackAdapter(getContext(), myFeedbackList, false); // false = không hiển thị nút reply
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        btnAddFeedback.setOnClickListener(v -> showSubmitFeedbackDialog());
        btnBack.setOnClickListener(view1 -> getParentFragmentManager().popBackStack());

        loadMyFeedbacks();
    }

    private void loadMyFeedbacks() {
        apiService.getMyFeedbacks().enqueue(new Callback<List<Feedback>>() {
            @Override
            public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    myFeedbackList.clear();
                    List<Feedback> res = response.body();

                    if (!res.isEmpty()) {
                        myFeedbackList.addAll(res);
                        btnAddFeedback.setVisibility(View.GONE); // đã gửi rồi thì ẩn nút gửi
                    } else {
                        btnAddFeedback.setVisibility(View.VISIBLE); // chưa gửi → cho gửi
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Không thể tải feedback", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Feedback>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSubmitFeedbackDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_submit_feedback, null);
        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
        EditText contentInput = dialogView.findViewById(R.id.feedbackContent);

        new AlertDialog.Builder(getContext())
                .setTitle("Gửi feedback")
                .setView(dialogView)
                .setPositiveButton("Gửi", (dialog, which) -> {
                    int rating = (int) ratingBar.getRating();
                    String content = contentInput.getText().toString().trim();

                    if (rating == 0 || content.isEmpty()) {
                        Toast.makeText(getContext(), "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Feedback feedback = new Feedback();
                    feedback.setContent(content);
                    feedback.setRating(rating);

                    apiService.sendFeedback(feedback).enqueue(new Callback<FeedbackResponse>() {
                        @Override
                        public void onResponse(Call<FeedbackResponse> call, Response<FeedbackResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getContext(), "Gửi thành công", Toast.LENGTH_SHORT).show();
                                loadMyFeedbacks(); // làm mới danh sách
                            } else {
                                Toast.makeText(getContext(), "Gửi thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FeedbackResponse> call, Throwable t) {
                            Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Huỷ", null)
                .show();
    }
}
