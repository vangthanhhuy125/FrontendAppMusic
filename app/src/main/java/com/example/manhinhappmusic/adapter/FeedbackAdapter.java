package com.example.manhinhappmusic.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.dto.FeedbackResponse;
import com.example.manhinhappmusic.model.Feedback;
import com.example.manhinhappmusic.network.ApiClient;
import com.example.manhinhappmusic.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    private boolean isAdmin = true;
    private Context context;
    private List<Feedback> feedbackList;
    private final ApiService apiService = ApiClient.getApiService();

    public FeedbackAdapter(Context context, List<Feedback> feedbackList, boolean isAdmin) {
        this.context = context;
        this.feedbackList = feedbackList;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feedback feedback = feedbackList.get(position);
        holder.userName.setText(feedback.getUserName());
        holder.ratingBar.setRating(feedback.getRating());
        holder.content.setText(feedback.getContent());

        Glide.with(context)
                .load(ApiService.BASE_URL + feedback.getUserAvatarUrl())
                .placeholder(R.drawable.exampleavatar)
                .into(holder.avatar);

        // Giả sử dùng thời gian đơn giản
        holder.time.setText(feedback.getCreatedAt());

        // Admin reply (nổi bật)
        if (feedback.getReply() != null && !feedback.getReply().isEmpty()) {
            holder.adminReply.setVisibility(View.VISIBLE);
            holder.adminReply.setText("Admin reply: " + feedback.getReply());
        } else {
            holder.adminReply.setVisibility(View.GONE);
        }

        // Gắn nút reply
        holder.btnReply.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
        holder.btnReply.setOnClickListener(v -> showReplyDialog(feedback, holder.getAdapterPosition()));
    }

    private void showReplyDialog(Feedback feedback, int position) {
        EditText input = new EditText(context);
        input.setHint("Reply...");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(feedback.getReply() != null ? feedback.getReply() : "");

        new AlertDialog.Builder(context)
                .setTitle("Reply to Feedback")
                .setView(input)
                .setPositiveButton("Send", (dialog, which) -> {
                    String replyText = input.getText().toString().trim();
                    if (!replyText.isEmpty()) {
                        Feedback updated = new Feedback();
                        updated.setReply(replyText);

                        apiService.replyFeedback(feedback.getId(), updated)
                                .enqueue(new Callback<Feedback>() {
                                    @Override
                                    public void onResponse(Call<Feedback> call, Response<Feedback> response) {
                                        if (response.isSuccessful()) {
                                            feedback.setReply(replyText);
                                            notifyItemChanged(position);
                                            Toast.makeText(context, "Reply sent", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Failed to reply", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Feedback> call, Throwable t) {
                                        Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView userName, content, time, adminReply;
        RatingBar ratingBar;
        ImageView avatar;
        Button btnReply;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            content = itemView.findViewById(R.id.content);
            time = itemView.findViewById(R.id.time);
            adminReply = itemView.findViewById(R.id.adminReply);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            avatar = itemView.findViewById(R.id.avatar);
            btnReply = itemView.findViewById(R.id.sendReply);
        }
    }
}
