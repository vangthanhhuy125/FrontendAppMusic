package com.example.manhinhappmusic.activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.fragment.user.EditProfileFragment;
import com.example.manhinhappmusic.fragment.user.MiniPlayerFragment;
import com.example.manhinhappmusic.fragment.user.NowPlayingSongFragment;
import com.example.manhinhappmusic.fragment.user.UserArtistFragment;
import com.example.manhinhappmusic.fragment.user.UserGenreFragment;
import com.example.manhinhappmusic.fragment.user.UserLibrarySearchFragment;
import com.example.manhinhappmusic.fragment.user.UserPlaylistAddSongFragment;
import com.example.manhinhappmusic.fragment.user.UserPlaylistFragment;
import com.example.manhinhappmusic.fragment.user.UserProfileFragment;
import com.example.manhinhappmusic.fragment.user.UserSearchAddSongFragment;
import com.example.manhinhappmusic.fragment.user.SearchExFragment;
import com.example.manhinhappmusic.model.MediaPlayerManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MainActivity extends AppCompatActivity implements BaseFragment.FragmentInteractionListener {

    private BottomSheetDialogFragment bottomSheetDialogFragment;
    private BottomNavigationView bottomNavigationView;
    private MiniPlayerFragment miniPlayer;
    private NavController navController;

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

       MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance(this);

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        navController =  ((NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.host_fragment))
                .getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if(navDestination.getId() == R.id.editProfileFragment)
                    bottomNavigationView.setVisibility(View.GONE);
                else if(navDestination.getId() == R.id.editPlaylistFragment)
                    bottomNavigationView.setVisibility(View.GONE);
                else
                    bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
    }

   public void setBottomNavigationViewVisibility(int visibility)
   {
       bottomNavigationView.setVisibility(visibility);
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
                    .replace(R.id.host_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onRequestChangeFragment(BaseFragment.FragmentTag destinationTag, Object... params) {
        Fragment destinationFragment = null;
        if(destinationTag == BaseFragment.FragmentTag.USER_PROFILE)
            destinationFragment = new UserProfileFragment();
        else if(destinationTag == BaseFragment.FragmentTag.SEARCH_EX)
            destinationFragment = new SearchExFragment();
        else if(destinationTag == BaseFragment.FragmentTag.USER_PLAYLIST)
        {
            String id = null;
            if(params[0] instanceof String)
            {
                id = (String) params[0];
            }
            destinationFragment = UserPlaylistFragment.newInstance(id);

        }
        else if (destinationTag == BaseFragment.FragmentTag.LIBRARY_SEARCH) {
            destinationFragment = new UserLibrarySearchFragment();
        }
        else if (destinationTag == BaseFragment.FragmentTag.PLAYLIST_ADD_SONG) {
            String id = null;
            if(params[0] instanceof String) {
                id = (String) params[0];
            }
            destinationFragment = UserPlaylistAddSongFragment.newInstance(id);

        }
        else if(destinationTag == BaseFragment.FragmentTag.USER_GENRE)
        {
            String id = null;
            if(params[0] instanceof String) {
                id = (String) params[0];
            }
            destinationFragment = UserGenreFragment.newInstance(id);
        }
        else if(destinationTag == BaseFragment.FragmentTag.USER_ARTIST)
        {
            String id = null;
            if(params[0] instanceof String) {
                id = (String) params[0];
            }
            destinationFragment = UserArtistFragment.newInstance(id);
        }
        else if(destinationTag == BaseFragment.FragmentTag.USER_SEARCH_ADD_SONG)
        {
            String id = null;
            if(params[0] instanceof String) {
                id = (String) params[0];
            }
            destinationFragment = UserSearchAddSongFragment.newInstance(id);
        }

        loadFragment(destinationFragment);
    }

    @Override
    public void onRequestChangeFrontFragment(BaseFragment.FragmentTag destinationTag, Object... params) {

    }

    //    @Override
//    public void onRequestChangeFrontFragment(BaseFragment.FragmentTag destinationTag, Object... params) {
//        Fragment destinationFragment = null;
//        if(destinationTag == BaseFragment.FragmentTag.USER_SEARCH_ADD_SONG)
//        {
//            String id = null;
//            if(params[0] instanceof String) {
//                id = (String) params[0];
//            }
//            destinationFragment = UserSearchAddSongFragment.newInstance(id);
//        }
//        else if(destinationTag == BaseFragment.FragmentTag.PLAYLIST_EDIT)
//        {
//            String id = null;
//            if(params[0] instanceof String) {
//                id = (String) params[0];
//            }
//            destinationFragment = EditPlaylistFragment.newInstance(id);
//        }
//
//        loadFrontFragment(destinationFragment);
//    }
    @Override
    public void onRequestChangeActivity(BaseFragment.FragmentTag destinationTag, Object... params) {
    }

    public void onRequestOpenBottomSheetFragment(BaseFragment.FragmentTag destinationTag, Object... params) {
        switch (destinationTag) {
            case EDIT_PROFILE:
                showBottomSheet(new EditProfileFragment());
                break;
            case NOW_PLAYING_SONG:
                showBottomSheet(new NowPlayingSongFragment());
                break;
            case USER_SEARCH_ADD_SONG:
                if (params[0] instanceof String) {
                    showBottomSheet(UserSearchAddSongFragment.newInstance((String) params[0]));
                }
                break;
        }
    }

    private void showBottomSheet(BottomSheetDialogFragment fragment) {
        fragment.show(getSupportFragmentManager(), fragment.getClass().getSimpleName());
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