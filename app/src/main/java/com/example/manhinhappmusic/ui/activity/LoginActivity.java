package com.example.manhinhappmusic.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.manhinhappmusic.R;
import com.example.manhinhappmusic.ui.fragment.auth.ForgotPasswordFragment;
import com.example.manhinhappmusic.ui.fragment.auth.LoginFragment;
import com.example.manhinhappmusic.ui.fragment.auth.OtpVerificationFragment;
import com.example.manhinhappmusic.ui.fragment.auth.RegisterFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener,
        RegisterFragment.RegisterFragmentListener,
        ForgotPasswordFragment.ForgotPasswordFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            replaceFragment(new LoginFragment());
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onSignUpClicked() {
        replaceFragment(new RegisterFragment());
    }
    @Override
    public void onForgetPasswordClicked() {
        replaceFragment(new ForgotPasswordFragment());
    }

    @Override
    public void onBackToLogin() {
        replaceFragment(new LoginFragment());
    }

    @Override
    public void onOtpRequested(String email) {
        OtpVerificationFragment otpFragment = OtpVerificationFragment.newInstance(email);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, otpFragment)
                .addToBackStack(null)
                .commit();
    }
}