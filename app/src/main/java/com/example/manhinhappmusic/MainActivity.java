package com.example.manhinhappmusic;

import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
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
//        getSupportFragmentManager().setFragmentFactory(appFragmentFactory);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        appFragmentFactory = new AppFragmentFactory(null, null, null, null);
//        appFragmentFactory.setLibrary(TestData.playlistList);
        MediaPlayerManager.getInstance(this);

        loadFragment(new UserHomeFragment());
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
                selectedFragment = new UserHomeFragment();
            else if(item.getItemId() == R.id.nav_search)
                selectedFragment = new UserSearchFragment();
            else if(item.getItemId() == R.id.nav_library)
            {
                selectedFragment = new UserLibraryFragment();


            }
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
        if(destinationTag == BaseFragment.FragmentTag.USER_PROFILE)
            destinationFragment = new UserProfileFragment();
        else if(destinationTag == BaseFragment.FragmentTag.SEARCH_EX)
            destinationFragment = new SeacrhExFragment();
        else if(destinationTag == BaseFragment.FragmentTag.USER_PLAYLIST)
        {
            String id = null;
            if(params[0] instanceof String)
            {
                id = (String) params[0];
            }
            destinationFragment = UserPlaylistFragment.newInstance(id);

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