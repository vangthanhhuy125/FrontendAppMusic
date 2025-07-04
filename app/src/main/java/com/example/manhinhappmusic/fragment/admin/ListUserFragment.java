package com.example.manhinhappmusic.fragment.admin;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.activity.AdminActivity;
import com.example.manhinhappmusic.adapter.ListUserAdapter;
import com.example.manhinhappmusic.dto.UserResponse;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.model.User;
import com.example.manhinhappmusic.network.ApiService;
import com.example.manhinhappmusic.network.ApiClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListUserFragment extends BaseFragment {

    private RecyclerView recyclerViewUsers;
    private EditText searchBoxUser;
    private ImageView btnFilter;

    private List<User> allUsers = new ArrayList<>();
    private List<User> displayedUsers = new ArrayList<>();

    private ListUserAdapter adapter;

    private ApiService apiService;

    public ListUserFragment() {

    }

    public static ListUserFragment newInstance(String param1, String param2) {
        ListUserFragment fragment = new ListUserFragment();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", param1);
        args.putString("ARG_PARAM2", param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_user, container, false);

        recyclerViewUsers = view.findViewById(R.id.recyclerViewUsers);
        searchBoxUser = view.findViewById(R.id.searchBoxUser);
        btnFilter = view.findViewById(R.id.btn_filter);

        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        apiService = ApiClient.getApiService();
        loadAllUsers();

        adapter = new ListUserAdapter(getContext(), displayedUsers, new ListUserAdapter.OnUserClickListener() {
            @Override
            public void onViewDetail(User user) {
                if (user == null || user.getRole() == null) {
                    return;
                }

                switch (user.getRole().toUpperCase()) {
                    case "ROLE_ARTIST":
                        callback.onRequestChangeFragment(FragmentTag.ARTIST_DETAIL, user);
                        break;
                    case "ROLE_USER":
                        callback.onRequestChangeFragment(FragmentTag.USER_DETAIL, user);
                        break;
                    case "ROLE_ADMIN":
                        Bundle adminBundle = new Bundle();
                        adminBundle.putSerializable("user_data", user);
                        callback.onRequestChangeFragment(FragmentTag.ADMIN_PROFILE, adminBundle);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onDeleteUser(User user) {
                ConfirmDeletingUserFragment confirmFragment = new ConfirmDeletingUserFragment();
                Bundle args = new Bundle();
                args.putSerializable("user_data", user);
                confirmFragment.setArguments(args);

                View confirmContainer = requireView().findViewById(R.id.confirm_delete_container);
                confirmContainer.setVisibility(View.VISIBLE);

                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.confirm_delete_container, confirmFragment)
                        .commit();
            }
        });

        recyclerViewUsers.setAdapter(adapter);
        recyclerViewUsers.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 10) {
                    ((AdminActivity) requireActivity()).hideBottomNav();
                } else if (dy < -10) {
                    ((AdminActivity) requireActivity()).showBottomNav();
                }
            }
        });

        searchBoxUser.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }
        });

        btnFilter.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(requireContext(), btnFilter);
            popup.getMenuInflater().inflate(R.menu.menu_filter_user, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();

                String selectedRole = null;

                if (itemId == R.id.menu_all) {
                    displayedUsers = new ArrayList<>(allUsers);
                } else if (itemId == R.id.menu_artist) {
                    selectedRole = "ROLE_ARTIST";
                } else if (itemId == R.id.menu_user) {
                    selectedRole = "ROLE_USER";
                }

                if (selectedRole != null) {
                    List<User> filtered = new ArrayList<>();
                    for (User user : allUsers) {
                        if (selectedRole.equalsIgnoreCase(user.getRole())) {
                            filtered.add(user);
                        }
                    }
                    displayedUsers = filtered;
                }

                adapter = new ListUserAdapter(getContext(), displayedUsers, adapter.getListener());
                recyclerViewUsers.setAdapter(adapter);
                return true;
            });

            popup.show();
        });



        getChildFragmentManager().setFragmentResultListener("user_deleted", this, (requestKey, result) -> {
            try {
                User deletedUser = (User) result.getSerializable("deleted_user");
                if (deletedUser != null) {

                    Log.d("USER_DELETE", "Xóa user: " + deletedUser.getId());


                    allUsers.removeIf(user -> user.getId().equals(deletedUser.getId()));
                    displayedUsers.removeIf(user -> user.getId().equals(deletedUser.getId()));


                    adapter.notifyDataSetChanged();

                    Toast.makeText(getContext(), "Đã xoá: " + deletedUser.getFullName(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.w("USER_DELETE", "deletedUser null");
                }
            } catch (Exception e) {
                Log.e("USER_DELETE", "Lỗi xoá user", e);
                Toast.makeText(getContext(), "Xoá thất bại", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void loadAllUsers() {
        apiService.getAllUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> users = response.body();

                    // Tiếp tục gọi getAllArtists
                    apiService.getAllArtists().enqueue(new Callback<List<User>>() {
                        @Override
                        public void onResponse(Call<List<User>> call, Response<List<User>> artistResponse) {
                            if (artistResponse.isSuccessful() && artistResponse.body() != null) {
                                List<User> artists = artistResponse.body();

                                // Gộp users + artists
                                allUsers = new ArrayList<>();
                                allUsers.addAll(users);
                                allUsers.addAll(artists);

                                displayedUsers = new ArrayList<>(allUsers);
                                adapter = new ListUserAdapter(getContext(), displayedUsers, adapter.getListener());
                                recyclerViewUsers.setAdapter(adapter);
                            } else {
                                Toast.makeText(getContext(), "Lỗi tải danh sách artist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<User>> call, Throwable t) {
                            Log.e("API_ERROR", "Lỗi tải artists", t);
                            Toast.makeText(getContext(), "Lỗi mạng khi tải artist", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(getContext(), "Không thể tải danh sách user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi tải users", t);
                Toast.makeText(getContext(), "Lỗi mạng khi tải user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterUsers(String query) {
        List<User> filteredList = new ArrayList<>();
        for (User user : displayedUsers) {
            if (user.getFullName().toLowerCase().contains(query.toLowerCase()) ||
                    user.getEmail().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(user);
            }
        }
        adapter = new ListUserAdapter(getContext(), filteredList, adapter.getListener());
        recyclerViewUsers.setAdapter(adapter);
    }
}