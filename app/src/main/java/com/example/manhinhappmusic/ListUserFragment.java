package com.example.manhinhappmusic;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.ArrayList;
import java.util.List;

public class ListUserFragment extends BaseFragment {

    private RecyclerView recyclerViewUsers;
    private EditText searchBoxUser;
    private ImageView btnFilter;

    private List<User> allUsers = new ArrayList<>();
    private List<User> displayedUsers = new ArrayList<>();

    private ListUserAdapter adapter;

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


    public void onDeleteUser(User user) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("user_data", user);
        callback.onRequestChangeFragment(FragmentTag.CONFIRM_DELETING_USER, bundle);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_user, container, false);

        recyclerViewUsers = view.findViewById(R.id.recyclerViewUsers);
        searchBoxUser = view.findViewById(R.id.searchBoxUser);
        btnFilter = view.findViewById(R.id.btn_filter);

        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        allUsers = getMockUsers();
        displayedUsers = new ArrayList<>(allUsers);



        adapter = new ListUserAdapter(getContext(), displayedUsers, new ListUserAdapter.OnUserClickListener() {
            @Override
            public void onViewDetail(User user) {
                if ("artist".equalsIgnoreCase(user.getRole())) {
                    callback.onRequestChangeFragment(FragmentTag.ARTIST_DETAIL, user);
                } else {
                    callback.onRequestChangeFragment(FragmentTag.USER_DETAIL, user);
                }
            }

            @Override
            public void onDeleteUser(User user) {
                allUsers.remove(user);
                displayedUsers.remove(user);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Deleted: " + user.getFullName(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerViewUsers.setAdapter(adapter);

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

                displayedUsers.clear();

                if (itemId == R.id.menu_all) {
                    displayedUsers.addAll(allUsers);
                } else if (itemId == R.id.menu_artist) {
                    for (User user : allUsers) {
                        if ("artist".equalsIgnoreCase(user.getRole())) {
                            displayedUsers.add(user);
                        }
                    }
                } else if (itemId == R.id.menu_user) {
                    for (User user : allUsers) {
                        if ("user".equalsIgnoreCase(user.getRole())) {
                            displayedUsers.add(user);
                        }
                    }
                }

                adapter = new ListUserAdapter(getContext(), displayedUsers, adapter.getListener());
                recyclerViewUsers.setAdapter(adapter);
                return true;
            });

            popup.show();
        });



        getParentFragmentManager().setFragmentResultListener("user_deleted", this, (requestKey, result) -> {
            User deletedUser = result.getParcelable("deleted_user");
            if (deletedUser != null) {
                allUsers.remove(deletedUser);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Deleted from result: " + deletedUser.getFullName(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
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


    private List<User> getMockUsers() {
        List<User> list = new ArrayList<>();
        list.add(new User("1", "a@gmail.com", "pass", "Nguyễn", "Nguyễn Văn A", "artist", R.drawable.exampleavatar));
        list.add(new User("2", "b@gmail.com", "pass", "Trần", "Trần Thị B", "user", R.drawable.exampleavatar));
        list.add(new User("3", "c@gmail.com", "pass", "Lê", "Lê Văn C", "artist", R.drawable.exampleavatar));
        list.add(new User("4", "d@gmail.com", "pass", "Phạm", "Phạm Thị D", "user", R.drawable.exampleavatar));
        list.add(new User("5", "e@gmail.com", "pass", "Đỗ", "Đỗ Văn E", "artist", R.drawable.exampleavatar));
        return list;
    }

}
