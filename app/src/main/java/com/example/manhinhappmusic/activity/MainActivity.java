package com.example.manhinhappmusic.activity;

import android.os.Bundle;
import android.view.MenuItem;

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
import com.example.manhinhappmusic.fragment.user.MiniPlayerFragment;
import com.example.manhinhappmusic.fragment.user.NowPlayingSongFragment;
import com.example.manhinhappmusic.model.MediaPlayerManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MainActivity extends AppCompatActivity implements BaseFragment.FragmentInteractionListener {

    private BottomSheetDialogFragment bottomSheetDialogFragment;
    private BottomNavigationView bottomNavigationView;
//    AppFragmentFactory appFragmentFactory;
    private MiniPlayerFragment miniPlayer;
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getSupportFragmentManager().setFragmentFactory(appFragmentFactory);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

       MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance(this);


//        loadFragment(new UserHomeFragment());

        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        navController =  ((NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.host_fragment))
                .getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
//                if(navDestination.getId() == R.id.editProfileFragment)
//                    bottomNavigationView.setVisibility(View.GONE);
//                else if(navDestination.getId() == R.id.editPlaylistFragment)
//                    bottomNavigationView.setVisibility(View.GONE);
//                else
//                    bottomNavigationView.setVisibility(View.VISIBLE);
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
//        if(fragment != null){
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.frame_container, fragment)
//                    .addToBackStack(null)
//                    .commit();
//            return true;
//        }
        return false;
    }

    private boolean loadFrontFragment(Fragment fragment){
//        if(fragment != null){
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.front_frame_container, fragment)
//                    .addToBackStack(null)
//                    .commit();
//            return true;
//        }
        return false;
    }

    private boolean navigation(MenuItem item){
        Fragment selectedFragment = null;

//        try
//        {
//            if(item.getItemId() == R.id.nav_home)
//                selectedFragment = new UserHomeFragment();
//            else if(item.getItemId() == R.id.nav_search)
//                selectedFragment = new UserSearchFragment();
//            else if(item.getItemId() == R.id.nav_library)
//            {
//                selectedFragment = new UserLibraryFragment();
//
//
//            }
//        }
//        catch (Exception ex)
//        {
//            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
//        }


        return loadFragment(selectedFragment);
    }


    @Override
    public void onRequestChangeFragment(BaseFragment.FragmentTag destinationTag, Object... params) {
//        Fragment destinationFragment = null;
//        if(destinationTag == BaseFragment.FragmentTag.USER_PROFILE)
//            destinationFragment = new UserProfileFragment();
//        else if(destinationTag == BaseFragment.FragmentTag.SEARCH_EX)
//            destinationFragment = new SeacrhExFragment();
//        else if(destinationTag == BaseFragment.FragmentTag.USER_PLAYLIST)
//        {
//            String id = null;
//            if(params[0] instanceof String)
//            {
//                id = (String) params[0];
//            }
//            destinationFragment = UserPlaylistFragment.newInstance(id);
//
//        }
//        else if (destinationTag == BaseFragment.FragmentTag.LIBRARY_SEARCH) {
//            destinationFragment = new UserLibrarySearchFragment();
//        }
//        else if (destinationTag == BaseFragment.FragmentTag.PLAYLIST_ADD_SONG) {
//            String id = null;
//            if(params[0] instanceof String) {
//                id = (String) params[0];
//            }
//            destinationFragment = UserPlaylistAddSongFragment.newInstance(id);
//
//        }
//        else if(destinationTag == BaseFragment.FragmentTag.USER_GENRE)
//        {
//            String id = null;
//            if(params[0] instanceof String) {
//                id = (String) params[0];
//            }
//            destinationFragment = UserGenreFragment.newInstance(id);
//        }
//        else if(destinationTag == BaseFragment.FragmentTag.USER_ARTIST)
//        {
//            String id = null;
//            if(params[0] instanceof String) {
//                id = (String) params[0];
//            }
//            destinationFragment = UserArtistFragment.newInstance(id);
//        }
//        else if(destinationTag == BaseFragment.FragmentTag.USER_SEARCH_ADD_SONG)
//        {
//            String id = null;
//            if(params[0] instanceof String) {
//                id = (String) params[0];
//            }
//            destinationFragment = UserSearchAddSongFragment.newInstance(id);
//        }



//        loadFragment(destinationFragment);
    }

    @Override
    public void onRequestChangeFrontFragment(BaseFragment.FragmentTag destinationTag, Object... params) {
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
    }

    @Override
    public void onRequestChangeActivity(BaseFragment.FragmentTag destinationTag, Object... params) {

    }

    @Override
    public void onRequestOpenBottomSheetFragment(BaseFragment.FragmentTag destinationTag, Object... params) {

        if(destinationTag == BaseFragment.FragmentTag.EDIT_PROFILE)
        {
//            bottomSheetDialogFragment = new EditProfileFragment();
//            bottomSheetDialogFragment.show(getSupportFragmentManager(),"");
        }
        else if(destinationTag == BaseFragment.FragmentTag.NOW_PLAYING_SONG)
        {
            bottomSheetDialogFragment = new NowPlayingSongFragment();
            bottomSheetDialogFragment.show(getSupportFragmentManager(),"");
        }
//        else if(destinationTag == BaseFragment.FragmentTag.USER_SEARCH_ADD_SONG)
//        {
//            String id = null;
//            if(params[0] instanceof String) {
//                id = (String) params[0];
//            }
//            bottomSheetDialogFragment = UserSearchAddSongFragment.newInstance(id);
//            bottomSheetDialogFragment.show(getSupportFragmentManager(),"");
//        }

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