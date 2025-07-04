package com.example.manhinhappmusic.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.fragment.ConfirmLoggingOutFragment;
import com.example.manhinhappmusic.fragment.admin.AddGenreFragment;
import com.example.manhinhappmusic.fragment.admin.AdminFeedbackFragment;
import com.example.manhinhappmusic.fragment.admin.AdminHomeFragment;
import com.example.manhinhappmusic.fragment.admin.AdminPlaylistFragment;
import com.example.manhinhappmusic.fragment.admin.AdminProfileFragment;
import com.example.manhinhappmusic.fragment.admin.ArtistDetailFragment;
import com.example.manhinhappmusic.fragment.admin.ConfirmDeletingGenreFragment;
import com.example.manhinhappmusic.fragment.admin.ConfirmDeletingUserFragment;
import com.example.manhinhappmusic.fragment.admin.EditGenreFragment;
import com.example.manhinhappmusic.fragment.admin.EditProfileAdminFragment;
import com.example.manhinhappmusic.fragment.admin.ListGenreFragment;
import com.example.manhinhappmusic.fragment.admin.ListUserFragment;
import com.example.manhinhappmusic.fragment.admin.UserDetailFragment;
import com.example.manhinhappmusic.fragment.artist.ConfirmDeletingSongFragment;
import com.example.manhinhappmusic.fragment.auth.LoginFragment;
import com.example.manhinhappmusic.model.Genre;
import com.example.manhinhappmusic.model.Song;
import com.example.manhinhappmusic.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

public class AdminActivity extends AppCompatActivity implements BaseFragment.FragmentInteractionListener {
    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    private static final String ARG_PREFERENCES = "AppPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        navController = ((NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.host_fragment))
                .getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.adminEditGenreFragment ||
                    navDestination.getId() == R.id.adminAddGenreFragment ||
                    navDestination.getId() == R.id.adminEditProfileFragment)
                bottomNavigationView.setVisibility(View.GONE);
            else
                bottomNavigationView.setVisibility(View.VISIBLE);
        });

        loadFragment(new AdminHomeFragment());
        initializeView();
    }

    private void initializeView() {
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnItemSelectedListener(this::navigation);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.host_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    private boolean navigation(MenuItem item) {
        Fragment selectedFragment = null;
        try {
            if (item.getItemId() == R.id.nav_home)
                selectedFragment = new AdminHomeFragment();
            else if (item.getItemId() == R.id.nav_user)
                selectedFragment = new ListUserFragment();
            else if (item.getItemId() == R.id.nav_songs)
                selectedFragment = new AdminPlaylistFragment();
            else if (item.getItemId() == R.id.nav_genres)
                selectedFragment = new ListGenreFragment();
            else if (item.getItemId() == R.id.nav_feedback)
                selectedFragment = new AdminFeedbackFragment();
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return loadFragment(selectedFragment);
    }

    @Override
    public void onRequestChangeFragment(BaseFragment.FragmentTag destinationTag, Object... params) {
        Fragment destinationFragment = null;
        if (destinationTag == BaseFragment.FragmentTag.EDIT_GENRE && params[0] instanceof Genre) {
            Genre genre = (Genre) params[0];
            destinationFragment = EditGenreFragment.newInstance(genre);
        } else if (destinationTag == BaseFragment.FragmentTag.ADMIN_PROFILE) {
            SharedPreferences preferences = getSharedPreferences(ARG_PREFERENCES, MODE_PRIVATE);
            String userJson = preferences.getString("current_user", null);
            if (userJson != null) {
                Gson gson = new Gson();
                User currentUser = gson.fromJson(userJson, User.class);
                destinationFragment = AdminProfileFragment.newInstance(currentUser);
            } else {
                destinationFragment = new AdminProfileFragment();
            }
        } else if (destinationTag == BaseFragment.FragmentTag.ADMIN_HOME) {
            destinationFragment = new AdminHomeFragment();
        } else if (destinationTag == BaseFragment.FragmentTag.EDIT_PROFILE_ADMIN) {
            destinationFragment = new EditProfileAdminFragment();
        } else if (destinationTag == BaseFragment.FragmentTag.CONFIRM_LOGGING_OUT) {
            ConfirmLoggingOutFragment fragment = new ConfirmLoggingOutFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.confirm_log_out_container, fragment)
                    .commit();

            findViewById(R.id.confirm_log_out_container).setVisibility(View.VISIBLE);
        } else if (destinationTag == BaseFragment.FragmentTag.LOGIN) {
            destinationFragment = new LoginFragment();
        } else if (destinationTag == BaseFragment.FragmentTag.LIST_GENRE) {
            destinationFragment = new ListGenreFragment();
            if (params.length > 0 && params[0] instanceof Genre) {
                Genre updatedGenre = (Genre) params[0];
                Bundle bundle = new Bundle();
                bundle.putSerializable("genre_result", updatedGenre);
                destinationFragment.setArguments(bundle);
            }
        } else if (destinationTag == BaseFragment.FragmentTag.CONFIRM_DELETING_GENRE) {
            Genre genre = null;
            if (params.length > 0 && params[0] instanceof Genre) {
                genre = (Genre) params[0];
            }
            destinationFragment = ConfirmDeletingGenreFragment.newInstance(genre, 0);

        } else if (destinationTag == BaseFragment.FragmentTag.CONFIRM_DELETING_SONG) {
            Song song = null;
            int position = -1;

            if (params.length > 0 && params[0] instanceof Song) {
                song = (Song) params[0];
            }

            if (params.length > 1 && params[1] instanceof Integer) {
                position = (int) params[1];
            }

            destinationFragment = ConfirmDeletingSongFragment.newInstance(song, position);
        } else if (destinationTag == BaseFragment.FragmentTag.ADD_GENRE) {
            destinationFragment = new AddGenreFragment();
        } else if (destinationTag == BaseFragment.FragmentTag.CONFIRM_DELETING_USER) {
            Fragment fragment = new ConfirmDeletingUserFragment();
            if (params.length > 0 && params[0] instanceof Bundle) {
                fragment.setArguments((Bundle) params[0]);
            }
            destinationFragment = fragment;
        } else if (destinationTag == BaseFragment.FragmentTag.ARTIST_DETAIL) {
            destinationFragment = new ArtistDetailFragment();
            if (params.length > 0 && params[0] instanceof User) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user_data", (User) params[0]);
                destinationFragment.setArguments(bundle);
            }
        } else if (destinationTag == BaseFragment.FragmentTag.USER_DETAIL) {
            destinationFragment = new UserDetailFragment();
            if (params.length > 0 && params[0] instanceof User) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("user_data", (User) params[0]);
                destinationFragment.setArguments(bundle);
            }
        }

        loadFragment(destinationFragment);
    }

    @Override
    public void onRequestChangeFrontFragment(BaseFragment.FragmentTag destinationTag, Object... params) {

    }

    @Override
    public void onRequestChangeActivity(BaseFragment.FragmentTag destinationTag, Object... params) {

    }

    @Override
    public void onRequestOpenBottomSheetFragment(BaseFragment.FragmentTag destinationTag, Object... params) {

    }

    @Override
    public void onRequestGoBackPreviousFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onRequestLoadMiniPlayer() {

    }

    @Override
    public void setIsProcessing(boolean isProcessing) {

    }

    public void hideBottomNav() {
        bottomNavigationView.animate().translationY(bottomNavigationView.getHeight()).setDuration(200);
    }

    public void showBottomNav() {
        bottomNavigationView.animate().translationY(0).setDuration(200);
    }
}
