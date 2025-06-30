package com.example.manhinhappmusic;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView txtName, txtDate, txtContent;
        ImageView[] starViews = new ImageView[5];

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgfeedback);
            txtName = itemView.findViewById(R.id.txtnamefeedback);
            txtDate = itemView.findViewById(R.id.txtdatefeedback);
            txtContent = itemView.findViewById(R.id.txtcontentfeedback);


            starViews[0] = itemView.findViewById(R.id.star1);
            starViews[1] = itemView.findViewById(R.id.star2);
            starViews[2] = itemView.findViewById(R.id.star3);
            starViews[3] = itemView.findViewById(R.id.star4);
            starViews[4] = itemView.findViewById(R.id.star5);
        }
    }

    private final Context context;
    private List<Feedback> feedbackList;

    public FeedbackAdapter(Context context, List<Feedback> feedbackList) {
        this.context = context;
        this.feedbackList = feedbackList;
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

        holder.txtName.setText(feedback.getId());
        holder.txtDate.setText(feedback.getDate());
        holder.txtContent.setText(feedback.getContent());

        int rating = feedback.getRating();
        for (int i = 0; i < 5; i++) {
            if (holder.starViews[i] != null) {
                if (i < rating) {
                    holder.starViews[i].setColorFilter(
                            ContextCompat.getColor(context, R.color.amber),
                            android.graphics.PorterDuff.Mode.SRC_IN
                    );
                } else {
                    holder.starViews[i].setColorFilter(
                            ContextCompat.getColor(context, android.R.color.white),
                            android.graphics.PorterDuff.Mode.SRC_IN
                    );
                }
            }
        }

        // Nếu dùng ảnh đại diện từ URL → dùng Glide (nếu có)
        // Glide.with(context).load(...).into(holder.imgAvatar);
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public void updateData(List<Feedback> newList) {
        this.feedbackList = newList;
        notifyDataSetChanged();
    }



}
