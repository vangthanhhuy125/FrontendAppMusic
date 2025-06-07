package com.example.manhinhappmusic.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.Button;
import android.widget.ImageButton;
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
import com.example.manhinhappmusic.model.Song;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private  List<Song> songList;
    private static OnItemClickListener itemClickListener;
    private OnItemMoreOptionsClickListener onItemMoreOptionsClickListener;
    public interface OnItemClickListener{
        void onItemClick(int position, Song song);
    }
    public interface OnItemMoreOptionsClickListener{
        void onItemMoreOptionsCLick(int position, Song song);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.getTextViewArtist().setText(song.getArtistId());
        holder.getTextViewSongTitle().setText(song.getTitle());
        Glide.with(holder.itemView.getContext())
                .load(song.getCoverImageResID())
                .apply(new RequestOptions().transform(new MultiTransformation<>(new CenterCrop(), new RoundedCorners(15))))
                .into(holder.getImageViewThumbnail());
        holder.getMoreOptionsButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemMoreOptionsClickListener != null)
                {
                    onItemMoreOptionsClickListener.onItemMoreOptionsCLick(holder.getAdapterPosition(), song);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null)
                {
                    itemClickListener.onItemClick(holder.getAdapterPosition(), song);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (songList != null) ? songList.size() : 0;
    }

    public SongAdapter(List<Song> songList, OnItemClickListener itemClickListener)
    {
        this.songList = songList;
        this.itemClickListener = itemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textViewArtist;
        private final TextView textViewSongTitle;
        private final ImageButton moreOptionsButton;
        private final ImageView imageViewThumbnail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewArtist = itemView.findViewById(R.id.txtArtist);
            textViewSongTitle = itemView.findViewById(R.id.txtSongTitle);
            moreOptionsButton = itemView.findViewById(R.id.more_options_button);
            imageViewThumbnail = itemView.findViewById(R.id.imgThumbnail);
            moreOptionsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public TextView getTextViewArtist() {
            return textViewArtist;
        }

        public TextView getTextViewSongTitle() {
            return textViewSongTitle;
        }

        public ImageButton getMoreOptionsButton() {
            return moreOptionsButton;
        }

        public ImageView getImageViewThumbnail() {
            return imageViewThumbnail;
        }
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public void setOnItemMoreOptionsClickListener(OnItemMoreOptionsClickListener onItemMoreOptionsClickListener) {
        this.onItemMoreOptionsClickListener = onItemMoreOptionsClickListener;
    }
}
