package com.example.manhinhappmusic.fragment.admin;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.model.User;
import com.example.manhinhappmusic.network.ApiClient;
import com.example.manhinhappmusic.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmDeletingUserFragment extends Fragment {

    private static final String ARG_USER = "user_data";
    private User user;
    private ApiService apiService;

    public static ConfirmDeletingUserFragment newInstance(User user) {
        ConfirmDeletingUserFragment fragment = new ConfirmDeletingUserFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = ApiClient.getApiService();

        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_confirm_deleting_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button cancelButton = view.findViewById(R.id.cancel_button);
        Button deleteButton = view.findViewById(R.id.delete_button);

        cancelButton.setOnClickListener(v -> closePopup());

        deleteButton.setOnClickListener(v -> {
            if (user == null) {
                Toast.makeText(getContext(), "User is null!", Toast.LENGTH_SHORT).show();
                return;
            }

            apiService.deleteUser(user.getId()).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (!isAdded()) return;

                    if (response.isSuccessful()) {
                        Bundle result = new Bundle();
                        result.putSerializable("deleted_user", user);

                        if (getParentFragment() != null) {
                            getParentFragment().getChildFragmentManager()
                                    .setFragmentResult("user_deleted", result);
                        }

                        Toast.makeText(getContext(), "Đã xoá: " + user.getFullName(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("DELETE_USER", "Lỗi khi xoá user: " + response.code());
                        Toast.makeText(getContext(), "Lỗi xoá user", Toast.LENGTH_SHORT).show();
                    }
                    closePopup();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if (!isAdded()) return;
                    Log.e("DELETE_USER", "Lỗi mạng khi gọi API", t);
                    Toast.makeText(getContext(), "Lỗi mạng khi xoá user", Toast.LENGTH_SHORT).show();
                    closePopup();
                }
            });
        });
    }

    private void closePopup() {
        if (getParentFragment() != null) {
            getParentFragment().getChildFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();

            View container = getParentFragment().requireView().findViewById(R.id.confirm_delete_container);
            container.setVisibility(View.GONE);
        }
    }
}
