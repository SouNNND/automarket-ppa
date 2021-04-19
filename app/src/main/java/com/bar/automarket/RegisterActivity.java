package com.bar.automarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    EditText mEmail, mPassword, mUsername;
    Button mRegisterBtn;
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar

        //init
        mEmail = findViewById(R.id.edit_email_register);
        mPassword = findViewById(R.id.edit_password_register);
        mUsername = findViewById(R.id.edit_username_register);
        mRegisterBtn = findViewById(R.id.button_register_register);

        //firebase
        mAuth = FirebaseAuth.getInstance();

        //progres dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User..");

        //register btn click
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
                registerUser(email, password);
            }
        });
    }

    private void registerUser(String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, dismiss dialog and start register activity
                        progressDialog.dismiss();
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(RegisterActivity.this, "Registered..\n"+user.getEmail(),
                                Toast.LENGTH_SHORT).show();
                        backToLoginActivity();
                    } else {
                        // If sign in fails, display a message to the user.
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void backToLoginActivity(View view) {
        this.finish();
    }
    public void backToLoginActivity() {
        this.finish();
    }
}