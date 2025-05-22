package com.example.manhinhappmusic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    List<Artist> artistItems;

    public ArtistAdapter(List<Artist> artistItems){
        this.artistItems = artistItems;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_artist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getArtistName().setText(artistItems.get(position).getName());
        holder.getArtistImage().setImageResource(R.drawable.example_artist);
    }

    @Override
    public int getItemCount() {
        return artistItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ShapeableImageView artistImage;
        private final TextView artistName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            artistImage = itemView.findViewById(R.id.artist_img);
            artistName = itemView.findViewById(R.id.artist_name);
        }

        public ShapeableImageView getArtistImage() {
            return artistImage;
        }

        public TextView getArtistName() {
            return artistName;
        }
    }


}
