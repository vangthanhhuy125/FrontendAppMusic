package com.example.manhinhappmusic;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MainActivity extends AppCompatActivity implements BaseFragment.FragmentInteractionListener {

    BottomSheetDialogFragment bottomSheetDialogFragment;
    BottomNavigationView bottomNavigationView;
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

        loadFragment(new UserHomeFragment());
        initializeView();
    }

    private void initializeView(){
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnItemSelectedListener(this::navigation);
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

        if(item.getItemId() == R.id.nav_home)
            selectedFragment = new UserHomeFragment();
        else if(item.getItemId() == R.id.nav_search)
            selectedFragment = new UserSearchFragment();
        else if(item.getItemId() == R.id.nav_library)
            selectedFragment = new UserLibraryFragment();

        return loadFragment(selectedFragment);
    }


    @Override
    public void onRequestChangeFragment(String destinationTag) {
        Fragment destinationFragment = null;
        if(destinationTag.equals("UserProfile"))
            destinationFragment = new UserProfileFragment();
        else if(destinationTag.equals("SearchEx"))
            destinationFragment = new SeacrhExFragment();
        loadFragment(destinationFragment);
    }

    @Override
    public void onRequestChangeActivity(String destinationTag) {

    }

    @Override
    public void onRequestOpenBottomSheetFragment(String destinationTag) {
        if(destinationTag.equals("EditProfile"))
        {
            bottomSheetDialogFragment = new EditProfileFragment();
            bottomSheetDialogFragment.show(getSupportFragmentManager(),"");
        }

    }

    @Override
    public void onRequestGoBackPreviousFragment() {
        getSupportFragmentManager().popBackStack();
    }
}