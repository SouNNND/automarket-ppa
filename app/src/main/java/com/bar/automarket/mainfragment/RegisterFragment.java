package com.bar.automarket.mainfragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bar.automarket.MainActivity;
import com.bar.automarket.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterFragment extends Fragment {

    private static final String TAG = "TAG";
    EditText mEmail, mPassword, mUsername, mPhone;
    Button mRegisterBtn;
    TextView mLoginBtn;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //init
        mEmail = requireActivity().findViewById(R.id.edit_email_register);
        mPassword = requireActivity().findViewById(R.id.edit_password_register);
        mUsername = requireActivity().findViewById(R.id.edit_username_register);
        mRegisterBtn = requireActivity().findViewById(R.id.button_register_register);
        mLoginBtn = requireActivity().findViewById(R.id.button_login_register);
        mPhone = requireActivity().findViewById(R.id.edit_phone_register);


        //firebase
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();


        //progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registering User..");

        //btn click
        mLoginBtn.setOnClickListener(v -> ((MainActivity)requireActivity()).displayRegister());

        mRegisterBtn.setOnClickListener(v -> {
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            String username = mUsername.getText().toString().trim();
            String phone = mPhone.getText().toString().trim();
            //validate
            if(username.length() < 5) {
                mUsername.setError("Username length at least 5 characters");
                mUsername.setFocusable(true);
            } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mEmail.setError("Invalid email");
                mEmail.setFocusable(true);
            } else if(!Patterns.PHONE.matcher(phone).matches()) {
                mPhone.setError("Invalid phone number");
                mPhone.setFocusable(true);
            } else if(password.length() <6) {
                mPassword.setError("Password length at least 6 characters");
                mPassword.setFocusable(true);
            } else {
                registerUser(username, email, password, phone);
            }
        });
    }

    private void registerUser(String username, String email, String password, String phone) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        progressDialog.dismiss();

                        //Validation
                        Toast.makeText(getActivity(), "Registered..\n"+email,
                                Toast.LENGTH_SHORT).show();
                        //Store data
                        userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                        DocumentReference documentReference = mStore.collection("users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("username", username);
                        user.put("email", email);
                        user.put("phone", phone);
                        documentReference.set(user).addOnSuccessListener(aVoid -> Log.d(TAG, "onSuccess: user Profile is created for " + userID));
                        //return to login
                        ((MainActivity)requireActivity()).displayRegister();
                    } else {
                        // If sign in fails, display a message to the user.
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}