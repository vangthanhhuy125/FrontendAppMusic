package com.example.manhinhappmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ArtistRequestAdapter extends RecyclerView.Adapter<ArtistRequestAdapter.ViewHolder> {

    private Context context;
    private List<ArtistRequest> requestList;
    private OnRequestActionListener listener;

    public interface OnRequestActionListener {
        void onApprove(ArtistRequest request);
        void onReject(ArtistRequest request);
    }

    public ArtistRequestAdapter(Context context, List<ArtistRequest> requestList, OnRequestActionListener listener) {
        this.context = context;
        this.requestList = requestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArtistRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_artist_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistRequestAdapter.ViewHolder holder, int position) {
        ArtistRequest request = requestList.get(position);

        holder.textArtistName.setText("Author: " + request.getUserId());
        holder.textSongName.setText("Portfolio");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if (request.getSubmittedAt() != null) {
            holder.textRequestDate.setText(sdf.format(request.getSubmittedAt()));
        } else {
            holder.textRequestDate.setText("N/A");
        }

        holder.btnApprove.setOnClickListener(v -> listener.onApprove(request));
        holder.btnReject.setOnClickListener(v -> listener.onReject(request));
    }

    @Override
    public int getItemCount() {
        return requestList != null ? requestList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageArtist;
        TextView textSongName, textArtistName, textRequestDate;
        Button btnApprove, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageArtist = itemView.findViewById(R.id.image_artist_request);
            textSongName = itemView.findViewById(R.id.text_artist_song_name);
            textArtistName = itemView.findViewById(R.id.text_artist_author);
            textRequestDate = itemView.findViewById(R.id.text_artist_request_date);
            btnApprove = itemView.findViewById(R.id.btn_artist_approve);
            btnReject = itemView.findViewById(R.id.btn_artist_reject);
        }
    }
}
