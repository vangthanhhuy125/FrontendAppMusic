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
import com.example.manhinhappmusic.fragment.auth.ChangePasswordFragment;
import com.example.manhinhappmusic.fragment.auth.ForgotPasswordFragment;
import com.example.manhinhappmusic.fragment.auth.LoginFragment;
import com.example.manhinhappmusic.fragment.auth.OtpVerificationFragment;
import com.example.manhinhappmusic.fragment.auth.RegisterFragment;

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
                    .replace(R.id.main, fragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onRequestChangeFragment(BaseFragment.FragmentTag destinationTag, Object... params) {

        Fragment destinationFragment = null;
        if(destinationTag == BaseFragment.FragmentTag.LOGIN)
        {
            destinationFragment = new LoginFragment();
        }
        else if(destinationTag == BaseFragment.FragmentTag.REGISTER)
        {
            destinationFragment = new RegisterFragment();
        }
        else if(destinationTag == BaseFragment.FragmentTag.FORGOT_PASSWORD)
        {
            destinationFragment = new ForgotPasswordFragment();
        }
        else if(destinationTag == BaseFragment.FragmentTag.CHANGE_PASSWORD)
        {
            destinationFragment = new ChangePasswordFragment();
        }
        else if(destinationTag == BaseFragment.FragmentTag.OTP_VERIFICATION)
        {
            destinationFragment = new OtpVerificationFragment();
        }

        if(destinationFragment != null)
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
}