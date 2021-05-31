package com.bar.automarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.bar.automarket.mainfragment.AccountFragment;
import com.bar.automarket.mainfragment.AddFragment;
import com.bar.automarket.mainfragment.FavoritesFragment;
import com.bar.automarket.mainfragment.FeedFragment;
import com.bar.automarket.mainfragment.LoginFragment;
import com.bar.automarket.mainfragment.RegisterFragment;
import com.bar.automarket.mainfragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FeedFragment feedFragment;
    private AccountFragment accountFragment;
    private AddFragment addFragment;
    private SearchFragment searchFragment;
    private FavoritesFragment favoritesFragment;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar

        BottomNavigationView bottomNavigation = findViewById(R.id.navigationView);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Initialize fragment
        feedFragment = new FeedFragment();
        accountFragment = new AccountFragment();
        addFragment = new AddFragment();
        searchFragment = new SearchFragment();
        favoritesFragment = new FavoritesFragment();
        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment();

        display(feedFragment);

        //Database
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    display(feedFragment);
                    return true;

                case R.id.navigation_favorites:
                    display(favoritesFragment);
                    return true;

                case R.id.navigation_search:
                    display(searchFragment);
                    return true;

                case R.id.navigation_add:
                    if(getUserStatus()) {
                        display(addFragment);
                        return true;
                    }
                    else
                        findViewById(R.id.navigation_account).callOnClick();
                    return false;

                case R.id.navigation_account:
                    displayAccount();
                    return true;
            }
            return false;
        }
    };

    protected void display(Fragment f) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (f.isAdded()) {
            ft.show(f);
        } else {
            ft.add(R.id.main_fragment, f);
        }
        if (accountFragment.isAdded() && f != accountFragment) { ft.hide(accountFragment);}
        if (addFragment.isAdded() && f != addFragment) { ft.hide(addFragment); }
        if (feedFragment.isAdded() && f != feedFragment) { ft.hide(feedFragment); }
        if (searchFragment.isAdded() && f != searchFragment) { ft.hide(searchFragment); }
        if (favoritesFragment.isAdded() && f != favoritesFragment) { ft.hide(favoritesFragment); }
        if (loginFragment.isAdded() && f != loginFragment) { ft.hide(loginFragment); }
        if (registerFragment.isAdded() && f != registerFragment) { ft.hide(registerFragment); }
        ft.commit();
    }

    public void displayAccount() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(getUserStatus()) {
            display(accountFragment);
            ft.remove(loginFragment);
        }
        else {
            display(loginFragment);
            ft.remove(accountFragment);
        }
        ft.commit();
    }

    public void displayRegister() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(loginFragment.isVisible()) {
            display(registerFragment);
            ft.remove(loginFragment);
        }
        else {
            display(loginFragment);
            ft.remove(registerFragment);
        }
        ft.commit();
    }

    private boolean getUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        return user != null;
    }

}