package com.example.manhinhappmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ArtistSongAdapter extends RecyclerView.Adapter<ArtistSongAdapter.SongViewHolder> {

    private final Context context;
    private final List<Song> songList;

    public ArtistSongAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_artist_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.textSongName.setText(song.getTitle());
        holder.textArtist.setText(song.getArtistId()); // Hoặc bạn có thể truyền tên nghệ sĩ nếu có

        // Nếu có ảnh URL thì load bằng Glide, còn không thì dùng ảnh test
        if (song.getCoverImageUrl() != null && !song.getCoverImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(song.getCoverImageUrl())
                    .placeholder(R.drawable.white_baseline_music_note_24)
                    .into(holder.imageSong);
        } else {
            holder.imageSong.setImageResource(song.getCoverImageResID() != 0 ?
                    song.getCoverImageResID() :
                    R.drawable.white_baseline_music_note_24);
        }
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSong;
        TextView textSongName, textArtist;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imageSong = itemView.findViewById(R.id.img_artist_song);
            textSongName = itemView.findViewById(R.id.text_name_song);
            textArtist = itemView.findViewById(R.id.text_title_song);
        }
    }
}
