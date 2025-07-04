package com.example.manhinhappmusic.fragment.user;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.dto.FeedbackResponse;
import com.example.manhinhappmusic.model.Feedback;
import com.example.manhinhappmusic.model.User;
import com.example.manhinhappmusic.network.ApiClient;
import com.example.manhinhappmusic.network.ApiService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitFeedbackDialogFragment extends DialogFragment {

    public interface OnFeedbackSubmittedListener {
        void onFeedbackSubmitted(Feedback feedback);
    }

    private OnFeedbackSubmittedListener listener;

    public void setOnFeedbackSubmittedListener(OnFeedbackSubmittedListener listener) {
        this.listener = listener;
    }

    private RatingBar ratingBar;
    private EditText feedbackContent;
    private ApiClient apiClient;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_submit_feedback, null);
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(view);

        ratingBar = view.findViewById(R.id.ratingBar);
        feedbackContent = view.findViewById(R.id.feedbackContent);
        return dialog;
    }

    private void submitFeedback() {
        int rating = (int) ratingBar.getRating();
        String content = feedbackContent.getText().toString().trim();

        if (rating == 0) {
            Toast.makeText(getContext(), "Vui lòng chọn số sao", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String currentUserJson = prefs.getString("current_user", null);
        User currentUser = new Gson().fromJson(currentUserJson, User.class);
        String userId = currentUser.getId();

        Feedback feedback = new Feedback();
        feedback.setUserId(userId);
        feedback.setRating(rating);
        feedback.setContent(content);

        ApiService apiService = ApiClient.getApiService();
        apiService.sendFeedback(feedback).enqueue(new Callback<FeedbackResponse>() {
            @Override
            public void onResponse(@NonNull Call<FeedbackResponse> call, @NonNull Response<FeedbackResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "Đã gửi đánh giá!", Toast.LENGTH_SHORT).show();

                    Feedback result = new Feedback();
                    result.setId(response.body().getId());
                    result.setUserId(response.body().getUserId());
                    result.setRating(response.body().getRating());
                    result.setContent(response.body().getContent());
                    result.setReply(response.body().getReply());
                    result.setCreatedAt(response.body().getCreatedAt());
                    result.setUserName(response.body().getUserName());
                    result.setUserAvatarUrl(response.body().getUserAvatarUrl());

                    if (listener != null) {
                        listener.onFeedbackSubmitted(result);
                    }

                    dismiss();
                } else {
                    Toast.makeText(getContext(), "Gửi thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<FeedbackResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
