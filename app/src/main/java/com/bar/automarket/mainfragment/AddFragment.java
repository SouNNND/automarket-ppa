package com.bar.automarket.mainfragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bar.automarket.R;
import com.bar.automarket.data.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import java.util.UUID;

@SuppressWarnings("ALL")
public class AddFragment extends Fragment {

    private static final String TAG = "TAG";
    private static final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    FirebaseStorage storage;
    StorageReference storageReference;

    Button post, add;
    EditText make, model, fuel, displacement, power, mileage, year;
    String userId;
    ImageView imageView;

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
        //Storage for images
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //Ui elements
        post = requireActivity().findViewById(R.id.button_post);
        make = requireActivity().findViewById(R.id.editText_add_marca);
        model = requireActivity().findViewById(R.id.editText_add_model);
        fuel = requireActivity().findViewById(R.id.editText_add_combustibil);
        displacement = requireActivity().findViewById(R.id.editText_add_capacitate);
        power = requireActivity().findViewById(R.id.editText_add_putere);
        mileage = requireActivity().findViewById(R.id.editText_add_km);
        year = requireActivity().findViewById(R.id.editText_add_an);
        add = requireActivity().findViewById(R.id.button_add_image);
        imageView = requireView().findViewById(R.id.post_image_view);

        post.setOnClickListener(v -> {
            String imageID = UUID.randomUUID().toString();
            postAd(imageID);
            uploadImage(imageID);
        });

        add.setOnClickListener(view1 -> {
            selectImage();
        });

    }

    private void postAd(String imageID) {
        Post add = new Post(make.getText().toString().trim(), model.getText().toString().trim(),
                fuel.getText().toString().trim(), displacement.getText().toString().trim(),
                power.getText().toString().trim(), mileage.getText().toString().trim(),
                year.getText().toString().trim(), userId, imageID);
        mStore.collection("posts")
                .add(add)
                .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    private void selectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                requireActivity().getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(String imageID)
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(requireActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageReference.child("images/" + imageID);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(requireActivity(),
                                                    "Ad Posted!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                    clearFields();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(requireActivity(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }
    private void clearFields() {
        make.setText("");
        model.setText("");
        fuel.setText("");
        displacement.setText("");
        power.setText("");
        mileage.setText("");
        year.setText("");
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_directions_car_24));
    }
}