package com.example.manhinhappmusic;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class PlaylistSongEditAdapter extends RecyclerView.Adapter<PlaylistSongEditAdapter.ViewHolder> {
    private List<Song> songList;
    public interface OnItemChangeListener{
        void onItemChange();
    }
    private OnItemChangeListener onItemChangeListener;
    private final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPos = viewHolder.getAdapterPosition();
            int toPos = target.getAdapterPosition();
            Collections.swap(songList, fromPos, toPos);
            Log.println(Log.INFO,"","swap");
            notifyItemMoved(fromPos, toPos);
            if(onItemChangeListener != null)
                onItemChangeListener.onItemChange();
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }
    };
    public ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
    public PlaylistSongEditAdapter(List<Song> songList, OnItemChangeListener onItemChangeListener)
    {
        this.songList = songList;
        this.onItemChangeListener = onItemChangeListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_playlist_song_edit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.getSongTitleText().setText(song.getTitle());
        holder.getArtistNameText().setText(song.getArtistId());
        holder.getRemoveButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songList.remove(song);
                notifyItemRemoved(holder.getAdapterPosition());
                if(onItemChangeListener != null)
                    onItemChangeListener.onItemChange();

            }
        });
        holder.getDragButton().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    itemTouchHelper.startDrag(holder);

                }
                else if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    v.performClick();
                }
                return false;

            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton removeButton;
        private final TextView songTitleText;
        private final TextView artistNameText;
        private final ImageButton dragButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            removeButton = itemView.findViewById(R.id.remove_button);
            songTitleText = itemView.findViewById(R.id.song_title_text);
            artistNameText = itemView.findViewById(R.id.artist_name_text);
            dragButton = itemView.findViewById(R.id.drag_button);

        }

        public ImageButton getRemoveButton() {
            return removeButton;
        }

        public TextView getSongTitleText() {
            return songTitleText;
        }

        public TextView getArtistNameText() {
            return artistNameText;
        }

        public ImageButton getDragButton() {
            return dragButton;
        }
    }

    public List<Song> getSongList() {
        return songList;
    }
}
