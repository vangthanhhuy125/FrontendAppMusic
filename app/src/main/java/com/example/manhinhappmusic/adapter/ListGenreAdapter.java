package com.example.manhinhappmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.model.Genre;

import java.util.List;

public class ListGenreAdapter extends RecyclerView.Adapter<ListGenreAdapter.ViewHolder> {

    private List<Genre> genreList;
    private OnItemRemoveListener removeListener;
    private OnItemClickListener clickListener;

    public interface OnItemRemoveListener {
        void onRemove(int position);
    }

    public interface OnItemClickListener {
        void onItemClick(Genre genre);
    }

    public interface OnGenreClickListener {
        void onGenreClick(String genreId);
    }

    private Context context;

    public ListGenreAdapter(List<Genre> genreList, OnItemRemoveListener removeListener) {
        this.genreList = genreList;
        this.removeListener = removeListener;
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnGenreClickListener(OnGenreClickListener listener) {
        this.clickListener = genre -> {
            if (listener != null) {
                listener.onGenreClick(genre.getId());
            }
        };
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_genre, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Genre genre = genreList.get(position);
        holder.genreName.setText(genre.getName());

        Glide.with(holder.itemView)
                .load(genre.getThumbnailUrl())
                .placeholder(R.drawable.exampleavatar)
                .error(R.drawable.error_image)
                .centerCrop()
                .into(holder.genreImage);

        holder.removeButton.setOnClickListener(v -> {
            if (context instanceof BaseFragment.FragmentInteractionListener) {
                ((BaseFragment.FragmentInteractionListener) context).onRequestChangeFragment(
                        BaseFragment.FragmentTag.CONFIRM_DELETING_GENRE,
                        genre.getId(),
                        genre.getName()
                );
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (context instanceof BaseFragment.FragmentInteractionListener) {
                ((BaseFragment.FragmentInteractionListener) context).onRequestChangeFragment(
                        BaseFragment.FragmentTag.EDIT_GENRE,
                        genre.getId(),
                        genre.getName()
                );
            }

            if (clickListener != null) {
                clickListener.onItemClick(genre);
            }
        });


    }

    @Override
    public int getItemCount() {
        return genreList != null ? genreList.size() : 0;
    }

    public void updateData(List<Genre> newGenreList) {
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
