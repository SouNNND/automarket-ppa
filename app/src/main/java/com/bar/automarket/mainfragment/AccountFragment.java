package com.bar.automarket.mainfragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bar.automarket.LoginActivity;
import com.bar.automarket.MainActivity;
import com.bar.automarket.ProfileActivity;
import com.bar.automarket.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.Objects;
import java.util.concurrent.Executor;

public class AccountFragment extends Fragment {

    private static final String TAG = "TAG";
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    String userId;

    Button logout;
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
        welcomeMessage = requireView().findViewById(R.id.profile_message);
        email = requireView().findViewById(R.id.text_profile_email);
        phone = requireView().findViewById(R.id.text_profile_phone);

        //Get data from DB
        getDataAndUpdateViews();

        //Buttons listeners
        logout.setOnClickListener(v -> {
            mAuth.signOut();
            ((MainActivity)requireActivity()).displayAccount();
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