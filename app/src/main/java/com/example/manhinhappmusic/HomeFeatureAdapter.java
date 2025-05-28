package com.example.manhinhappmusic;

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

import java.util.List;

public class HomeFeatureAdapter extends RecyclerView.Adapter<HomeFeatureAdapter.ViewHolder>{

    private List<Playlist> playlistList;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    private OnItemClickListener onItemClickListener;

    public HomeFeatureAdapter(List<Playlist> playlistList, OnItemClickListener onItemClickListener)
    {
        this.playlistList = playlistList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_featuring, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlistList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(playlist.getThumnailResID())
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                .into(holder.getFeaturesCoverImage());
        holder.getFeaturesTitleText().setText(playlist.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlistList.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView featuresCoverImage;
        private final TextView featuresTitleText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            featuresCoverImage = itemView.findViewById(R.id.features_cover_image);
            featuresTitleText = itemView.findViewById(R.id.features_title_text);
        }

        public ImageView getFeaturesCoverImage() {
            return featuresCoverImage;
        }

        public TextView getFeaturesTitleText() {
            return featuresTitleText;
        }
    }
}
