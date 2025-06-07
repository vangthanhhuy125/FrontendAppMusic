package com.example.manhinhappmusic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.model.User;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    List<User> artistList;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    private OnItemClickListener onItemClickListener;
    public ArtistAdapter(List<User> artistItems, OnItemClickListener onItemClickListener){
        this.artistList = artistItems;
        this.onItemClickListener = onItemClickListener;
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
        User artist = artistList.get(position);
        holder.getArtistName().setText(artistList.get(position).getFullName());
        Glide.with(holder.itemView.getContext())
                .load(artist.getAvatarResID())
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(80))))
                .into(holder.getArtistImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final ImageView artistImage;
        private final TextView artistName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            artistImage = itemView.findViewById(R.id.artist_img);
            artistName = itemView.findViewById(R.id.artist_name);
        }

        public ImageView getArtistImage() {
            return artistImage;
        }

        public TextView getArtistName() {
            return artistName;
        }
    }


}
