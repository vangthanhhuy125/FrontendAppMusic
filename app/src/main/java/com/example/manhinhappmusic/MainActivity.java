package com.example.manhinhappmusic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BaseFragment.FragmentInteractionListener {

    BottomSheetDialogFragment bottomSheetDialogFragment;
    BottomNavigationView bottomNavigationView;
//    AppFragmentFactory appFragmentFactory;
    MiniPlayerFragment miniPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MediaPlayerManager.getInstance(this);

        // 👇 Thay đổi ở đây: Load AdminHomeFragment thay vì UserHomeFragment
        loadFragment(new AdminHomeFragment());

        initializeView();
    }


    private void initializeView(){
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnItemSelectedListener(this::navigation);
    }

    private void loadMiniPlayer()
    {
        if (miniPlayer == null)
        {
            miniPlayer = new MiniPlayerFragment() ;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mini_player, miniPlayer)
                    .commit();
        }

    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    private boolean navigation(MenuItem item){
        Fragment selectedFragment = null;

        try
        {
            if(item.getItemId() == R.id.nav_home)
                selectedFragment = new AdminHomeFragment();
            else if(item.getItemId() == R.id.nav_user)
                selectedFragment = new ListUserFragment();
            else if(item.getItemId() == R.id.nav_songs)
                selectedFragment = new AdminPlaylistFragment();
            else if(item.getItemId() == R.id.nav_genres)
                selectedFragment = new ListGenreFragment();
            else if(item.getItemId() == R.id.nav_feedback)
                selectedFragment = new AdminFeedbackFragment();



        }
        catch (Exception ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


        return loadFragment(selectedFragment);
    }


    @Override
    public void onRequestChangeFragment(BaseFragment.FragmentTag destinationTag, Object... params) {
        Fragment destinationFragment = null;

        if (destinationTag == BaseFragment.FragmentTag.USER_PROFILE) {
            destinationFragment = new UserProfileFragment();
        }
        else if (destinationTag == BaseFragment.FragmentTag.SEARCH_EX) {
            destinationFragment = new SeacrhExFragment();
        }
        else if (destinationTag == BaseFragment.FragmentTag.USER_PLAYLIST) {
            String id = null;
            if (params[0] instanceof String) {
                id = (String) params[0];
            }
            destinationFragment = UserPlaylistFragment.newInstance(id);
        }
        else if (destinationTag == BaseFragment.FragmentTag.EDIT_GENRE) {
            if (params.length == 4 &&
                    params[0] instanceof String &&
                    params[1] instanceof String &&
                    params[2] instanceof String &&
                    (params[3] == null || params[3] instanceof String)) {

                String id = (String) params[0];
                String name = (String) params[1];
                String description = (String) params[2];
                String imageUrl = (String) params[3];

                destinationFragment = EditGenreFragment.newInstance(id, name, description, imageUrl);
            }
        }
        else if (destinationTag == BaseFragment.FragmentTag.ADMIN_PROFILE) {
            destinationFragment = new AdminProfileFragment();
        }
        else if (destinationTag == BaseFragment.FragmentTag.ADMIN_HOME) {
            destinationFragment = new AdminHomeFragment();
        }
        else if (destinationTag == BaseFragment.FragmentTag.EDIT_PROFILE_ADMIN) {
            destinationFragment = new EditProfileAdminFragment();
        }
        else if (destinationTag == BaseFragment.FragmentTag.CONFIRM_LOGGING_OUT) {
            ConfirmLoggingOutFragment fragment = new ConfirmLoggingOutFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.confirm_log_out_container, fragment)
                    .commit();

            findViewById(R.id.confirm_log_out_container).setVisibility(View.VISIBLE);
        }
        else if (destinationTag == BaseFragment.FragmentTag.LOGIN) {
            destinationFragment = new LoginFragment();
        }




        else if (destinationTag == BaseFragment.FragmentTag.LIST_GENRE) {
            destinationFragment = new ListGenreFragment();
            if (params.length > 0 && params[0] instanceof Genre) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("genre_result", (Genre) params[0]);
                destinationFragment.setArguments(bundle);
            }
        }
        else if (destinationTag == BaseFragment.FragmentTag.CONFIRM_DELETING_GENRE) {
            Genre genre = null;
            if (params.length > 0 && params[0] instanceof Genre) {
                genre = (Genre) params[0];
            }
            destinationFragment = ConfirmDeletingGenreFragment.newInstance(genre, 0);

        }
        else if (destinationTag == BaseFragment.FragmentTag.ADD_SONG) {
            destinationFragment = new AddSongFragment();
        }
        else if (destinationTag == BaseFragment.FragmentTag.EDIT_SONG) {
            Song song = null;
            if (params.length > 0 && params[0] instanceof Song) {
                song = (Song) params[0];
            }

            EditSongFragment fragment = EditSongFragment.newInstance(song);
            fragment.setOnSongEditedListener(updatedSong -> {
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                for (Fragment f : fragments) {
                    if (f instanceof AdminPlaylistFragment) {
                        ((AdminPlaylistFragment) f).updateSong(updatedSong);
                        break;
                    }
                }
            });

            destinationFragment = fragment;
        }
        else if (destinationTag == BaseFragment.FragmentTag.CONFIRM_DELETING_SONG) {
            Song song = null;
            int position = -1;

            if (params.length > 0 && params[0] instanceof Song) {
                song = (Song) params[0];
            }

            if (params.length > 1 && params[1] instanceof Integer) {
                position = (int) params[1];
            }

            destinationFragment = ConfirmDeletingSongFragment.newInstance(song, position);
        }

        else if (destinationTag == BaseFragment.FragmentTag.ADD_GENRE) {
            destinationFragment = new AddGenreFragment();
        }
        else if (destinationTag == BaseFragment.FragmentTag.CONFIRM_DELETING_USER) {
            Fragment fragment = new ConfirmDeletingUserFragment();
            if (params.length > 0 && params[0] instanceof Bundle) {
                fragment.setArguments((Bundle) params[0]);
            }
            destinationFragment = fragment;
        }
        else if (destinationTag == BaseFragment.FragmentTag.ARTIST_DETAIL) {
            destinationFragment = new ArtistDetailFragment();
            if (params.length > 0 && params[0] instanceof User) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("user_data", (User) params[0]);
                destinationFragment.setArguments(bundle);
            }
        }
        else if (destinationTag == BaseFragment.FragmentTag.USER_DETAIL) {
            destinationFragment = new UserDetailFragment();
            if (params.length > 0 && params[0] instanceof User) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("user_data", (User) params[0]);
                destinationFragment.setArguments(bundle);
            }
        }

        loadFragment(destinationFragment);
    }


    @Override
    public void onRequestChangeActivity(BaseFragment.FragmentTag destinationTag, Object... params) {

    }

    @Override
    public void onRequestOpenBottomSheetFragment(BaseFragment.FragmentTag destinationTag, Object... params) {

        if(destinationTag == BaseFragment.FragmentTag.EDIT_PROFILE)
        {
            bottomSheetDialogFragment = new EditProfileFragment();
            bottomSheetDialogFragment.show(getSupportFragmentManager(),"");
        }
        else if(destinationTag == BaseFragment.FragmentTag.NOW_PLAYING_SONG)
        {
            bottomSheetDialogFragment = new NowPlayingSongFragment();
            bottomSheetDialogFragment.show(getSupportFragmentManager(),"");
        }

    }

    @Override
    public void onRequestGoBackPreviousFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onRequestLoadMiniPlayer() {
        loadMiniPlayer();
    }


}
