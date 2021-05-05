package com.bar.automarket.mainfragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bar.automarket.LoginActivity;
import com.bar.automarket.MainActivity;
import com.bar.automarket.ProfileActivity;
import com.bar.automarket.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class AccountFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    Button logout;
    TextView welcomeMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        logout = getView().findViewById(R.id.button_logout);
        welcomeMessage = getView().findViewById(R.id.profile_message);

        welcomeMessage.setText(getString(R.string.message_welcome) + firebaseAuth.getCurrentUser().getDisplayName());

        logout.setOnClickListener(v -> {
            firebaseAuth.signOut();
            ((MainActivity)requireActivity()).displayAccount();
        });
    }



}