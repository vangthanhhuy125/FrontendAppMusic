package com.example.manhinhappmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ListGenreAdapter extends RecyclerView.Adapter<ListGenreAdapter.ViewHolder> {

    private List<Genre> genreList;
    private OnItemRemoveListener removeListener;
    private OnItemClickListener clickListener;
    private OnGenreClickListener genreClickListener;
    private Context context;

    public interface OnItemRemoveListener {
        void onRemove(int position, Genre genre);
    }

    public interface OnItemClickListener {
        void onItemClick(Genre genre);
    }

    public interface OnGenreClickListener {
        void onGenreClick(String genreId);
    }

    public ListGenreAdapter(List<Genre> genreList, OnItemRemoveListener removeListener) {
        this.genreList = genreList;
        this.removeListener = removeListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnGenreClickListener(OnGenreClickListener listener) {
        this.genreClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_genre, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Genre genre = genreList.get(position);
        holder.genreName.setText(genre.getName());

        String imageUrl = genre.getUrlCoverImage();
        if (imageUrl == null || imageUrl.isEmpty()) {
            holder.genreImage.setImageResource(R.drawable.exampleavatar);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.exampleavatar)
                    .error(R.drawable.error_image)
                    .centerCrop()
                    .into(holder.genreImage);
        }

        // Sự kiện xóa
        holder.removeButton.setOnClickListener(v -> {
            FragmentActivity activity = (FragmentActivity) v.getContext();
            ConfirmDeletingGenreFragment dialog = ConfirmDeletingGenreFragment.newInstance(genre, holder.getAdapterPosition());

            dialog.setConfirmGenreDeleteListener((genreId, pos) -> {
                if (removeListener != null) {
                    removeListener.onRemove(pos, genre);
                }
            });

            dialog.show(activity.getSupportFragmentManager(), "ConfirmDeleteGenreDialog");
        });

        // Sự kiện click vào item
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(genre);
            }
            if (genreClickListener != null) {
                genreClickListener.onGenreClick(genre.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return genreList != null ? genreList.size() : 0;
    }

    public void updateData(List<Genre> newGenreList) {
        if (newGenreList == null) return;
        genreList.clear();
        genreList.addAll(newGenreList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView genreImage;
        private TextView genreName;
        private ImageButton removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            genreImage = itemView.findViewById(R.id.genreImage);
            genreName = itemView.findViewById(R.id.genreName);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
