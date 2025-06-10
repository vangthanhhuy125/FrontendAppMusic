package com.example.manhinhappmusic.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.fragment.BaseFragment;
import com.example.manhinhappmusic.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity implements BaseFragment.FragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadFragment(new LoginFragment());
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onRequestChangeFragment(BaseFragment.FragmentTag destinationTag, Object... params) {

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

    }

    @Override
    public void onRequestLoadMiniPlayer() {

    }
}