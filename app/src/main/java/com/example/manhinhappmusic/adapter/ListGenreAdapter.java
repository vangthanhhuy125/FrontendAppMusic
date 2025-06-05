package com.example.manhinhappmusic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.model.Genre;

import java.util.List;

public class ListGenreAdapter extends RecyclerView.Adapter<ListGenreAdapter.ViewHolder>{

    private List<Genre> genreList;
    private OnItemRemoveListener removeListener;

    public interface OnItemRemoveListener {
        void onRemove(int position);
    }

    public ListGenreAdapter(List<Genre> genreList, OnItemRemoveListener removeListener) {
        this.genreList = genreList;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_genre, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Genre genre = genreList.get(position);
        holder.genreName.setText(genre.getName());

//        // Glide không cần context nếu dùng từ view
//        Glide.with(holder.itemView)
//                .load(genre.getUrlCoverImage())
//                .placeholder(R.drawable.placeholder_image)
//                .error(R.drawable.error_image)
//                .centerCrop()
//                .into(holder.genreImage);

        holder.removeButton.setOnClickListener(v -> {
            if (removeListener != null) {
                removeListener.onRemove(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView genreImage;
        private TextView genreName;
        private ImageButton removeButton;

        public ImageButton getRemoveButton() {
            return removeButton;
        }

        public ImageView getGenreImage() {
            return genreImage;
        }

        public TextView getGenreName() {
            return genreName;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            genreImage = itemView.findViewById(R.id.genreImage);
            genreName = itemView.findViewById(R.id.genreName);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
