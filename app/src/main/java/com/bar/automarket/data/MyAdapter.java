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

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private static final String TAG = "IMAGE";
    Context context;
    private ArrayList<Post> mPosts;
    private OnAdListener mOnAdListener;

    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public MyAdapter(Context context, ArrayList<Post> mPosts, OnAdListener onAdListener) {
        this.context = context;
        this.mPosts = mPosts;
        this.mOnAdListener = onAdListener;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.feed_row, parent, false);
        return new MyViewHolder(view, mOnAdListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyAdapter.MyViewHolder holder, int position) {
        holder.make.setText(mPosts.get(position).getMake());
        holder.model.setText(mPosts.get(position).getModel());
        holder.price.setText("€" + mPosts.get(position).getPrice());

        //Download images from Storage
        StorageReference img = storageReference.child("images/" + mPosts.get(position).getImgId());
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
        return mPosts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView make, model, price;
        ImageView imageView;
        OnAdListener mOnAdListener;

        public MyViewHolder(@NonNull @NotNull View itemView, OnAdListener onAdListener) {
            super(itemView);
            make = itemView.findViewById(R.id.ad_make);
            model = itemView.findViewById(R.id.ad_model);
            price = itemView.findViewById(R.id.ad_price);
            imageView = itemView.findViewById(R.id.ad_image_view);
            mOnAdListener = onAdListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnAdListener.onAdClick(getAdapterPosition());
        }
    }

    public interface OnAdListener {
        void onAdClick(int position);
    }
}
