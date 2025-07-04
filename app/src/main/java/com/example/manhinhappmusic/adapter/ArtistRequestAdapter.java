package com.example.manhinhappmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manhinhappmusic.fragment.admin.ConfirmApproveFragment;
import com.example.manhinhappmusic.fragment.admin.ConfirmRejectFragment;
import com.example.manhinhappmusic.model.ArtistRequest;
import com.example.manhinhappmusic.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ArtistRequestAdapter extends RecyclerView.Adapter<ArtistRequestAdapter.ViewHolder> {

    private final Context context;
    private final List<ArtistRequest> requests;
    private final FragmentManager fragmentManager;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public ArtistRequestAdapter(Context context, List<ArtistRequest> requests, FragmentManager fragmentManager) {
        this.context = context;
        this.requests = requests;
        this.fragmentManager = fragmentManager;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView songName, author, requestDate;
        ImageView avatar;
        View btnApprove, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.image_artist_request);
            songName = itemView.findViewById(R.id.text_artist_song_name);
            author = itemView.findViewById(R.id.text_artist_author);
            requestDate = itemView.findViewById(R.id.text_artist_request_date);
            btnApprove = itemView.findViewById(R.id.btn_artist_approve);
            btnReject = itemView.findViewById(R.id.btn_artist_reject);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_artist_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArtistRequest request = requests.get(position);
        holder.songName.setText("Portfolio");
        holder.author.setText("Author: " + request.getUserId());

        if (request.getSubmittedAt() != null) {
            holder.requestDate.setText(dateFormat.format(request.getSubmittedAt()));
        } else {
            holder.requestDate.setText("N/A");
        }

        holder.avatar.setImageResource(R.drawable.exampleavatar);

        holder.btnApprove.setOnClickListener(v -> {
            request.setStatus("approved");
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container_admin_home_view, ConfirmApproveFragment.newInstance(null, null))
                    .addToBackStack(null)
                    .commit();
        });

        holder.btnReject.setOnClickListener(v -> {
            request.setStatus("rejected");
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container_admin_home_view, ConfirmRejectFragment.newInstance(null, null))
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return requests != null ? requests.size() : 0;
    }
}
