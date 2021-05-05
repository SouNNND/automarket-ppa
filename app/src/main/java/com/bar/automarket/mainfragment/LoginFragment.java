package com.bar.automarket.mainfragment;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.bar.automarket.LoginActivity;
import com.bar.automarket.MainActivity;
import com.bar.automarket.ProfileActivity;
import com.bar.automarket.R;
import com.bar.automarket.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.concurrent.Executor;

public class LoginFragment extends Fragment {

    EditText mEmail, mPassword;
    Button mLoginBtn, mRegisterBtn;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init
        mEmail = getView().findViewById(R.id.edit_email_login);
        mPassword = getView().findViewById(R.id.edit_password_login);
        mLoginBtn = getView().findViewById(R.id.button_login_login);
        mRegisterBtn = getView().findViewById(R.id.button_register_login);

        //firebase
        mAuth = FirebaseAuth.getInstance();

        //progress dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Logging in..");

        //handle login button click
        mRegisterBtn.setOnClickListener((View v) -> ((MainActivity)requireActivity()).displayRegister());
        mLoginBtn.setOnClickListener(v -> {
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            //validate
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mEmail.setError("Invalid email");
                mEmail.setFocusable(true);
            } else {
                loginUser(email, password);
            }
        });

    }

    private void loginUser(String email, String password) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        progressDialog.dismiss();
                        ((MainActivity)requireActivity()).displayAccount();
                    } else {
                        // If sign in fails, display a message to the user.
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        });
    }


    public void launchRegisterActivity(View view) {

    }
}