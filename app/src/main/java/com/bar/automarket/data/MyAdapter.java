package com.bar.automarket.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bar.automarket.AdInfoActivity;
import com.bar.automarket.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private static final String TAG = "IMAGE";
    String make[], model[];
    String images[];
    Context context;

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public MyAdapter(Context context, String make[], String model[], String images[]) {
        this.context = context;
        this.make = make;
        this.model = model;
        this.images = images;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.feed_row, parent, false);
        view.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdInfoActivity.class);
            context.startActivity(intent);
        });
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyAdapter.MyViewHolder holder, int position) {
        holder.make.setText(make[position]);
        holder.model.setText(model[position]);


        //Download images from Storage
        StorageReference img = storageReference.child("images/" + images[position]);
        //StorageReference img = storageReference.child("images/64ecfc48-e942-4817-810b-dec77a5f7c74.jpg");
        Log.d(TAG, img.toString());

        GlideApp.with(context)
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
                }).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return model.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView make, model;
        ImageView imageView;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            make = itemView.findViewById(R.id.ad_make);
            model = itemView.findViewById(R.id.ad_model);
            imageView = itemView.findViewById(R.id.ad_image_view);
        }

    }
}
