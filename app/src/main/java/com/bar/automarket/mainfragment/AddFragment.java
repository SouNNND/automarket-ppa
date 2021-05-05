package com.bar.automarket.mainfragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bar.automarket.R;
import com.bar.automarket.data.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AddFragment extends Fragment {

    private static final String TAG = "TAG";
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;

    Button post;
    EditText make, model, fuel, displacement, power, mileage, year;
    String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        //Ui elements
        post = requireActivity().findViewById(R.id.button_post);
        make = requireActivity().findViewById(R.id.editText_add_marca);
        model = requireActivity().findViewById(R.id.editText_add_model);
        fuel = requireActivity().findViewById(R.id.editText_add_combustibil);
        displacement = requireActivity().findViewById(R.id.editText_add_capacitate);
        power = requireActivity().findViewById(R.id.editText_add_putere);
        mileage = requireActivity().findViewById(R.id.editText_add_km);
        year = requireActivity().findViewById(R.id.editText_add_an);

        post.setOnClickListener(v -> {
            Post add = new Post(make.getText().toString().trim(), model.getText().toString().trim(),
                    fuel.getText().toString().trim(), displacement.getText().toString().trim(),
                    power.getText().toString().trim(), mileage.getText().toString().trim(),
                    year.getText().toString().trim(), userId);
            mStore.collection("posts")
                    .add(add)
                    .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId()))
                    .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
        });
    }

}