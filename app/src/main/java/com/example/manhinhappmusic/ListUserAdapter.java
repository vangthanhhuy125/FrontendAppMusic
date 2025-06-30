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

public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;
    private OnUserClickListener listener;

    public interface OnUserClickListener {
        void onViewDetail(User user);
        void onDeleteUser(User user);
    }

    public ListUserAdapter(Context context, List<User> userList, OnUserClickListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
    }

    public OnUserClickListener getListener() {
        return listener;
    }


    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.textName.setText(user.getFullName());
        holder.textEmail.setText(user.getEmail());

        if ("artist".equalsIgnoreCase(user.getRole())) {
            holder.textIsSinger.setText("Artist");
        } else {
            holder.textIsSinger.setText("");
        }

        if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
            Glide.with(context)
                    .load(user.getAvatarUrl())
                    .placeholder(R.drawable.exampleavatar)
                    .into(holder.imageUser);
        } else {
            holder.imageUser.setImageResource(user.getAvatarResID() != 0 ?
                    user.getAvatarResID() : R.drawable.exampleavatar);
        }

        holder.iconViewDetail.setOnClickListener(v -> listener.onViewDetail(user));
        holder.iconDeleteUser.setOnClickListener(v -> listener.onDeleteUser(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imageUser, iconViewDetail, iconDeleteUser;
        TextView textName, textEmail, textIsSinger;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            imageUser = itemView.findViewById(R.id.image_user);
            iconViewDetail = itemView.findViewById(R.id.icon_view_detail);
            iconDeleteUser = itemView.findViewById(R.id.icon_delete_user);
            textName = itemView.findViewById(R.id.text_name);
            textEmail = itemView.findViewById(R.id.text_email);
            textIsSinger = itemView.findViewById(R.id.textIsSinger);
        }
    }
}
