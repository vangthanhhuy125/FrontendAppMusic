package com.example.manhinhappmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.model.Playlist;

import java.util.List;

public class PlaylistTrendAdapter extends RecyclerView.Adapter<PlaylistTrendAdapter.ViewHolder> {

    private List<Playlist> playlists;
    private static OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public PlaylistTrendAdapter(Context context, List<Playlist> playlists) {
        this.playlists = playlists;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    @NonNull
    @Override
    public PlaylistTrendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlisttrend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.getThumbnail().setImageResource(R.drawable.exampleavatar);
        holder.getTextTitle().setText(playlist.getDescription());
//        holder.getTextArtist().setText(playlist.getArtist());
//        holder.getTextViews().setText(String.valueOf(playlist.getViewCount()));
    }



    @Override
    public int getItemCount() {
        return playlists != null ? playlists.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail;
        private TextView textTitle;
        private TextView textArtist;
        private TextView textViews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && onItemClickListener != null)
                    {
                        onItemClickListener.onItemClick(position);
                    }
                }
            });

            thumbnail = itemView.findViewById(R.id.image_thumbnail);
            textTitle = itemView.findViewById(R.id.text_title);
            textArtist = itemView.findViewById(R.id.text_artist);
            textViews = itemView.findViewById(R.id.text_views);

        }

        public ImageView getThumbnail() { return thumbnail; }

        public TextView getTextArtist() { return textArtist; }

        public TextView getTextTitle() { return textTitle; }

        public TextView getTextViews() { return textViews; }
    }

    public List<Playlist> getPlaylists() { return playlists; }
}
