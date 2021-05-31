package com.bar.automarket.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bar.automarket.R;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    String make[], model[];
    int images[];
    Context context;

    public MyAdapter(Context context, String make[], String model[], int images[]) {
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
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyAdapter.MyViewHolder holder, int position) {
        holder.make.setText(make[position]);
        holder.model.setText(model[position]);
        //holder.imageView.setImageResource(images[position]);
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
