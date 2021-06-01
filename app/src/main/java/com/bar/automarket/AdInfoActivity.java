package com.bar.automarket;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bar.automarket.data.GlideApp;
import com.bar.automarket.data.User;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AdInfoActivity extends AppCompatActivity {

    TextView modelTextView, makeTextView;
    TextView yearTextView, mileageTextView;
    TextView fuelTextView, displacementTextView, powerTextView;
    TextView usernameTextView, phoneTextView, priceTextView;
    ImageView imageView;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    //firebase
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    User user = new User();

    //debug
    private static final String TAG2 = "SECOND ACTIVITY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ad_info);

        setWindow();
        modelTextView = findViewById(R.id.info_model);
        makeTextView = findViewById(R.id.info_make);
        imageView = findViewById(R.id.info_image);
        yearTextView = findViewById(R.id.info_year);
        mileageTextView = findViewById(R.id.info_mileage);
        fuelTextView = findViewById(R.id.info_fuel);
        displacementTextView = findViewById(R.id.info_displacement);
        powerTextView = findViewById(R.id.info_power);
        usernameTextView = findViewById(R.id.info_user_name);
        phoneTextView = findViewById(R.id.info_user_phone);
        priceTextView = findViewById(R.id.info_price);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            modelTextView.setText(extras.getString("model"));
            makeTextView.setText(extras.getString("make"));
            yearTextView.setText(extras.getString("year"));
            mileageTextView.setText(extras.getString("mileage") + " Km");
            fuelTextView.setText(extras.getString("fuel"));
            displacementTextView.setText(extras.getString("displacement") + " cc");
            powerTextView.setText(extras.getString("power") + " HP");
            priceTextView.setText(extras.getString("price") + "€");

            getAndShowUserData(extras.getString("userId"));
            showImage(extras.getString("imgId"));
        }
    }

    private void showImage(String imgId) {
        StorageReference img = storageReference.child("images/" + imgId);
        //load image
        GlideApp.with(this)
                .load(img)
                .error(R.drawable.ic_baseline_directions_car_24)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(imageView);
    }

    private void getAndShowUserData(String userId) {
        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnSuccessListener(documentSnapshot ->  {
            user = documentSnapshot.toObject(User.class);
            usernameTextView.setText(user.getUsername());
            phoneTextView.setText(user.getPhone());
        });

    }

    private void setWindow() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .85), (int) (height * .7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;
        getWindow().setAttributes(params);
    }
}