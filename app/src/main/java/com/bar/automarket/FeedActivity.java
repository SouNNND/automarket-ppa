package com.bar.automarket;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class FeedActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void launchLoginActivity(View view) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null) {
            //user is signed in
            startActivity(new Intent(FeedActivity.this, ProfileActivity.class));
        } else {
            //user is not signed in
            startActivity(new Intent(FeedActivity.this, LoginActivity.class));
        }
    }

    public void launchFeed(View view) {
        startActivity(new Intent(FeedActivity.this, FeedActivity.class));
    }

}