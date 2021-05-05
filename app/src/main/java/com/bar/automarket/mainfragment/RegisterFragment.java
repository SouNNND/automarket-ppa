package com.bar.automarket.mainfragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bar.automarket.MainActivity;
import com.bar.automarket.R;
import com.bar.automarket.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class RegisterFragment extends Fragment {

    EditText mEmail, mPassword, mUsername;
    Button mRegisterBtn, mLoginBtn;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

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
        mEmail = getActivity().findViewById(R.id.edit_email_register);
        mPassword = getActivity().findViewById(R.id.edit_password_register);
        mUsername = getActivity().findViewById(R.id.edit_username_register);
        mRegisterBtn = getActivity().findViewById(R.id.button_register_register);
        mLoginBtn = getActivity().findViewById(R.id.button_login_register);

        //firebase
        mAuth = FirebaseAuth.getInstance();

        //progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registering User..");

        //btn click
        mLoginBtn.setOnClickListener(v -> ((MainActivity)requireActivity()).displayRegister());

        mRegisterBtn.setOnClickListener(v -> {
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            String username = mUsername.getText().toString().trim();
            //validate
            if(username.length() < 5) {
                mUsername.setError("Username length at least 5 characters");
                mUsername.setFocusable(true);
            } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mEmail.setError("Invalid email");
                mEmail.setFocusable(true);
            } else if(password.length() <6) {
                mPassword.setError("Password length at least 6 characters");
                mPassword.setFocusable(true);
            } else {
                registerUser(username, email, password);
            }
        });
    }

    private void registerUser(String username, String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        progressDialog.dismiss();
                        FirebaseUser user = mAuth.getCurrentUser();

                        //Set user name
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username).build();
                        assert user != null;
                        user.updateProfile(profileUpdates);

                        //Validation
                        Toast.makeText(getActivity(), "Registered..\n"+user.getEmail(),
                                Toast.LENGTH_SHORT).show();
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