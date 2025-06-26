package com.example.manhinhappmusic.adapter;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manhinhappmusic.R;

import java.util.List;

public class LyricAdapter extends RecyclerView.Adapter<LyricAdapter.ViewHolder> {

    private List<String> lyrics;
    private int sungLyricColor;
    private int notSungLyricColor;
    private int currentLyricPosition = -1;
    private SparseBooleanArray sungLyrics = new SparseBooleanArray();

    public LyricAdapter(List<String> lyrics)
    {
        this.lyrics = lyrics;
        sungLyricColor = Color.parseColor("#FFFFFF");
        notSungLyricColor = Color.parseColor("#000000");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lyric, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView lyricsTextView = holder.getLyricsText();
        if(position <= currentLyricPosition)
        {
            lyricsTextView.setTextColor(sungLyricColor);
        }
        else
        {
            lyricsTextView.setTextColor(notSungLyricColor);

        }
        lyricsTextView.setText(lyrics.get(position));
    }

    @Override
    public int getItemCount() {
        return lyrics.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView lyricsText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lyricsText = itemView.findViewById(R.id.lyrics_text);
        }

        public TextView getLyricsText() {
            return lyricsText;
        }
    }

    public void setLyrics(List<String> lyrics) {
        this.lyrics = lyrics;
        notifyDataSetChanged();
    }

    public void setCurrentLyricPosition(int currentLyricPosition) {
        int oldCurrentPosition =  this.currentLyricPosition;
        this.currentLyricPosition = currentLyricPosition;
        if(currentLyricPosition == oldCurrentPosition + 1)
        {
            notifyItemChanged(currentLyricPosition);
        }
        else if(currentLyricPosition > oldCurrentPosition)
        {
            notifyItemRangeChanged(oldCurrentPosition + 1, currentLyricPosition - oldCurrentPosition);
        }
        else
        {
            notifyItemRangeChanged(currentLyricPosition + 1, oldCurrentPosition - currentLyricPosition);
        }
    }

    public void setNotSungLyricColor(int notSungLyricColor) {
        this.notSungLyricColor = notSungLyricColor;
    }

    public void setSungLyricColor(int sungLyricColor) {
        this.sungLyricColor = sungLyricColor;
    }



    public void clearSungLyric()
    {
        currentLyricPosition = -1;
    }
}
