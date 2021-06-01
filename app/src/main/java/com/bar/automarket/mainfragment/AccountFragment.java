package com.bar.automarket.mainfragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bar.automarket.MainActivity;
import com.bar.automarket.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AccountFragment extends Fragment {

    private static final String TAG = "TAG";
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    String userId;

    Button logout, myAds;
    TextView welcomeMessage, email, phone;

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
        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        //Ui elements
        logout = requireView().findViewById(R.id.button_logout);
        myAds = requireView().findViewById(R.id.button_ads);
        welcomeMessage = requireView().findViewById(R.id.profile_message);
        email = requireView().findViewById(R.id.text_profile_email);
        phone = requireView().findViewById(R.id.text_profile_phone);

        //Get data from DB
        getDataAndUpdateViews();

        //Buttons listeners
        logout.setOnClickListener(v -> {
            mAuth.signOut();
            ((MainActivity)requireActivity()).displayAccount();
            ((MainActivity)requireActivity()).removeMyAdsFragment();
        });

        myAds.setOnClickListener(v -> {
            ((MainActivity)requireActivity()).displayMyAds();
        });
    }

    private void getDataAndUpdateViews() {
        DocumentReference documentReference = mStore.collection("users").document(userId);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                if (document.exists()) {
                    welcomeMessage.append(document.getString("username"));
                    phone.append(document.getString("phone"));
                    email.append(document.getString("email"));
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }


}