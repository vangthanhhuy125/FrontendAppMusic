package com.example.manhinhappmusic;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminHomeFragment extends BaseFragment {

    private TextView totalUsers, totalArtists, totalGenres, totalSongs;
    private RecyclerView recyclerArtistRequests;
    private ImageView imageAvatar;
    private ArtistRequestAdapter adapter;
    private final List<ArtistRequest> artistRequests = new ArrayList<>();

    public static AdminHomeFragment newInstance() {
        return new AdminHomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        totalUsers = view.findViewById(R.id.textad_total_users);
        totalArtists = view.findViewById(R.id.textad_total_artists);
        totalGenres = view.findViewById(R.id.textad_total_genres);
        totalSongs = view.findViewById(R.id.textad_total_songs);
        recyclerArtistRequests = view.findViewById(R.id.recycler_artist_requests);
        imageAvatar = view.findViewById(R.id.image_adavatar);

        totalUsers.setText("156");
        totalArtists.setText("42");
        totalGenres.setText("18");
        totalSongs.setText("432");

        Glide.with(this)
                .load(R.drawable.exampleavatar)
                .into(imageAvatar);

        imageAvatar.setOnClickListener(v -> {
            if (callback != null) {
                callback.onRequestChangeFragment(FragmentTag.ADMIN_PROFILE);
            }
        });

        artistRequests.add(new ArtistRequest("user4", "https://portfolio.com/artistX", "pending", new Date()));
        artistRequests.add(new ArtistRequest("user5", "https://portfolio.com/artistY", "pending", new Date()));

        recyclerArtistRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ArtistRequestAdapter(requireContext(), artistRequests, getChildFragmentManager(), artistRequests);
        recyclerArtistRequests.setAdapter(adapter);
    }

    public static class ArtistRequestAdapter extends RecyclerView.Adapter<ArtistRequestAdapter.ViewHolder> {

        private final Context context;
        private final FragmentManager fragmentManager;
        private final List<ArtistRequest> requests;
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        public ArtistRequestAdapter(Context context, List<ArtistRequest> requests, FragmentManager fragmentManager, List<ArtistRequest> fullListRef) {
            this.context = context;
            this.fragmentManager = fragmentManager;
            this.requests = fullListRef;
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
                request.setRole("artist");

                if (position != RecyclerView.NO_POSITION) {

                    ConfirmApproveFragment fragment = ConfirmApproveFragment.newInstance(request);
                    fragment.show(fragmentManager, "ConfirmApprove");


                    requests.remove(position);
                    notifyItemRemoved(position);
                }
            });

            holder.btnReject.setOnClickListener(v -> {
                request.setStatus("rejected");

                ConfirmRejectFragment fragment = ConfirmRejectFragment.newInstance(request);
                fragment.setOnRejectConfirmedListener(rejectedRequest -> {
                    int index = requests.indexOf(rejectedRequest);
                    if (index != -1) {
                        requests.remove(index);
                        notifyItemRemoved(index);
                    }
                });


                fragment.show(fragmentManager, "ConfirmReject");
            });
        }

        @Override
        public int getItemCount() {
            return requests != null ? requests.size() : 0;
        }
    }
}

